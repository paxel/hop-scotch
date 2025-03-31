package paxel.hopscotch.api.enrichment.values;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Provides Map values
 *
 * @param <K> The key type of the map
 * @param <V> The value type of the map
 */
public interface MapValueProvider<K, V> extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<Map<K, V>> stream();
}
