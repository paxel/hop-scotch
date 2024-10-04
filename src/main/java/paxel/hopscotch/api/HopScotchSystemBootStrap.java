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
 * @param <M> The type of the meta data.
 * @param <D> The type of the data.
 */
public class HopScotchSystemBootStrap<M, D> {

    private final Map<Integer, List<Object>> factories = new TreeMap<>();
    private Config config = new Config();
    private Consumer<HopScotchData<D>> consumer = d -> {
    };

    public static HopScotchSystemBootStrap builder() {
        return new HopScotchSystemBootStrap();
    }

    private HopScotchSystemBootStrap() {
    }

    public HopScotchSystemBootStrap add(HopFactory<M, D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("HopFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    public HopScotchSystemBootStrap add(GateFactory<D> factory) {
        int stage = factory.getStage();
        if (stage < 0)
            throw new IllegalArgumentException("GateFactory must have a stage greater than 0");
        factories.computeIfAbsent(stage, k -> new ArrayList<>()).add(factory);
        return this;
    }

    public HopScotchSystem build() {
        if (factories.isEmpty())
            throw new IllegalStateException("Need at least one Hop or GateFactory");
        HopScotchSystemImpl hopScotchSystem = new HopScotchSystemImpl(config);
        hopScotchSystem.start(factories, consumer);
        return hopScotchSystem;
    }


}
