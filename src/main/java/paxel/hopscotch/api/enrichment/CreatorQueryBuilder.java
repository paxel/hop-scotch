package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.api.Creator;

/**
 * The Query-builder for querying or filtering for creators.
 */
public interface CreatorQueryBuilder {


    /**
     * Create a new CreatorQueryBuilder that has only the entities with the given key
     *
     * @param creator The key value
     * @return the CreatorQueryBuilder for the filtered enrichments
     */
    CreatorQueryBuilder matchExact(Creator creator);
}
