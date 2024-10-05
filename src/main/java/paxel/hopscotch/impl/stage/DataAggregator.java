package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.HopScotchData;

import java.util.Optional;

public class DataAggregator<D> {

    public Optional<HopScotchData<D>> update(HopScotchData hopScotchData, int sent) {
        return Optional.empty();
    }

    public Optional<HopScotchData<D>> add(HopScotchData<D> fragment) {
        return Optional.empty();
    }

    public void drop(HopScotchData hopScotchData) {

    }
}
