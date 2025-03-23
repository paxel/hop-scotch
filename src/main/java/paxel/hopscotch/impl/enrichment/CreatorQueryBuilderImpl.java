package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.enrichment.CreatorQueryBuilder;

import java.util.List;

/**
 *
 */
public class CreatorQueryBuilderImpl implements CreatorQueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * @param enrichments
     */
    public CreatorQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public CreatorQueryBuilder matchExact(Creator creator) {
        return null;
    }
}
