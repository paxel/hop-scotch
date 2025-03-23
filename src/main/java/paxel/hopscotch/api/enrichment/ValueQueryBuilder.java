package paxel.hopscotch.api.enrichment;

import java.util.Collection;
import java.util.Optional;

/**
 * The Query-builder for querying or filtering for values.
 */

public interface ValueQueryBuilder {

    /**
     * Create a new ValueQueryBuilder that has only the entities with the given type
     *
     * @param type The Value type
     * @return the ValueQueryBuilder for the filtered enrichments
     */
    ValueQueryBuilder matchType(Value.Type type);

    /**
     * Get any value if available.
     *
     * @return optional value
     */
    Optional<Integer> anyInteger();

    /**
     * Get all values
     *
     * @return all matching values
     */
    Collection<Integer> allInteger();
}
