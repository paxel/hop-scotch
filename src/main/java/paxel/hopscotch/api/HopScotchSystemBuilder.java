package paxel.hopscotch.api;

import paxel.hopscotch.impl.HopScotchSystemImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Used to create a Hopscotch Pipeline.
 *
 * @param <D> The type of the data.
 */
public class HopScotchSystemBuilder<D> {

    private final Map<Integer, List<Object>> factories = new TreeMap<>();
    private Consumer<HopScotchData<D>> consumer = d -> {
    };
    private int backPressure;

    /**
     * Create an instance to build a service.
     *
     * @param <T> The data type
     * @return the bootstrap.
     */
    public static <T> HopScotchSystemBuilder<T> builder() {
        return new HopScotchSystemBuilder<>();
    }

    private HopScotchSystemBuilder() {
    }

    /**
     * Add a {@link JudgeFactory} to the System.
     * The Factory is responsible for creating a {@link Judge}, which is responsible for creating {@link Hop}s.
     *
     * @param factory The judge factory
     * @return the builder itself.
     */
    public HopScotchSystemBuilder<D> add(JudgeFactory<D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("HopFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    /**
     * Add a {@link GateFactory to the System}.
     * The GateFactory is responsible to filter out data at a specific stage.
     *
     * @param factory The gate factory
     * @return the builder itself
     */
    public HopScotchSystemBuilder<D> add(GateFactory<D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("GateFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    /**
     * Define the amount of backpressure in the system.
     * Default is none.
     * The backpressure is applied to each stage
     * and makes sure that not more than the given amount is enqueued to the next stage.
     *
     * @param backPressure number of messages per stage that are enqueued at max
     * @return the builder itself
     */
    public HopScotchSystemBuilder<D> setBackPressure(int backPressure) {
        this.backPressure = backPressure;
        return this;
    }

    /**
     * Sets a consumer that receives all finished data
     *
     * @param consumer The receiver of all the finished data
     * @return the builder itself
     */
    public HopScotchSystemBuilder<D> setConsumer(Consumer<HopScotchData<D>> consumer) {
        this.consumer = requireNonNull(consumer);
        return this;
    }


    /**
     * Starts a System with the given parameters.
     * Calling it multiple times will result in multiple running Systems.
     *
     * @return the running System.
     */
    public HopScotchSystem<D> build() {
        if (factories.isEmpty())
            throw new IllegalStateException("Need at least one Hop or GateFactory");
        Config config = new Config(backPressure);

        HopScotchSystemImpl<D> hopScotchSystem = new HopScotchSystemImpl<>(config);
        hopScotchSystem.start(factories, consumer);
        return hopScotchSystem;
    }
}
