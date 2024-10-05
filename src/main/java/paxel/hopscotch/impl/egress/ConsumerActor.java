package paxel.hopscotch.impl.egress;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.Consumer;

import static paxel.hopscotch.impl.statistic.StatisticsActor.PROCESSED;
import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class ConsumerActor<D> implements LintStoneActor {
    public static final String CONSUMER = "Consumer";

    private final Consumer<HopScotchData<D>> consumer;
    private final Config config;
    private StatisticsActor.Increment incMessage;

    public ConsumerActor(Consumer<HopScotchData<D>> consumer, Config config) {
        this.consumer = consumer;
        this.config = config;
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
        // only create it once, to reduce garbage
        if (incMessage == null)
            incMessage = new StatisticsActor.Increment(1L, mec.getName(), PROCESSED);
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }


    private void dataReceived(HopScotchData<D> hopScotchData, LintStoneMessageEventContext mec) {
        consumer.accept(hopScotchData);
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
    }
}
