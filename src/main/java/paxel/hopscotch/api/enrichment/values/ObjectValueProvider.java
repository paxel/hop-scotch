package paxel.hopscotch.api.enrichment.values;

import java.util.stream.Stream;

/**
 * Provides Object values
 *
 * @param <T> the type of the provided objects
 */
public interface ObjectValueProvider<T> extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<T> stream();
}
