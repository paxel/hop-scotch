package paxel.hopscotch.api.enrichment.values;

import java.util.stream.Stream;

/**
 * Provides short values
 */
public interface ShortValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<Short> stream();
}
