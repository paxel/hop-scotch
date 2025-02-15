package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.Key;

import java.util.Collection;
import java.util.List;

/**
 * @param key The user defined key for enrichment
 */
public record KeyImpl(Collection<String> key) implements Key {

    public String toString() {
        return String.join(".", key);
    }

    @Override
    public Collection<String> asCollection() {
        return List.copyOf(key);
    }
}
