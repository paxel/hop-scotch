package paxel.hopscotch.impl;

import paxel.hopscotch.api.Statistics;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

public class StatisticsActor implements LintStoneActor {
    public static final Request REQUEST = new Request("Statistics");

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(Request.class, this::requestReceived)
                .otherwise(this::unknown);
    }

    private void unknown(Object o, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // TODO error handling
    }

    private void requestReceived(Request request, LintStoneMessageEventContext lintStoneMessageEventContext) {
        lintStoneMessageEventContext.reply(createStatistics());
    }

    private Statistics createStatistics() {
        return new Statistics() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

    record Request(String request) {
    }
}
