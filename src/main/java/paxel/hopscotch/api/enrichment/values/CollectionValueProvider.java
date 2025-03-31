package paxel.hopscotch.api.enrichment.values;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Provides Collection values
 *
 * @param <V> The type of the data in the collection
 */
public interface CollectionValueProvider<V> extends ValueProvider {


    /**
     * All existing values as Stream
     *
     * @return Raw Stream
     */
    Stream<Collection<V>> stream();
}
