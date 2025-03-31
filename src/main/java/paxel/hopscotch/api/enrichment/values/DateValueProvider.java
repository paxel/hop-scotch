package paxel.hopscotch.api.enrichment.values;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

/**
 * Provides Date values
 */
public interface DateValueProvider extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<ZonedDateTime> stream();
}
