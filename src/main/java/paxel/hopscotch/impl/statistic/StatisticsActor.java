package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Statistics;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

/**
 * A central collection of numbers
 */
public class StatisticsActor implements LintStoneActor {
    /**
     * The name of the actor
     */
    public static final String STATISTICS = "Statistics";
    /**
     * A keyword for processed data
     */
    public static final String PROCESSED = "processed";

    /**
     * A message
     */
    public static final Request REQUEST = new Request("Statistics");

    /**
     * Constructs the actor
     */
    public StatisticsActor() {
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

    /**
     * The Request message
     *
     * @param request The request
     */
    public record Request(String request) {
    }

    /**
     * The increment message
     *
     * @param value   The additional value
     * @param stage   The stage
     * @param creator The creator
     * @param path    The path of the statistic
     */
    public record Increment(long value, Stage stage, Creator creator, String... path) {
    }
}
