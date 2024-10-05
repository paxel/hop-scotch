package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Statistics;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ImmutableStatistic implements Statistics {
    private final Map<String, Long> map;

    public ImmutableStatistic(Map<String, AtomicLong> map) {
        this.map = map.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
