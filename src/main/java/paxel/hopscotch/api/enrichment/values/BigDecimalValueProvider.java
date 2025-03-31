package paxel.hopscotch.api.enrichment.values;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * Provides Date values
 */
public interface BigDecimalValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<BigDecimal> stream();
}
