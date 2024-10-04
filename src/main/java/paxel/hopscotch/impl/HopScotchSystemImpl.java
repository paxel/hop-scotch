package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.HopScotchSystem;
import paxel.hopscotch.api.Statistics;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HopScotchSystemImpl<D> implements HopScotchSystem<D> {

    private final Config config;

    public HopScotchSystemImpl(Config config) {
        this.config = config;
    }

    public void start(Map<Integer, List<Object>> factories, Consumer<HopScotchData<D>> consumer) {
        // TODO: create actor System and start it
    }

    @Override
    public void add(D data) {
        // TODO: signal data to first stage
    }

    @Override
    public Statistics getStatistics() {
        // TODO: invent Statistics Actor / System
        return null;
    }

    @Override
    public boolean awaitFinish() {
        // TODO: await finish
        return false;
    }

    @Override
    public boolean awaitFinish(Duration timeout) {
        // TODO: await finish
        return false;
    }
}
