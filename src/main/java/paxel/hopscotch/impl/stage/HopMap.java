package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.HopId;
import paxel.lintstone.api.LintStoneActorAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HopMap<D> {

    private final Map<Integer, Map<HopId, LintStoneActorAccessor>> actorMap = new HashMap<>();

    public LintStoneActorAccessor computeIfAbsent(int judgeNumber, HopId id, Supplier<LintStoneActorAccessor> supplier) {
        return actorMap.computeIfAbsent(judgeNumber, j -> new HashMap<>())
                .computeIfAbsent(id, h -> supplier.get());
    }
}
