package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.enrichment.CreatorQueryBuilder;

import java.util.List;

/**
 * Implements the {@link CreatorQueryBuilder} which reduces the available enrichments depending on the Creator.
 */
public class CreatorQueryBuilderImpl implements CreatorQueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with the remaining {@link Enrichment}s
     *
     * @param enrichments The remaining enrichments.
     */
    public CreatorQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public CreatorQueryBuilder matchExact(Creator creator) {
        return new CreatorQueryBuilderImpl(enrichments.stream().filter(e -> e.creator().equals(creator)).toList());
    }
}
