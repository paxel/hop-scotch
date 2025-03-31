package paxel.hopscotch.api.enrichment.values;

import java.util.stream.Stream;

/**
 * Provides Boolean values
 */
public interface BooleanValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<Boolean> stream();
}
