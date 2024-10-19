package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Gate;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;
import paxel.hopscotch.impl.data.HopScotchEnrichedDataWrapper;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * The actor for a gate
 *
 * @param <D> The data type
 */
public class GateActor<D> implements LintStoneActor {
    private final Gate<D> gate;
    private final String nextStageName;
    private final Creator creator;
    private final Stage stage;

    /**
     * Constructs an actor
     *
     * @param gate          The gate of this actor
     * @param nextStageName The receiver of the results
     * @param stage         The stage this actor is part of
     * @param creator       The creator name of this gate
     */
    public GateActor(Gate<D> gate, String nextStageName, Stage stage, Creator creator) {
        this.gate = gate;
        this.nextStageName = nextStageName;
        this.creator = creator;
        this.stage = stage;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchEnrichedData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchEnrichedData<D> hopScotchData, LintStoneMessageEventContext mec) {
        HopScotchEnrichedDataWrapper<D> hopScotchEnrichedDataWrapper = new HopScotchEnrichedDataWrapper<>(hopScotchData, stage, creator);

        try {
            if (!gate.canPass(hopScotchEnrichedDataWrapper)) {
                mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "dropped"));
                mec.getActor(nextStageName).tell(new StageActor.Drop<>(hopScotchData.copy()));
            } else {
                mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData.copy()));
            }
        } catch (RuntimeException e) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "invalid_result", e.getClass().getSimpleName()));
            mec.getActor(nextStageName).tell(new StageActor.Fragment<>(hopScotchData.copy()));
        }
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
