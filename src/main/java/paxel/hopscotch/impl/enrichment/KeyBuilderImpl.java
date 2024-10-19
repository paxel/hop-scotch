package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.*;

import java.util.function.Consumer;

/**
 * Implementation of the {@link KeyBuilder} interface
 */
public class KeyBuilderImpl implements KeyBuilder {

    private final Creator creator;
    private final Stage stage;
    private final Consumer<EnrichmentImpl> enrichmentConsumer;

    /**
     * Constructs an instance with the given meta-data.
     *
     * @param enrichmentConsumer The receiver of the final Enrichment.
     * @param stage              The stage
     * @param creator            The creator
     */
    public KeyBuilderImpl(Consumer<EnrichmentImpl> enrichmentConsumer, Stage stage, Creator creator) {
        this.creator = creator;
        this.stage = stage;
        this.enrichmentConsumer = enrichmentConsumer;
    }


    @Override
    public ValueBuilder forKey(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (key instanceof KeyImpl keyImpl) {
            return new ValueBuilderImpl(keyImpl, enrichmentConsumer, stage, creator);
        }
        throw new IllegalArgumentException("Unknown key type " + key.getClass());
    }
}
