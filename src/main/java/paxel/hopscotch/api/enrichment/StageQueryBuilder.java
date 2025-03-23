package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.api.Stage;

/**
 * The Query-builder for querying or filtering for stages.
 */
public interface StageQueryBuilder {


    /**
     * Create a new StageQueryBuilder that has only the entities with the given key
     *
     * @param stage The key value
     * @return the StageQueryBuilder for the filtered enrichments
     */
    StageQueryBuilder matchExact(Stage stage);
}
