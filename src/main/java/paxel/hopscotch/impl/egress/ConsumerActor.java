package paxel.hopscotch.impl.egress;

import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.Consumer;

import static paxel.hopscotch.impl.statistic.StatisticsActor.PROCESSED;
import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class ConsumerActor<D> implements LintStoneActor {
    public static final String CONSUMER = "Consumer";
    private final Creator creator;
    private final Consumer<HopScotchData<D>> consumer;
    private StatisticsActor.Increment incMessage;

    public ConsumerActor(Creator creator, Consumer<HopScotchData<D>> consumer) {
        this.creator = creator;
        this.consumer = consumer;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        // only do anything if there is a consumer
        if (consumer != null) {
            mec.inCase(HopScotchData.class, this::dataReceived)
                    .otherwise(this::unknown);
        }
    }


    private void ensureMessage(LintStoneMessageEventContext mec) {
        // only create it once, to reduce gc
        if (incMessage == null)
            incMessage = new StatisticsActor.Increment(1L, null, creator, mec.getName(), PROCESSED);
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, null, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }


    private void dataReceived(HopScotchData<D> hopScotchData, LintStoneMessageEventContext mec) {
        consumer.accept(hopScotchData);
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
    }
}
