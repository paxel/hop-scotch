package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Key;

import java.time.Instant;

public interface Enrichment {
    Key key();

    Value value();

    Instant creation();

    Creator creator();
}
