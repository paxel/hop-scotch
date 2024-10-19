package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.Key;

import java.util.Arrays;
import java.util.Collection;

/**
 * Creates Keys from Strings
 */
public class KeyFactory {

    /**
     * Constructs Keys from a valid string
     *
     * @param key an absolut key
     * @return The key
     */
    public static Key forString(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        String[] split = key.split("\\.");
        if (split == null)
            throw new IllegalArgumentException("Invalid key '%s'".formatted(key));
        return new KeyImpl(Arrays.asList(split));
    }

    /**
     * Constructs Key from a path
     *
     * @param path the path
     * @return The key
     */
    public static Key path(String... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        for (String s : path) {
            if (s == null || s.isEmpty() || s.contains("."))
                throw new IllegalArgumentException("Invalid Path string: '%s'".formatted(s));
        }
        return new KeyImpl(Arrays.asList(path));
    }

    /**
     * Creates a key from a collection path
     *
     * @param path The path
     * @return The key
     */
    public static Key collection(Collection<String> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        for (String s : path) {
            if (s.contains("."))
                throw new IllegalArgumentException("Path strings cannot contain a dot char: '%s'".formatted(s));
        }
        return new KeyImpl(path);
    }
}
