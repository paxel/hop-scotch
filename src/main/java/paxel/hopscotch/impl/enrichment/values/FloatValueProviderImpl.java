package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.FloatValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides Float values as objects
 */
public class FloatValueProviderImpl implements FloatValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public FloatValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<Float> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.FLOAT)
                .map(value -> ((Value.FloatValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.FLOAT;
    }
}
