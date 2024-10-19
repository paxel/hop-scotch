package paxel.hopscotch.impl.egress;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.egress.ConsumerActor.CONSUMER;
import static paxel.hopscotch.impl.statistic.StatisticsActor.PROCESSED;
import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * The Terminator actor is the last Stage, that will forward all Data that was not dropped to the ConsumerActor.
 *
 * @param <D> The data type
 */
public class TerminatorActor<D> implements LintStoneActor {
    private final Creator creator;
    private StatisticsActor.Increment incMessage;


    /**
     * The name of this actor
     */
    public static final String TERMINATOR = "Terminator";

    /**
     * Constructs an instance with a Creator. The last stage has no stage
     *
     * @param creator The creator
     */
    public TerminatorActor(Creator creator) {
        this.creator = creator;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }


    private void processData(HopScotchData<D> hopScotchData, LintStoneMessageEventContext mec) {
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
        mec.getActor(CONSUMER).tell(hopScotchData);
    }

    private void ensureMessage(LintStoneMessageEventContext mec) {
        // only create it once, to reduce garbage
        if (incMessage == null)
            incMessage = new StatisticsActor.Increment(1L, null, creator, mec.getName(), PROCESSED);
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, null, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }

}
