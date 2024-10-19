package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Key;
import paxel.hopscotch.api.enrichment.Stage;

import java.time.Instant;
import java.util.UUID;

/**
 * The Enrichment record.
 *
 * @param key      The user defined key
 * @param value    The user defined value
 * @param creation The creation time
 * @param uuid     The unique ID to merge fragments
 * @param stage    The stage that created the enrichment
 * @param creator  The Hop/Gate/Judge that created the enrichment.
 */
public record EnrichmentImpl(Key key, Value value, Instant creation, UUID uuid, Stage stage, Creator creator
) implements Enrichment {
}
