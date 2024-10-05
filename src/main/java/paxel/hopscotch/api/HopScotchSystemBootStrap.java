package paxel.hopscotch.api;

import paxel.hopscotch.impl.HopScotchSystemImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Used to create a Hopscotch Pipeline.
 *
 * @param <D> The type of the data.
 */
public class HopScotchSystemBootStrap<D> {

    private final Map<Integer, List<Object>> factories = new TreeMap<>();
    private Consumer<HopScotchData<D>> consumer = d -> {
    };
    private int backPressure;

    public static HopScotchSystemBootStrap builder() {
        return new HopScotchSystemBootStrap();
    }

    private HopScotchSystemBootStrap() {
    }

    public HopScotchSystemBootStrap<D> add(JudgeFactory<D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("HopFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    public HopScotchSystemBootStrap<D> add(GateFactory<D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("GateFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    public HopScotchSystemBootStrap<D> setBackPressure(int backPressure) {
        this.backPressure = backPressure;
        return this;
    }

    public HopScotchSystem build() {
        if (factories.isEmpty())
            throw new IllegalStateException("Need at least one Hop or GateFactory");
        Config config = new Config(backPressure);

        HopScotchSystemImpl hopScotchSystem = new HopScotchSystemImpl(config);
        hopScotchSystem.start(factories, consumer);
        return hopScotchSystem;
    }


}
