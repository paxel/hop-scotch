package paxel.hopscotch.impl.statistic;

import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.Stage;
import paxel.hopscotch.api.Statistics;
import paxel.hopscotch.impl.stage.StageActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.Consumer;

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
     * A keyword for received messages
     */
    public static final String RECEIVED = "received";

    /**
     * A keyword for sent messages
     */
    public static final String SENT = "sent";

    /**
     * A message for requesting data
     */
    public static final Request REQUEST = new Request("Statistics");
    private final Consumer<Statistics> finalStatisticConsumer;

    /**
     * Constructs the actor
     *
     * @param finalStatisticConsumer The receiver of the final statistix.
     */

    public StatisticsActor(Consumer<Statistics> finalStatisticConsumer) {
        this.finalStatisticConsumer = finalStatisticConsumer;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(Request.class, this::requestReceived)
                .inCase(Increment.class, this::increment)
                .inCase(StageActor.PoisonPill.class, this::finish)
                .otherwise(this::unknown);
    }

    private void increment(Increment increment, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // TODO: counter
    }

    private void finish(StageActor.PoisonPill poisonPill, LintStoneMessageEventContext mec) {
        // TODO: counter
        finalStatisticConsumer.accept(createStatistics());
        mec.unregister();
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        // TODO: errorcounter
    }


    private void requestReceived(Request request, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // TODO: counter
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
