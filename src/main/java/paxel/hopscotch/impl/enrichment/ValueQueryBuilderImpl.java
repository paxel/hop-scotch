package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.*;
import paxel.hopscotch.api.enrichment.values.DoubleValueProvider;
import paxel.hopscotch.api.enrichment.values.IntValueProvider;
import paxel.hopscotch.api.enrichment.values.LongValueProvider;
import paxel.hopscotch.impl.enrichment.values.DoubleValueProviderImpl;
import paxel.hopscotch.impl.enrichment.values.IntValueProviderImpl;
import paxel.hopscotch.impl.enrichment.values.LongValueProviderImpl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Implements the {@link ValueQueryBuilder} which reduces the available enrichments depending on the Creator.
 */
public class ValueQueryBuilderImpl implements ValueQueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with the remaining {@link Enrichment}s
     *
     * @param enrichments The remaining enrichments.
     */
    public ValueQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }

    @Override
    public KeyQueryBuilder queryKey() {
        return new KeyQueryBuilderImpl(enrichments);
    }

    @Override
    public StageQueryBuilder queryStage() {
        return new StageQueryBuilderImpl(enrichments);
    }

    @Override
    public CreatorQueryBuilder queryCreator() {
        return new CreatorQueryBuilderImpl(enrichments);
    }

    @Override
    public IntValueProvider asInt() {
        return new IntValueProviderImpl(enrichments.stream().filter(e -> e.value().getType() == Value.Type.INTEGER).toList());
    }

    @Override
    public LongValueProvider asLong() {
        return new LongValueProviderImpl(enrichments.stream().filter(e -> e.value().getType() == Value.Type.LONG).toList());
    }

    @Override
    public DoubleValueProvider asDouble() {
        return new DoubleValueProviderImpl(enrichments.stream().filter(e -> e.value().getType() == Value.Type.DOUBLE).toList());
    }


    @Override
    public Stream<Enrichment> stream() {
        return enrichments.stream().map(Function.identity());
    }

}
