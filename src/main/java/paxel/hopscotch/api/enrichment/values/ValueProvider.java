package paxel.hopscotch.api.enrichment.values;

import paxel.hopscotch.api.enrichment.Value;

/**
 * Defines a type safe value provider
 */
public interface ValueProvider {
    /**
     * Retrieve the value type
     *
     * @return The Value type of this provider
     */
    Value.Type getType();
}
