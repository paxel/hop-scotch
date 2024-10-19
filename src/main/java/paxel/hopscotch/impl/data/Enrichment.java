package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Key;

import java.time.Instant;

/**
 * The definition of a user generated enrichment.
 */
public interface Enrichment {
    /**
     * @return the key of the enrichment. Can exist multiple times.
     */
    Key key();

    /**
     * The actual value of this enrichment.
     *
     * @return the Value
     */
    Value value();

    /**
     * The time when this was created.
     *
     * @return the instant when this enrichment was added.
     */
    Instant creation();

    /**
     * The Hop that created this enrichment.
     *
     * @return The creator.
     */
    Creator creator();
}
