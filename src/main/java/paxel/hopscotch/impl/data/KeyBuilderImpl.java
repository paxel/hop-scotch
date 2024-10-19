package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.api.enrichment.ValueBuilder;

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
    public ValueBuilder key(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }

        return new ValueBuilderImpl(new KeyImpl(key), enrichmentConsumer, stage, creator);
    }
}
