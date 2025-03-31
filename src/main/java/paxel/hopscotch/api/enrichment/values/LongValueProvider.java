package paxel.hopscotch.api.enrichment.values;

import java.util.stream.LongStream;

/**
 * Provides long values
 */
public interface LongValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    LongStream stream();
}
