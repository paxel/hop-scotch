package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.HopScotchData;

public class HopScotchDataImpl<D> {
    private final D data;

    public HopScotchDataImpl(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }

    public HopScotchData<Object> copy() {
        return null;
    }
}
