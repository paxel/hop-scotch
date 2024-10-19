package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.enrichment.Key;
import paxel.hopscotch.impl.data.EnrichmentImpl;
import paxel.hopscotch.impl.data.HopScotchDataInternal;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataAggregator<D> {

    private final Map<UUID, Boolean> drop = new HashMap<>();
    private final Map<UUID, Integer> expected = new HashMap<>();
    private final Map<UUID, AtomicInteger> received = new HashMap<>();
    private final Map<UUID, HopScotchDataInternal<D>> aggregation = new HashMap<>();


    /**
     * Retrieves the aggregation if completely finished, otherwise stores the number of ragments needed.
     *
     * @param hopScotchData     The data
     * @param expectedFragments The number of fragments expected
     * @return the aggregated result or empty
     */
    public Optional<HopScotchDataInternal<D>> update(HopScotchDataInternal<D> hopScotchData, int expectedFragments) {
        AtomicInteger atomicInteger = received.get(hopScotchData.getId());
        if (atomicInteger != null && atomicInteger.get() == expectedFragments) {
            return cleanRemoval(hopScotchData.getId());
        }
        expected.computeIfAbsent(hopScotchData.getId(), k -> expectedFragments);
        return Optional.empty();
    }

    private Optional<HopScotchDataInternal<D>> cleanRemoval(UUID id) {
        received.remove(id);
        expected.remove(id);
        Boolean drop = this.drop.remove(id);
        HopScotchDataInternal<D> remove = aggregation.remove(id);
        if (Boolean.TRUE.equals(drop)) {
            return Optional.empty();
        }
        return Optional.ofNullable(remove);
    }

    public Optional<HopScotchDataInternal<D>> add(HopScotchDataInternal<D> fragment) {
        aggregation.compute(fragment.getId(), (k, old) -> {
            if (old == null) {
                // The first received fragment becomes the aggregation seed
                return fragment;
            }
            // Merge previous with new fragment
            return new HopScotchDataInternal<>(old.getData(), mergeEnrichments(old.getEnrichments(), fragment.getEnrichments()), old.getId());
        });

        int fragmentsReceived = received.computeIfAbsent(fragment.getId(), k -> new AtomicInteger()).incrementAndGet();
        if (fragmentsReceived < expected.getOrDefault(fragment.getId(), 0)) {
            // We have both received the fragment count or not enough fragments
            return Optional.empty();
        }

        // We have enough fragments
        return cleanRemoval(fragment.getId());
    }

    private List<EnrichmentImpl> mergeEnrichments(List<EnrichmentImpl> old, List<EnrichmentImpl> add) {
        ArrayList<EnrichmentImpl> enrichments = new ArrayList<>(old);
        Set<Key> collect = old.stream().map(EnrichmentImpl::key).collect(Collectors.toSet());
        add.stream().filter(f -> !collect.contains(f.key())).forEach(enrichments::add);
        return enrichments;
    }

    public void drop(HopScotchDataInternal<D> hopScotchData) {
        this.drop.put(hopScotchData.getId(), true);
        int expected = this.expected.getOrDefault(hopScotchData.getId(), 0);
        int received = this.received.getOrDefault(hopScotchData.getId(), new AtomicInteger()).get();
        if (expected > 0 && expected == received) {
            cleanRemoval(hopScotchData.getId());
        }
    }
}
