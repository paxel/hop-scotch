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

public class ValueBuilderImpl implements ValueBuilder {
    private final KeyImpl key;
    private final Creator creator;
    private final Stage stage;
    private final Consumer<EnrichmentImpl> enrichmentConsumer;

    public ValueBuilderImpl(KeyImpl key, Creator creator, Stage stage, Consumer<EnrichmentImpl> enrichmentConsumer) {
        this.key = key;
        this.creator = creator;
        this.stage = stage;
        this.enrichmentConsumer = enrichmentConsumer;
    }

    @Override
    public void add(String value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.StringValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(int value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.IntegerValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(long value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.LongValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(double value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.DoubleValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(float value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.FloatValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(short value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.ShortValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(BigDecimal value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BigDecimalValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(BigInteger value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BigIntegerValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public void add(boolean value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.BooleanValue(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public <V> void add(V value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.ObjectValue<>(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public <V> void addList(Collection<V> value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.CollectionValue<>(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }

    @Override
    public <K, V> void addMap(Map<K, V> value) {
        enrichmentConsumer.accept(new EnrichmentImpl(key, new Value.MapValue<>(value), Instant.now(), stage, creator, UUID.randomUUID()));
    }
}
