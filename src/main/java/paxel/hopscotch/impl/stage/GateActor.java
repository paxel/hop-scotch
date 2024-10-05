package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Gate;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class GateActor<D> implements LintStoneActor {
    private final Gate<D> gate;
    private final String nextStageName;
    private final Config config;

    public GateActor(Gate<D> gate, String nextStageName, Config config) {
        this.gate = gate;
        this.nextStageName = nextStageName;
        this.config = config;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchData<D> hopScotchData, LintStoneMessageEventContext mec) {
        try {
            if (!gate.canPass(hopScotchData)) {
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "dropped"));
                mec.getActor(nextStageName).tell(new StageActor.Drop(hopScotchData));
            } else {
                mec.getActor(nextStageName).tell(new StageActor.Fragment(hopScotchData));
            }
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            mec.getActor(nextStageName).tell(new StageActor.Fragment(hopScotchData));
        }
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
