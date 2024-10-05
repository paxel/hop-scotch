package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.*;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.ActorSettings;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneActorAccessor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.ArrayList;
import java.util.List;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class StageActor<D> implements LintStoneActor {
    private final Integer stage;
    private final Config config;

    private List<Judge<D>> judges = new ArrayList<>();
    private List<GateFactory<D>> gateFactories = new ArrayList<>();
    private String nextStageName;
    private HopMap<D> hopMap = new HopMap<>();
    private GateMap<D> gateMap = new GateMap<>();

    public StageActor(Integer stage, List<Object> factories, Config config) {
        this.stage = stage;
        this.config = config;
        for (Object factory : factories) {
            switch (factory) {
                case JudgeFactory<?> hf -> {
                    judges.add(((JudgeFactory<D>) hf).createJudge());
                }
                case GateFactory<?> gf -> {
                    gateFactories.add(((GateFactory<D>) gf));
                }
                case null -> {
                    throw new IllegalArgumentException("Factory must not be null");
                }
                default -> {
                    throw new IllegalArgumentException("Unknown factory type: " + factory.getClass());
                }
            }
        }
    }


    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(String.class, (nextStageName, b) -> this.nextStageName = nextStageName)
                .inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchData<D> fragment, LintStoneMessageEventContext mec) {

        HopScotchData<D> aggregation = aggregate(fragment);

        if (aggregation == null) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "fragment", StatisticsActor.PROCESSED));
            return;
        }
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "aggregation", StatisticsActor.PROCESSED));


        int sent = distributeData(mec, aggregation);
        // Notify the next stage about the number of splits to merge
        mec.getActor(nextStageName).tell(new StageActor.Split<>(aggregation, sent));
    }

    private int distributeData(LintStoneMessageEventContext mec, HopScotchData<D> aggregation) {
        int sent = 0;
        {
            int hopNumber = 0;
            for (Judge<D> judge : judges) {
                hopNumber++;
                Judgment<D> judgement = judge.judge(aggregation);
                if (judgement.isAccepted()) {
                    LintStoneActorAccessor actor = getHopActor(mec, hopNumber, judgement.getId(), judgement);
                    // forward message to hop
                    actor.tell(aggregation);
                    sent++;
                }
            }
        }
        {
            int gateNumber = 0;
            for (GateFactory<D> gate : gateFactories) {
                gateNumber++;
                LintStoneActorAccessor actor = getGateActor(mec, gateNumber, gate);
                // forward message to hop
                actor.tell(aggregation);
                sent++;
            }
        }

        return sent;
    }

    private LintStoneActorAccessor getGateActor(LintStoneMessageEventContext mec, int gateNumber, GateFactory<D> gateFactory) {
        return gateMap.computeIfAbsent(gateNumber, () -> {
            Gate<D> gate = gateFactory.createGate();
            String name = "Gate." + stage + "." + gateNumber + "." + gate.getClass().getSimpleName();
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "gate", "created"));
            return mec.registerActor(name, () -> new GateActor(gate, nextStageName, config), ActorSettings.DEFAULT);
        });
    }

    private LintStoneActorAccessor getHopActor(LintStoneMessageEventContext mec, int hopNumber, HopId id, Judgment<D> judgement) {
        return hopMap.computeIfAbsent(hopNumber, id, () -> {
            Hop<D> hop = judgement.createHop();
            String name = "Hop." + stage + "." + hopNumber + "." + hop.getClass().getSimpleName() + "." + id.id();
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "hop", "created"));
            return mec.registerActor(name, () -> new HopActor(hop, nextStageName, config), ActorSettings.DEFAULT);
        });
    }

    private HopScotchData<D> aggregate(HopScotchData<D> fragment) {
        // TODO: aggregate fragments
        return null;
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }

    public record Split<D>(HopScotchData<D> hopScotchData, int sent) {
    }
}
