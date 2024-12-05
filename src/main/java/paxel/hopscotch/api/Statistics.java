package paxel.hopscotch.api;


import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Used to provide information about the progress in the pipeline
 */
public interface Statistics {

    /**
     * Retrieve all the keys.
     *
     * @return All existing keys
     */
    Set<String> getKeys();

    /**
     * Retrieve the number of statistics
     *
     * @return The number of statistics available
     */
    default int size() {
        return getKeys().size();
    }

    /**
     * Retrieve the number of statistics
     *
     * @return The number of statistics available
     */
    default boolean isEmpty() {
        return getKeys().isEmpty();
    }

    /**
     * Retrieve the value of the given key.
     *
     * @param key The key.
     * @return The value or null if key is unknown.
     */
    Long get(String key);


    /**
     * Processes all Statistic values as Key value tuples.
     *
     * @param consumer Consumer for key value Tuples
     */
    default void forEach(Consumer<NamedValue> consumer) {
        stream().forEach(consumer);
    }

    /**
     * Create a Stream of all Statistics as NamedValues.
     *
     * @return a Stream.
     */
    Stream<NamedValue> stream();

    /**
     * Contains a Statistic tuple.
     *
     * @param key   The Statistic key or path
     * @param value The current value
     */
    record NamedValue(String key, long value) {
    }
}
