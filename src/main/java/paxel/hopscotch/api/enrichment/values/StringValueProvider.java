package paxel.hopscotch.api.enrichment.values;

import java.util.stream.Stream;

/**
 * Provides long values
 */
public interface StringValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<String> stream();
}
