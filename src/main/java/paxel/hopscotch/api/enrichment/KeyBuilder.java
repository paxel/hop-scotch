package paxel.hopscotch.api.enrichment;


import java.util.SequencedCollection;
import java.util.StringJoiner;

/**
 * The Key builder helps to create a structured key for the enrichment
 */
public interface KeyBuilder {

    /**
     * This method expects a string preformatted as a key with a dot structure.
     * The formatting expects to have all '_' escaped as "__" and all '.' that are not a delimiter escaped as "_"
     * <br/>
     * e.g. germany.saxony.dresden or car.ford.kuga
     * <br/>
     * If you don't want to handle this use {@link #path(String...)} with a single string
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    ValueBuilder key(String key);

    /**
     * This method expects an array of unescaped strings, that will be escaped and joined by a '.' to form the key.
     * <br/>
     * e.g. ["germany","saxony","dresden"] or ["car","ford","kuga"]
     * <br/>
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    default ValueBuilder path(String... key) {
        if (key == null || key.length == 0) throw new IllegalArgumentException("key cannot be null or empty");
        StringJoiner joiner = new StringJoiner(".");
        for (String s : key) {
            // Replaces '.' with '\.'
            // So "This_is.wrong" becomes "This_is\.wrong"
            // And "This_is\.wrong" becomes "This_is\\.wrong"

            joiner.add(s.replace(".", "\\."));
        }
        return key(joiner.toString());
    }

    /**
     * This method expects a {@link SequencedCollection} of unescaped strings, that will be escaped and joined by a '.' to form the key.
     * <br/>
     * e.g. ("germany","saxony","dresden") or ("car","ford","kuga")
     * <br/>
     *
     * @param key The full key
     * @return The ValueBuilder
     */
    default ValueBuilder collection(SequencedCollection<String> key) {
        if (key == null || key.isEmpty()) throw new IllegalArgumentException("key cannot be null or empty");
        StringJoiner joiner = new StringJoiner(".");
        for (String s : key) {
            // Replaces '.' with '\.'
            // So "This_is.wrong" becomes "This_is\.wrong"
            // And "This_is\.wrong" becomes "This_is\\.wrong"

            joiner.add(s.replace(".", "\\."));
        }
        return key(joiner.toString());
    }
}
