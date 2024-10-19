package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Statistics;

import java.util.SortedMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the Statistics of the whole system
 */
public class MutableStatistic implements Statistics {


    static String toKey(String... path) {
        StringJoiner joiner = new StringJoiner(".");
        for (String s : path) {
            joiner.add(s.replace('.', '_').toLowerCase());
        }
        return joiner.toString();
    }

    private final SortedMap<String, AtomicLong> map = new TreeMap<>();

    long increment(long value, String... field) {
        return map.computeIfAbsent(toKey(field), k -> new AtomicLong()).addAndGet(value);
    }

    void set(long value, String... field) {
        map.computeIfAbsent(toKey(field), k -> new AtomicLong()).set(value);
    }

    void remove(String... field) {
        map.remove(toKey(field));
    }

    Statistics snapshot() {
        return new ImmutableStatistic(map);
    }
}
