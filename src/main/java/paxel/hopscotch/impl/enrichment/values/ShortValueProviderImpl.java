package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.ShortValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides Short values as objects
 */
public class ShortValueProviderImpl implements ShortValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public ShortValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<Short> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.SHORT)
                .map(value -> ((Value.ShortValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.SHORT;
    }
}
