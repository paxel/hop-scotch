package paxel.hopscotch.api.enrichment.values;

import java.util.stream.IntStream;

/**
 * Provides int values
 */
public interface IntValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    IntStream stream();
}
