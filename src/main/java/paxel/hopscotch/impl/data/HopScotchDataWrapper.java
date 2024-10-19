package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.QueryBuilder;
import paxel.hopscotch.api.enrichment.Stage;

public class HopScotchDataWrapper<D> implements HopScotchData<D> {

    private final HopScotchDataInternal<D> delegate;
    private final boolean immutable;
    private final Stage stage;
    private final Creator creator;

    public HopScotchDataWrapper(HopScotchDataInternal<D> delegate, Stage stage, Creator creator) {
        this.delegate = delegate;
        this.stage = stage;
        this.creator = creator;
        immutable = false;
    }

    public HopScotchDataWrapper(HopScotchDataInternal<D> delegate, boolean immutable, Stage stage, Creator creator) {
        this.delegate = delegate;
        this.immutable = immutable;
        this.stage = stage;
        this.creator = creator;
    }

    @Override
    public D getData() {
        return null;
    }

    @Override
    public KeyBuilder startEnrichment() {
        ensureMutable();
        return new KeyBuilderImpl(delegate::add,stage,creator);
    }

    private void ensureMutable() {
        if (immutable) {
            throw new UnsupportedOperationException("Enrichment is not allowed here");
        }
    }

    @Override
    public QueryBuilder startEnrichmentQuery() {
        return null;
    }
}
