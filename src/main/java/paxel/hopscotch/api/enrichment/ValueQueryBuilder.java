package paxel.hopscotch.api.enrichment;

import java.util.stream.Stream;

/**
 * The Query-builder for querying or filtering for values.
 */

public interface ValueQueryBuilder {


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
