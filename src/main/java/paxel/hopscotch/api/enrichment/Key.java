package paxel.hopscotch.api.enrichment;

import java.util.Collection;

/**
 * Defines the Key of an Enrichment
 */
public interface Key {
    /**
     * @return The key as collection of sub paths
     */
    Collection<String> asCollection();
}
