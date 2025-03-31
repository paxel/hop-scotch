package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.BigDecimalValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides BigDecimal values as objects
 */
public class BigDecimalValueProviderImpl implements BigDecimalValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public BigDecimalValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<BigDecimal> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.BIG_DECIMAL)
                .map(value -> ((Value.BigDecimalValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.BIG_DECIMAL;
    }
}
