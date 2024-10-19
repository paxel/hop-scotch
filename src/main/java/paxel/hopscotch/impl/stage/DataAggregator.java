package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.enrichment.Key;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Is responsible for combining all results of a stage in case the data was sent (fragmented) to multiple Hops and Gates.
 *
 * @param <D> The data type
 */
public class DataAggregator<D> {

    private final Map<UUID, Boolean> drop = new HashMap<>();
    private final Map<UUID, Integer> expected = new HashMap<>();
    private final Map<UUID, AtomicInteger> received = new HashMap<>();
    private final Map<UUID, HopScotchEnrichedData<D>> aggregation = new HashMap<>();


    /**
     * Update the aggregation with the number of expected fragments.
     * There are two possible outcomes:
     * The fragments have already been received, and the aggregation is already finished.
     * Or there are not enough fragments yet or the data is dropped.
     * <br>
     * If the last message for the data is received, all information about this data is deleted.
     *
     * @param hopScotchData     The data
     * @param expectedFragments The number of fragments expected
     * @return the aggregated result or empty
     */
    public Optional<HopScotchEnrichedData<D>> update(HopScotchEnrichedData<D> hopScotchData, int expectedFragments) {
        AtomicInteger atomicInteger = received.get(hopScotchData.getId());
        if (atomicInteger != null && atomicInteger.get() == expectedFragments) {
            return cleanRemoval(hopScotchData.getId());
        }
        expected.putIfAbsent(hopScotchData.getId(), expectedFragments);
        return Optional.empty();
    }


    /**
     * Add another fragment to the aggregation.
     * There are two possible outcomes:
     * This is the last fragment and finishes the aggregation.
     * Or there are fragments missing, or the number of fragments is not yet defined, or the data is dropped.
     * <br>
     * If the last message for the data is received, all information about this data is deleted.
     *
     * @param fragment A fragment
     * @return the aggregated result or empty
     */
    public Optional<HopScotchEnrichedData<D>> add(HopScotchEnrichedData<D> fragment) {
        aggregation.compute(fragment.getId(), (k, old) -> {
            if (old == null) {
                // The first received fragment becomes the aggregation seed
                return fragment;
            }
            // Merge previous with new fragment
            return new HopScotchEnrichedData<>(old.getData(), mergeEnrichments(old.getEnrichments(), fragment.getEnrichments()), old.getId());
        });

        int fragmentsReceived = received.computeIfAbsent(fragment.getId(), k -> new AtomicInteger()).incrementAndGet();
        if (fragmentsReceived < expected.getOrDefault(fragment.getId(), 0)) {
            // We have both received the fragment count or not enough fragments
            return Optional.empty();
        }

        // We have enough fragments
        return cleanRemoval(fragment.getId());
    }


    /**
     * Marks the data as dropped.
     * There are two possible outcomes:
     * This is the last fragment and this data is deleted.
     * Or there are fragments missing, or the number of fragments is not yet defined.
     * <br>
     * If the last message for the data is received, all information about this data is deleted.
     *
     * @param hopScotchData The data
     */
    public void drop(HopScotchEnrichedData<D> hopScotchData) {
        this.drop.put(hopScotchData.getId(), true);
        int expected = this.expected.getOrDefault(hopScotchData.getId(), 0);
        int received = this.received.getOrDefault(hopScotchData.getId(), new AtomicInteger()).get();
        if (expected > 0 && expected == received) {
            cleanRemoval(hopScotchData.getId());
        }
    }

    private List<EnrichmentImpl> mergeEnrichments(List<EnrichmentImpl> old, List<EnrichmentImpl> add) {
        ArrayList<EnrichmentImpl> enrichments = new ArrayList<>(old);
        Set<Key> collect = old.stream().map(EnrichmentImpl::key).collect(Collectors.toSet());
        add.stream().filter(f -> !collect.contains(f.key())).forEach(enrichments::add);
        return enrichments;
    }

    private Optional<HopScotchEnrichedData<D>> cleanRemoval(UUID id) {
        received.remove(id);
        expected.remove(id);
        Boolean drop = this.drop.remove(id);
        HopScotchEnrichedData<D> remove = aggregation.remove(id);
        if (Boolean.TRUE.equals(drop)) {
            return Optional.empty();
        }
        return Optional.ofNullable(remove);
    }

}
