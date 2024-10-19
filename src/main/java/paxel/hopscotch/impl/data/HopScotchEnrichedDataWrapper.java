package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.QueryBuilder;
import paxel.hopscotch.api.enrichment.Stage;

/**
 * The wrapper for the data and enrichments that is given to Gates, Judges and Hops.
 *
 * @param <D> The data type
 */
public class HopScotchEnrichedDataWrapper<D> implements HopScotchData<D> {

    private final HopScotchEnrichedData<D> hopScotchEnrichedData;
    private final boolean immutable;
    private final Stage stage;
    private final Creator creator;

    /**
     * Constructs an instance from existing data.
     *
     * @param hopScotchEnrichedData The enriched data
     * @param stage                 The stage
     * @param creator               The creator
     */
    public HopScotchEnrichedDataWrapper(HopScotchEnrichedData<D> hopScotchEnrichedData, Stage stage, Creator creator) {
        this.hopScotchEnrichedData = hopScotchEnrichedData;
        this.stage = stage;
        this.creator = creator;
        immutable = false;
    }

    /**
     * Constructs an instance from existing data.
     *
     * @param hopScotchEnrichedData The enriched data
     * @param immutable             If adding more enriched data is allowed
     * @param stage                 The stage
     * @param creator               The creator
     */
    public HopScotchEnrichedDataWrapper(HopScotchEnrichedData<D> hopScotchEnrichedData, boolean immutable, Stage stage, Creator creator) {
        this.hopScotchEnrichedData = hopScotchEnrichedData;
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
        return new KeyBuilderImpl(hopScotchEnrichedData::add, stage, creator);
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
