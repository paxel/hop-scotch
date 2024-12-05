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
    public static final Request REQUEST = new Request();
    private static final String INCREMENT = "increment";
    private static final String POISON_PILL = "poison-pill";
    private static final String UNKNOWN = "unknown";
    private static final String REQUEST_MESSAGE = "request-message";
    private final Consumer<Statistics> finalStatisticConsumer;
    private final MutableStatistic statistic = new MutableStatistic();

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

    private void increment(Increment increment, LintStoneMessageEventContext mec) {
        statistic.increment(1, mec.getName(), RECEIVED, INCREMENT);
        statistic.increment(increment.value, increment.path);
    }

    private void finish(StageActor.PoisonPill poisonPill, LintStoneMessageEventContext mec) {
        statistic.increment(1, mec.getName(), RECEIVED, POISON_PILL);
        finalStatisticConsumer.accept(createStatistics());
        mec.unregister();
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        statistic.increment(1, mec.getName(), RECEIVED, UNKNOWN);
    }


    private void requestReceived(Request request, LintStoneMessageEventContext mec) {
        statistic.increment(1, mec.getName(), RECEIVED, REQUEST_MESSAGE);
        mec.reply(createStatistics());
    }

    private Statistics createStatistics() {
        return statistic.snapshot();
    }

    /**
     * The Request message
     */
    public record Request() {
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
