package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.impl.data.HopScotchDataInternal;

import java.util.Optional;

public class DataAggregator<D> {

    public Optional<HopScotchDataInternal<D>> update(HopScotchDataInternal<D> hopScotchData, int sent) {
        return Optional.empty();
    }

    public Optional<HopScotchDataInternal<D>> add(HopScotchDataInternal<D> fragment) {
        return Optional.empty();
    }

    public void drop(HopScotchDataInternal<D> hopScotchData) {

    }
}
