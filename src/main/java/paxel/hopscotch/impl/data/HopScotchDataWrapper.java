package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.QueryBuilder;

public class HopScotchDataWrapper<D> implements HopScotchData<D> {

    private final HopScotchDataImpl<D> delegate;

    public HopScotchDataWrapper(HopScotchDataImpl<D> delegate) {
        this.delegate = delegate;
    }

    @Override
    public D getData() {
        return null;
    }

    @Override
    public KeyBuilder startEnrichment() {
        return null;
    }

    @Override
    public QueryBuilder startEnrichmentQuery() {
        return null;
    }
}
