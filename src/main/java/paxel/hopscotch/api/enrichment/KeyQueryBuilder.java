package paxel.hopscotch.api.enrichment;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * The Query-builder for querying or filtering for keys.
 */
public interface KeyQueryBuilder {

    /**
     * Retrieves all currently added keys.
     *
     * @return a Set of keys.
     */
    Set<Key> all();

    /**
     * Retrieve the key that matches exact or empty if not available.
     *
     * @param key The key value
     * @return The key or empty
     */
    Optional<Key> exact(String key);


    /**
     * Retrieve the key that matches exact or empty if not available.
     *
     * @param path The path value
     * @return The key or empty
     */
    Optional<Key> exact(String... path);

    /**
     * Retrieve the key that matches exact or empty if not available.
     *
     * @param path The path value
     * @return The key or empty
     */
    Optional<Key> exact(Collection<String> path);

    /**
     * Retrieve the keys that match the given regex
     *
     * @param regex The path value
     * @return The keys or empty set
     */
    Set<Key> regex(String regex);

}
