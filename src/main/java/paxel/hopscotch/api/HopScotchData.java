package paxel.hopscotch.api;


import paxel.hopscotch.api.enrichment.*;


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
     * Create a {@link KeyQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    KeyQueryBuilder queryKey();

    /**
     * Create a {@link StageQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    StageQueryBuilder queryStage();

    /**
     * Create a {@link ValueQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    ValueQueryBuilder queryValue();

    /**
     * Create a {@link CreatorQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    CreatorQueryBuilder queryCreator();

}
