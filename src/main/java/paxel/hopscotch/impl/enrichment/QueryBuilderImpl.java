package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.*;

import java.util.List;

/**
 * The central QueryBuilder. It creates specific QueryBuilders for different parts of the Enrichment
 */
public class QueryBuilderImpl implements QueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * @param enrichments The remaining enrichments.
     */
    public QueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = List.copyOf(enrichments);
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
    public ValueQueryBuilder queryValue() {
        return new ValueQueryBuilderImpl(enrichments);
    }
}
