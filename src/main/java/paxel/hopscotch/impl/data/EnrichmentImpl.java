package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Key;
import paxel.hopscotch.api.enrichment.Stage;

import java.time.Instant;
import java.util.UUID;

public record EnrichmentImpl(Key key, Value value, Instant creation, UUID uuid, Stage stage, Creator creator
) implements Enrichment {
}
