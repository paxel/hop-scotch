package paxel.hopscotch.impl.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.values.ObjectValueProvider;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Provides Float values as objects
 *
 * @param <T> The type of the object
 */
public class ObjectValueProviderImpl<T> implements ObjectValueProvider<T> {
    private final List<EnrichmentImpl> enrichments;
    private final Class<T> type;

    /**
     * Constructs an instance
     *
     * @param enrichments The remaining enrichments
     * @param type        the type of the object
     */
    public ObjectValueProviderImpl(List<EnrichmentImpl> enrichments, Class<T> type) {
        this.enrichments = enrichments;
        this.type = type;
    }


    @Override
    public Stream<T> stream() {
        return enrichments.stream()
                .map(EnrichmentImpl::value)
                .filter(v -> v.getType() == Value.Type.OBJECT)
                .map(value -> ((Value.ObjectValue<T>) value))
                .filter(v -> Objects.nonNull(v.value()))
                .filter(v -> type.isAssignableFrom(v.value().getClass()))
                .map(Value.ObjectValue::value);
    }

    @Override
    public Value.Type getType() {
        return Value.Type.OBJECT;
    }
}
