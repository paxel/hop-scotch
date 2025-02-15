package paxel.hopscotch.api.enrichment;

import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * The Query-builder for querying or filtering for keys.
 */
public interface KeyQueryBuilder {


    /**
     * Create a new KeyQueryBuilder that has only the entities with the given key
     *
     * @param key The key value
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder matchExact(String key);

    /**
     * Create a new KeyQueryBuilder that has only the entities with the given key
     *
     * @param key The key value
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder matchExact(Key key);

    /**
     * Create a new KeyQueryBuilder that has only the entities with the given path
     *
     * @param path The path value
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder matchExact(String... path);

    /**
     * Create a new KeyQueryBuilder that has only the entities with the given path
     *
     * @param path The path value
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder matchExact(Collection<String> path);

    /**
     * Create a new KeyQueryBuilder that has as Source all enrichments with keys that contain every of the given sub paths.
     * <br>
     * <br>
     * given subPaths foo,bar<br>
     * path: alpha.bar.foo.beta - matches<br>
     * path: alpha.bard.foo.beta - doesn't match (bard != bar)<br>
     *
     * @param subPaths The required sub paths
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder containsAll(String... subPaths);

    /**
     * Create a new KeyQueryBuilder that has as Source all enrichments with keys that contain any of the given sub paths.
     * <br>
     * <br>
     * given subPaths foo,bar<br>
     * path: alpha.bar.beta - matches bar<br>
     * path: alpha.bard.beta - doesn't match bar nor foo (bard != bar)<br>
     *
     * @param subPaths The required sub paths
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder containsAny(String... subPaths);

    /**
     * Create a new KeyQueryBuilder that has as Source all enrichments with keys that contain all the given sub paths in the given order without a gap.
     * <br>
     * <br>
     * given subPaths foo,bar<br>
     * path: alpha.foo.bar.beta - matches<br>
     * path: alpha.foo.and.bar.beta - doesn't match (and is between foo and bar)<br>
     * path: alpha.foo.bard.beta - doesn't match (bard != bar)<br>
     *
     * @param subPaths The required sub paths
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder containsInOrder(String... subPaths);


    /**
     * Create a new KeyQueryBuilder that has as Source the result of the given regex match on keys.
     * <br>
     * <br>
     * given regex bar<br>
     * path: alpha.foo.bar.beta - matches<br>
     * path: alpha.foo.bard.beta - matches<br>
     *
     * @param regex The regex to filter on the key
     * @return the KeyQueryBuilder for the filtered enrichments
     */
    KeyQueryBuilder matchRegex(String regex);


    /**
     * Get the enrichments as a Stream. Can be used to access the remaining keys
     *
     * @return an unfiltered stream of enrichments
     */
    Stream<EnrichmentImpl> stream();

    /**
     * Get a QueryBuilder for the remaining enrichments.
     *
     * @return a new QueryBuilder that only works on the remaining Enrichments.
     */
    QueryBuilder query();
}
