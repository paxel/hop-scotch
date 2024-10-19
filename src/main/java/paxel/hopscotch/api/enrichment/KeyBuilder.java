package paxel.hopscotch.api.enrichment;


import paxel.hopscotch.impl.enrichment.KeyFactory;

import java.util.Collection;

/**
 * The Key builder helps to create a structured key for the enrichment
 */
public interface KeyBuilder {

    /**
     * This method expects a string preformatted as a key with a dot structure.
     * <br/>
     * e.g. germany.saxony.dresden or car.ford.kuga
     * <br/>
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    default ValueBuilder forString(String key) {
        return forKey(KeyFactory.forString(key));
    }

    /**
     * This method expects an array of strings.
     * <br/>
     * e.g. ["germany","saxony","dresden"] or ["car","ford","kuga"]
     * <br/>
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    default ValueBuilder path(String... key) {
        return forKey(KeyFactory.path(key));
    }

    /**
     * This method expects a {@link Collection} of strings.
     * <br/>
     * e.g. ("germany","saxony","dresden") or ("car","ford","kuga")
     * <br/>
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    default ValueBuilder collection(Collection<String> key) {
        return forKey(KeyFactory.collection(key));
    }

    /**
     * This method expects a {@link Key} that was provided by a {@link KeyQueryBuilder}.
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    ValueBuilder forKey(Key key);
}
