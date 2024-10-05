package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import static paxel.hopscotch.impl.ConsumerActor.CONSUMER;
import static paxel.hopscotch.impl.StatisticsActor.STATISTICS;

public class TerminatorActor implements LintStoneActor {
    private final Config config;
    private String REMOVED = "removed";
    private StatisticsActor.Increment incMessage;

    public TerminatorActor(Config config) {
        this.config = config;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }


    private void processData(HopScotchData hopScotchData, LintStoneMessageEventContext mec) {
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
        mec.getActor(CONSUMER).tell(hopScotchData);
    }

    private void ensureMessage(LintStoneMessageEventContext mec) {
        // only create it once, to reduce garbage
        if (incMessage == null)
            incMessage = new StatisticsActor.Increment(1L, mec.getName(), REMOVED);
    }


    private void unknown(Object o, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // error handling
    }
}
