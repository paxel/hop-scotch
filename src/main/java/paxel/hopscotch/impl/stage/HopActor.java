package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Hop;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.impl.data.HopScotchDataInternal;
import paxel.hopscotch.impl.data.HopScotchDataWrapper;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class HopActor<D> implements LintStoneActor {
    private final Hop<D> hop;
    private final String nextStageName;
    private final Creator creator;
    private final Stage stage;

    public HopActor(Hop<D> hop, String nextStageName, Config config, Stage stage, Creator creator) {
        this.hop = hop;
        this.nextStageName = nextStageName;
        this.creator = creator;
        this.stage = stage;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchDataInternal.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchDataInternal<D> hopScotchData, LintStoneMessageEventContext mec) {
        int originalHash = hopScotchData.getData().hashCode();
        try {
            HopScotchDataWrapper<D> wrapper = new HopScotchDataWrapper<>(hopScotchData,stage,creator);
            hop.process(wrapper);
            int postProcessHash = hopScotchData.getData().hashCode();
            if (originalHash != postProcessHash) {
                // We can't prevent a Hop modifying the original data, because we can't enforce immutable data.
                // But we will add an annoying statistic because this introduces concurrency in the actor framework
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", "modified_data"));
            }
            mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData.copy()));
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            // we forward the input instead the output
            mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData.copy()));
        }
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
