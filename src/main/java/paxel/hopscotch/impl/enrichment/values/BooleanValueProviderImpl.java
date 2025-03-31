package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.BooleanValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides Boolean values as objects
 */
public class BooleanValueProviderImpl implements BooleanValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public BooleanValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<Boolean> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.BOOLEAN)
                .map(value -> ((Value.BooleanValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.BOOLEAN;
    }
}
