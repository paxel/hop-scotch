package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Statistics;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The current statistics as a snapshot
 */
public class ImmutableStatistic implements Statistics {
    private final Map<String, Long> map;


    /**
     * Constructs an instance with a copy of the given map.
     *
     * @param map the map containing the statistics
     */
    public ImmutableStatistic(Map<String, AtomicLong> map) {
        this.map = map.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    @Override
    public Set<String> getKeys() {
        return map.keySet();
    }

    @Override
    public Long get(String key) {
        return map.get(key);
    }

    @Override
    public Stream<NamedValue> stream() {
        return map.entrySet().stream().map(e -> new NamedValue(e.getKey(), e.getValue()));
    }
}
