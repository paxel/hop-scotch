package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.CollectionValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides Collection values as objects
 *
 * @param <T> The type of the objects
 */
public class CollectionValueProviderImpl<T> implements CollectionValueProvider<T> {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     */
    public CollectionValueProviderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }


    @Override
    public Stream<Collection<T>> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.COLLECTION)
                // We don't check types here
                .map(v -> ((Value.CollectionValue<T>) v).value());
    }

    @Override
    public Value.Type getType() {
        return Value.Type.COLLECTION;
    }
}
