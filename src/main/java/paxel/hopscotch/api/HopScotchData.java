package paxel.hopscotch.api;


import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.QueryBuilder;


/**
 * Describes how the Data is presented to the pipeline stages.
 *
 * @param <D> the type of the data
 */
public interface HopScotchData<D> {

    /**
     * Retrieve the original data.
     * This data should not be modified to prevent concurrency issues.
     * The processors of one stage might access this data concurrently.
     *
     * @return The data.
     */
    D getData();

    /**
     * The method to add an Enrichment to the data.
     * The process is self-explorable in the IDE.
     * You get a {@link KeyBuilder} which will provide you with a Value Builder which will add the enriched Data.
     *
     * @return the KeyBuilder.
     */
    KeyBuilder startEnrichment();

    /**
     * The method to query data from previous enrichments.
     * The process is self-explorable in the IDE.
     * You get a {@link QueryBuilder} that will give you either the requested data or more QueryBuilders to refine the data.
     *
     * @return the QueryBuilder.
     */
    QueryBuilder startEnrichmentQuery();

}
