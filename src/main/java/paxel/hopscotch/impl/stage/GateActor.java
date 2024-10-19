package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Gate;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.impl.data.HopScotchDataInternal;
import paxel.hopscotch.impl.data.HopScotchDataWrapper;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class GateActor<D> implements LintStoneActor {
    private final Gate<D> gate;
    private final String nextStageName;
    private final Config config;
    private final Creator creator;
    private final Stage stage;
    public GateActor(Gate<D> gate, String nextStageName, Config config, Stage stage, Creator creator) {
        this.gate = gate;
        this.nextStageName = nextStageName;
        this.config = config;
        this.creator = creator;
        this.stage = stage;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchDataInternal.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchDataInternal<D> hopScotchData, LintStoneMessageEventContext mec) {
        HopScotchDataWrapper hopScotchDataWrapper = new HopScotchDataWrapper(hopScotchData, stage, creator);

        try {
            if (!gate.canPass(hopScotchDataWrapper)) {
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "dropped"));
                mec.getActor(nextStageName).tell(new StageActor.Drop(hopScotchData.copy()));
            } else {
                mec.getActor(nextStageName).tell(new StageActor.Fragment(hopScotchData.copy()));
            }
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            mec.getActor(nextStageName).tell(new StageActor.Fragment(hopScotchData.copy()));
        }
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
