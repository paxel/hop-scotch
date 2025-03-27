package paxel.hopscotch.api.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;

import java.util.stream.LongStream;

/**
 * Provides long values
 */
public interface LongValueProvider {


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
    LongStream stream();
}
