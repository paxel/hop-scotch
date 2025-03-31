package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.DateValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides ZonedDateTime values as objects
 */
public class DateValueProviderImpl implements DateValueProvider {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public DateValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<ZonedDateTime> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.DATE)
                .map(value -> ((Value.DateValue) value).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.DATE;
    }
}
