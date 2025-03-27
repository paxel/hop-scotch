package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.*;

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
    public Stream<Enrichment> stream() {
        return enrichments.stream().map(Function.identity());
    }

}
