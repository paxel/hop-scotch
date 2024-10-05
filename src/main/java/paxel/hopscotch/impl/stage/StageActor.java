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
    private Gates<D> bill = new Gates<>();
    private String nextStageName;
    private HopMap<D> hopMap = new HopMap<>();

    public StageActor(Integer stage, List<Object> factories, Config config) {
        this.stage = stage;
        this.config = config;
        for (Object factory : factories) {
            switch (factory) {
                case JudgeFactory<?> hf -> {
                    judges.add(((JudgeFactory<D>) hf).createJudge());
                }
                case GateFactory<?> gf -> {
                    bill.add((GateFactory<D>) gf);
                }
                default -> {
                    // TODO: error handler
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
        int i = 0;
        int sent = 0;
        for (Judge<D> judge : judges) {
            i++;
            Judgment<D> judgement = judge.judge(aggregation);
            if (judgement.isAccepted()) {
                LintStoneActorAccessor actor = getLintStoneActorAccessor(mec, i, judgement.getId(), judgement);
                // forward message to hop
                actor.tell(aggregation);
                sent++;
            }
        }
        return sent;
    }

    private LintStoneActorAccessor getLintStoneActorAccessor(LintStoneMessageEventContext mec, int judgeNumber, HopId id, Judgment<D> judgement) {
        LintStoneActorAccessor actor = hopMap.computeIfAbsent(judgeNumber, id, () -> {
            Hop<D> hop = judgement.createHop();
            String name = "Hop." + stage + "." + judgeNumber + "." + hop.getClass().getSimpleName() + "." + id.id();
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "hop", "created"));
            return mec.registerActor(name, () -> new HopActor(hop), ActorSettings.DEFAULT);
        });
        return actor;
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
