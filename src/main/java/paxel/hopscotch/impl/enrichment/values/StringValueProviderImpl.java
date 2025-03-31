package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.StringValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides Integer values as objects or primitives
 */
public class StringValueProviderImpl implements StringValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public StringValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<String> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.STRING)
                .map(value -> ((Value.StringValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.STRING;
    }
}
