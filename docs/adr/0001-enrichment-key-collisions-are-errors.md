---
status: carried forward — not implemented here
---

# Enrichment Key collisions are errors, detected by uuid identity

> **This repository is an archived prototype.** The decision below was accepted and
> carried into the Rust successor at `../hopscotch-rs` (see its
> `docs/adr/0001-enrichment-key-collisions-are-errors.md`). It is deliberately **not**
> implemented in Java: all three prerequisite fixes are in `DataAggregator` and the actor
> message protocol, which the successor's execution model deletes entirely. The reasoning
> below remains accurate about this codebase and is why the successor decided as it did.

A Key identifies at most one Enrichment per Data. Two producers writing the same Key to one Data is a programming error, not a merge conflict to be resolved by a rule. Identity for de-duplication will be `EnrichmentImpl.uuid`, never the Key and never record equality. Collisions will be enforced at two points with different policies, because only one of them has user code on the stack to fail into.

**Implementation is pending — no source file has been changed.** This records the decision and the reasoning behind it.

## Why uuid is the identity

Every Fragment carries the full inherited Enrichment list, because `HopScotchEnrichedData.copy()` copies the list shallowly and shares the `EnrichmentImpl` instances. So aggregation must remove inherited duplicates while keeping every genuinely new Enrichment. Only `uuid` separates the two cases:

|          | inherited from an earlier Stage | written by two sibling Hops |
| -------- | ------------------------------- | --------------------------- |
| `Key`    | same                            | same                        |
| `uuid`   | **same**                        | **different**               |

`DataAggregator` currently de-duplicates by Key, which conflates them: a genuine collision is silently swallowed and the surviving Enrichment depends on Fragment arrival order.

Record equality was rejected as the identity. `Value.ObjectValue`, `CollectionValue` and `MapValue` wrap arbitrary user objects, so `EnrichmentImpl.equals` would delegate to user `equals()` — possibly unimplemented, expensive, or throwing. Hashing a UUID is O(1) and total.

## Two enforcement points

**Write time — `HopScotchEnrichedData.add()` will throw.** Enrichment writes terminate here via `HopScotchEnrichedDataWrapper.startEnrichment()`, on the Hop's own call stack, so the exception can name the Key, the Creator and Stage already holding it, and the Creator and Stage attempting to overwrite it. `HopActor` and `GateActor` already catch `RuntimeException` and record an `invalid_result` statistic, so no new failure path is introduced.

This catches a Hop writing the same Key twice, and a Hop overwriting a Key inherited from an earlier Stage. It cannot catch sibling Hops, which write to separate Fragments and never see each other.

**Aggregation — keep the first Enrichment and record an `invalid_result` statistic.** A sibling collision only becomes visible when Fragments recombine in `DataAggregator`, where `StageActor` is processing a message and there is no user code to fail into; throwing there would kill the Stage actor rather than the offending Hop. The Data continues with a nondeterministic winner, but the collision is counted rather than silent. Dropping the Data was rejected as too destructive for what is usually a pipeline definition error, and failing the system was rejected as too severe for a condition detected at runtime per Data.

## Hop enrichment becomes transactional

A Hop's Enrichments will be buffered per invocation and committed only when `process` returns cleanly. Today they are appended to the live Data as they are written, so a Hop that throws part-way leaves its earlier Enrichments behind — which is why `HopActor`'s "we forward the input instead the output" comment does not describe what actually happens. With write-time collision detection a mid-Hop throw stops being exotic, so partial writes have to stop being possible.

## Consequences

Three defects in the surrounding code must be fixed for this to hold, none of them cosmetic:

- **`KeyImpl` must normalise its path to `List.copyOf` at construction.** `KeyFactory.forString` and `path` build over `Arrays.asList` while `collection()` stores the caller's `Collection` as-is. `KeyImpl` is a record, so a `Set`-backed Key never equals a `List`-backed Key with the same path, and collision detection would silently miss.
- **A drop must count as an arrival.** `DataAggregator.drop()` marks the Data dropped without incrementing `received`, and a refusing Gate sends `Drop` rather than `Fragment`. With two Hops and one Gate, `expected` is 3 while `received` never exceeds 2, so the aggregation state for that Data is never released.
- **The completion guard must test `expected.containsKey(id)`.** `fragmentsReceived < expected.getOrDefault(id, 0)` evaluates `1 < 0` when the fragment count is not yet known, completing a one-Fragment aggregation. `StageActor.processCompleteData` distributes to Hops before sending `Split`, so a fast Hop's `Fragment` can reach the next Stage before the `Split` announcing it. An explicit "count not yet known" test is preferred over relying on send order: it holds regardless of the actor framework's delivery guarantees, and sending `Split` first would push a potentially blocking `tellWithBackPressure` ahead of the work distribution.

The aggregation accumulator becomes `Map<UUID, EnrichmentImpl>` for uniqueness plus `Map<Key, UUID>` for collision detection, scanning every Enrichment of every Fragment including the seed — the first Fragment can no longer be adopted wholesale. This also removes the quadratic re-merge, since the current implementation rebuilds a `HashSet` of Keys on each pairwise merge.
