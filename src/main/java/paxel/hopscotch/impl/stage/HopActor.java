package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.Hop;
import paxel.hopscotch.api.Stage;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;
import paxel.hopscotch.impl.data.HopScotchEnrichedDataWrapper;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * Actor responsible to process data with a Hop
 *
 * @param <D> The data type
 */
public class HopActor<D> implements LintStoneActor {
    private final Hop<D> hop;
    private final Stage nextStage;
    private final Creator creator;
    private final Stage stage;

    /**
     * Constructs the Actor
     *
     * @param hop       The hop
     * @param nextStage The next stage
     * @param stage     The stage
     * @param creator   The creator
     */
    public HopActor(Hop<D> hop, Stage nextStage, Stage stage, Creator creator) {
        this.hop = hop;
        this.nextStage = nextStage;
        this.creator = creator;
        this.stage = stage;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchEnrichedData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchEnrichedData<D> hopScotchData, LintStoneMessageEventContext mec) {
        int originalHash = hopScotchData.getData().hashCode();
        try {
            HopScotchEnrichedDataWrapper<D> wrapper = new HopScotchEnrichedDataWrapper<>(hopScotchData, stage, creator);
            hop.process(wrapper);
            int postProcessHash = hopScotchData.getData().hashCode();
            if (originalHash != postProcessHash) {
                // We can't prevent a Hop modifying the original data, because we can't enforce immutable data.
                // But we will add an annoying statistic because this introduces concurrency in the actor framework
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", "modified_data"));
            }
            mec.getActor(nextStage.name()).tell(new StageActor.Fragment<>(hopScotchData.copy()));
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            // we forward the input instead the output
            mec.getActor(nextStage.name()).tell(new StageActor.Fragment<>(hopScotchData.copy()));
        }
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
