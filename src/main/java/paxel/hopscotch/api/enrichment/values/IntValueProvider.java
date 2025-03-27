package paxel.hopscotch.api.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;

import java.util.stream.IntStream;

/**
 * Provides int values
 */
public interface IntValueProvider {


    /**
     * @return The Value type of this provider
     */
    default Value.Type get() {
        return Value.Type.INTEGER;
    }


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    IntStream stream();
}
