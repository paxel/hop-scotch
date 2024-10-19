package paxel.hopscotch.impl.data;

import paxel.hopscotch.api.enrichment.Key;

/**
 * @param key The user defined key for enrichment
 */
public record KeyImpl(String key) implements Key {
}
