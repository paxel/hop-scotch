package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.api.Stage;

import java.util.stream.Stream;

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

    /**
     * Create a {@link KeyQueryBuilder} for the filtered Enrichments
     *
     * @return a new queryBuilder
     */
    KeyQueryBuilder queryKey();

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

    /**
     * Get the enrichments as a Stream.
     *
     * @return a stream of enrichments
     */
    Stream<Enrichment> stream();
}
