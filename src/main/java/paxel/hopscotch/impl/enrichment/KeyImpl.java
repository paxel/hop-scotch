package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.Key;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param key The user defined key for enrichment
 */
public record KeyImpl(Collection<String> key) implements Key {

    public String toString() {
        return key.stream().collect(Collectors.joining("."));
    }
}
