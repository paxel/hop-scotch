package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.api.enrichment.ValueBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Implementation of the {@link ValueBuilder} interface
 */
public class ValueBuilderImpl implements ValueBuilder {
    private final KeyImpl key;
    private final Creator creator;
    private final Stage stage;
    private final Consumer<EnrichmentImpl> enrichmentConsumer;

    /**
     * Constructs an instance of a Builder
     *
     * @param key                The key
     * @param enrichmentConsumer The receiver of the value
     * @param stage              The stage
     * @param creator            The creator
     */
    public ValueBuilderImpl(KeyImpl key, Consumer<EnrichmentImpl> enrichmentConsumer, Stage stage, Creator creator) {
        this.key = key;
        this.creator = creator;
        this.stage = stage;
        this.enrichmentConsumer = enrichmentConsumer;
    }

    @Override
    public void add(String value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.StringValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(int value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.IntegerValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(long value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.LongValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(double value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.DoubleValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(float value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.FloatValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(short value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.ShortValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(BigDecimal value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BigDecimalValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(BigInteger value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BigIntegerValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public void add(boolean value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BooleanValue(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public <V> void add(V value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.ObjectValue<>(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public <V> void addList(Collection<V> value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.CollectionValue<>(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }

    @Override
    public <K, V> void addMap(Map<K, V> value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.MapValue<>(value), Instant.now(), UUID.randomUUID(), stage, creator));
    }
}
