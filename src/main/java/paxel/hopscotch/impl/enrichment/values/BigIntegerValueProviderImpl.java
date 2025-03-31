package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.BigIntegerValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides BigInteger values as objects
 */
public class BigIntegerValueProviderImpl implements BigIntegerValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public BigIntegerValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<BigInteger> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.BIG_INTEGER)
                .map(value -> ((Value.BigIntegerValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.BIG_INTEGER;
    }
}
