package paxel.hopscotch.impl.stage;

import paxel.lintstone.api.LintStoneActorAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GateMap {

    private final Map<Integer, LintStoneActorAccessor> actorMap = new HashMap<>();

    public LintStoneActorAccessor computeIfAbsent(int gateNumber, Supplier<LintStoneActorAccessor> supplier) {
        return actorMap.computeIfAbsent(gateNumber, k -> supplier.get());
    }
}
