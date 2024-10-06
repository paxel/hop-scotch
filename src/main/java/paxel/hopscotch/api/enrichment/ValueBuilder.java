package paxel.hopscotch.api.enrichment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.SequencedCollection;

/**
 * The ValueBuilder actually adds a value to the Enrichment.
 * Each call adds another record with the same key.
 * The Generic values are not stored typesafe as the Generics are erased at run time.
 * The user needs to make sure that the Objects are handled safely.
 * <p>
 * The other methods are here to make sure the enrichment data is stored typesafe for the queries.
 */
public interface ValueBuilder {

    /**
     * Add a String with the given key to the enrichment.
     *
     * @param value the value of the enrichment. Can be null
     */
    void add(String value);

    /**
     * Add a integer with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(int value);

    /**
     * Add a long with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(long value);

    /**
     * Add a double with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(double value);

    /**
     * Add a float with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(float value);

    /**
     * Add a short with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(short value);

    /**
     * Add a {@link BigDecimal} with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(BigDecimal value);

    /**
     * Add a {@link BigInteger} with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(BigInteger value);

    /**
     * Add a boolean with the given key to the enrichment.
     *
     * @param value the value of the enrichment.
     */
    void add(boolean value);

    /**
     * Add a Object with the given key to the enrichment.
     *
     * @param value the value of the enrichment. Can be null
     */
    <V> void add(V value);

    /**
     * Add a Collection with the given key to the enrichment.
     * The Collection will be copied and become immutable to prevent concurrent modification.
     * The order in the collection should stay the same, but that is not guaranteed.
     *
     * @param value the value of the enrichment. Can be null or empty
     */
    <V> void addList(Collection<V> value);

    /**
     * Add a Map with the given key to the enrichment.
     * The Map will be copied and become immutable to prevent concurrent modification.
     * The order in the map should stay the same, but that is not guaranteed.
     *
     * @param value the value of the enrichment. Can be null or empty
     */
    <K, V> void addMap(Map<K, V> value);
}
