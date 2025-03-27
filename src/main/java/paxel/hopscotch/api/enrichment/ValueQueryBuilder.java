package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.api.enrichment.values.DoubleValueProvider;
import paxel.hopscotch.api.enrichment.values.IntValueProvider;
import paxel.hopscotch.api.enrichment.values.LongValueProvider;

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
     * Concentrate on Int values
     *
     * @return a IntValueProvider
     */
    IntValueProvider asInt();

    /**
     * Concentrate on Long values
     *
     * @return a LongValueProvider
     */
    LongValueProvider asLong();

    /**
     * Concentrate on Double values
     *
     * @return a DoubleValueProvider
     */
    DoubleValueProvider asDouble();

    /**
     * Get the enrichments as a Stream.
     *
     * @return a stream of enrichments
     */
    Stream<Enrichment> stream();
}
