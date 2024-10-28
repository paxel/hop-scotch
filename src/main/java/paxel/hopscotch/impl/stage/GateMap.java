package paxel.hopscotch.impl.stage;

import paxel.lintstone.api.LintStoneActorAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Creates and caches Actors for Gates
 */
public class GateMap {

    private final Map<Integer, LintStoneActorAccessor> actorMap = new HashMap<>();

    /**
     * A cache for Gate Actors
     *
     * @param gateNumber The number of the gate
     * @param supplier   The Actor Supplier
     * @return the Actor
     */
    public LintStoneActorAccessor computeIfAbsent(int gateNumber, Supplier<LintStoneActorAccessor> supplier) {
        return actorMap.computeIfAbsent(gateNumber, k -> supplier.get());
    }

    /**
     * @return The number of created GateActors
     */
    public int size() {
        return actorMap.size();
    }

    /**
     * Consume all generated actors
     *
     * @param consumer The consumer that processes every created Actor
     */
    public void forEachActor(Consumer<? super LintStoneActorAccessor> consumer) {
        actorMap.values().forEach(consumer);
    }
}
