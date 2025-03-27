package paxel.hopscotch.api.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;

import java.util.stream.DoubleStream;

/**
 * Provides long values
 */
public interface DoubleValueProvider {


    /**
     * @return The Value type of this provider
     */
    default Value.Type get() {
        return Value.Type.DOUBLE;
    }


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    DoubleStream stream();
}
