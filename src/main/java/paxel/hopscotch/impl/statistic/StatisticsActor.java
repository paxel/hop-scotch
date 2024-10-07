package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Statistics;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

public class StatisticsActor implements LintStoneActor {
    public static final String STATISTICS = "Statistics";
    public static final String PROCESSED = "processed";

    public static final Request REQUEST = new Request("Statistics");
    private final Config config;

    public StatisticsActor(Config config) {
        this.config = config;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(Request.class, this::requestReceived)
                .otherwise(this::unknown);
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
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

    public record Request(String request) {
    }

    public record Increment(long value, Stage stage, Creator creator, String... path) {
    }
}
