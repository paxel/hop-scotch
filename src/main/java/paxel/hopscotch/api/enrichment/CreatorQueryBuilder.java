package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.api.Creator;

import java.util.stream.Stream;

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
     * Create a  for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    CreatorQueryBuilder queryCreator();

    /**
     * Create a {@link KeyQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    KeyQueryBuilder queryKey();

    /**
     * Get the enrichments as a Stream.
     *
     * @return a stream of enrichments
     */
    Stream<Enrichment> stream();

}
