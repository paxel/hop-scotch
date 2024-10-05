package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Hop;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class HopActor<D> implements LintStoneActor {
    private final Hop<D> hop;
    private final String nextStageName;

    public HopActor(Hop<D> hop, String nextStageName, Config config) {
        this.hop = hop;
        this.nextStageName = nextStageName;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchData<D> hopScotchData, LintStoneMessageEventContext mec) {
        int originalHash = hopScotchData.getData().hashCode();
        try {
            HopScotchData<D> result = hop.process(hopScotchData);
            if (result == null) {
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "invalid_result", "null"));
                // we forward the input instead the output
                mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData));
            } else {
                int postProcessHash = hopScotchData.getData().hashCode();
                if (originalHash != postProcessHash) {
                    // We can't prevent a Hop modifying the original data, because we can't enforce immutable data.
                    // But we will add an annoying statistic because this introduces concurrency in the actor framework
                    mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "invalid_result", "modified_data"));
                }
                mec.getActor(nextStageName).tell(new StageActor.Fragment<>(result));
            }
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            // we forward the input instead the output
            mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData));
        }
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
