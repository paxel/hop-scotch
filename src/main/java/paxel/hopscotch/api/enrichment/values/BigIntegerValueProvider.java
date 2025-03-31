package paxel.hopscotch.api.enrichment.values;

import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Provides Date values
 */
public interface BigIntegerValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<BigInteger> stream();
}
