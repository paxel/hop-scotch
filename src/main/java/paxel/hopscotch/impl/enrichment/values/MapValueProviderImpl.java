package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.MapValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Provides Map values as objects
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public class MapValueProviderImpl<K, V> implements MapValueProvider<K, V> {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public MapValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<Map<K, V>> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.MAP)
                // We don't check types here
                .map(v -> ((Value.MapValue<K, V>) v).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.MAP;
    }
}
