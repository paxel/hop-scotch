package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.HopId;
import paxel.lintstone.api.LintStoneActorAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Creates and caches Actors for Hops
 */
public class HopMap {

    private final Map<Integer, Map<HopId, LintStoneActorAccessor>> actorMap = new HashMap<>();

    /**
     * Get or create the Actor for the given Judge and Hop
     *
     * @param judgeNumber The number of the judge
     * @param id          The Hop ID
     * @param supplier    The supplier for the Actor
     * @return the actor
     */
    public LintStoneActorAccessor computeIfAbsent(int judgeNumber, HopId id, Supplier<LintStoneActorAccessor> supplier) {
        return actorMap.computeIfAbsent(judgeNumber, j -> new HashMap<>())
                .computeIfAbsent(id, h -> supplier.get());
    }
}
