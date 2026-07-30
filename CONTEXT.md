# Hop-Scotch

> **Archived prototype.** This glossary describes the Java implementation in this
> repository, which is no longer developed. The successor's vocabulary — which drops
> `Fragment`, renames `Data` to `Datum`, and adds `Source`, `Origin`, `Partition`,
> `Dead letter` and `Event` — lives in `../hopscotch-rs/CONTEXT.md`.

A framework for self-managing data pipelines. The user declares numbered processing steps and the conditions under which each applies; the framework builds and runs the actor chain that carries data through them.

## Language

### Pipeline structure

**Stage**:
A numbered position in the pipeline. Stages run in ascending numeric order, and every Data passes through each one.
_Avoid_: step, phase, level

**Hop**:
User-supplied business logic that processes one Data at a Stage. A Hop may add Enrichments but need not — processing for side effects alone is legitimate.
_Avoid_: processor, task, handler, worker

**Gate**:
A decision point at a Stage that says whether a Data may continue. If any Gate at a Stage refuses, the Data is dropped and never reaches the next Stage.
_Avoid_: filter, predicate, guard

**Judge**:
Decides, for each Data arriving at a Stage, whether a Hop should handle it and which Hop that is.
_Avoid_: matcher, router, selector

**Judgment**:
A Judge's verdict on one Data. An accepted Judgment names a HopId and yields the Hop that will process it.
_Avoid_: decision, verdict, match

**HopId**:
The identity that partitions Data between Hop instances belonging to the same Judge. Two Data with the same HopId are processed by the same Hop instance.
_Avoid_: hop key, partition key, shard

### Data and enrichment

**Data**:
A single item travelling through the pipeline, together with every Enrichment attached to it so far. Used as a singular noun.
_Avoid_: record, item, message, payload

**Enrichment**:
A fact a Hop, Gate, or Stage attaches to a Data — a Key, a Value, and the Stage and Creator that produced it. Enrichments accumulate as the Data descends the Stages; nothing is ever removed.
_Avoid_: annotation, attribute, metadata, tag

**Key**:
The dotted path identifying an Enrichment (`image.type`, `germany.saxony.dresden`). A Key identifies at most one Enrichment per Data — two producers writing the same Key to one Data is a programming error.
_Avoid_: path, name, field

**Value**:
The typed content of an Enrichment. Every Value belongs to one of the framework's supported types.
_Avoid_: data, content, payload

**Creator**:
Names the *slot* that produced an Enrichment — a particular Stage, Gate, or Hop position in the pipeline definition — not the individual instance occupying it. All Hop instances spawned by one Judge share a Creator.
_Avoid_: author, source, origin, producer

### Flow

**Fragment**:
One of the several Data that exist when a Stage hands the same incoming Data to more than one Hop or Gate. Fragments share their originating Data's identity and are recombined before the next Stage.
_Avoid_: copy, clone, duplicate, branch

**Aggregation**:
Recombining a Data's Fragments once every Hop and Gate at a Stage has finished with them, so that one Data continues to the next Stage.
_Avoid_: merge, join, collect, reduce

**Statistics**:
Counters describing the pipeline's progress and status. Statistics keys are a separate namespace from Enrichment Keys and describe the pipeline, not the Data.
_Avoid_: metrics, counters, telemetry
