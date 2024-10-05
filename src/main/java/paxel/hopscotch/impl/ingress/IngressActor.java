package paxel.hopscotch.impl.ingress;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.BiConsumer;

import static paxel.hopscotch.impl.statistic.StatisticsActor.PROCESSED;
import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * The IngressActor receives the data from outside, counts it via StatisticsActor and forwards to the first stage.
 * Also handles backpressure if configured.
 *
 * @param <D> The type of the data
 */
public class IngressActor<D> implements LintStoneActor {
    private final Config config;
    private StatisticsActor.Increment incMessage;
    private String firstStage;
    private BiConsumer<LintStoneMessageEventContext, HopScotchData<D>> forwarder = (m, d) -> m.getActor(firstStage).tell(d);

    public IngressActor(Config config) {
        this.config = config;
        int backPressure = config.backPressure();
        if (backPressure > 0) {
            forwarder = (m, d) -> {
                try {
                    m.getActor(firstStage)
                            .tellWithBackPressure(d, backPressure);
                } catch (InterruptedException e) {
                    // TODO: more graceful handling
                    throw new RuntimeException(e);
                }
            };
        }


    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(String.class, (firstStageName, b) -> this.firstStage = firstStageName)
                .inCase(HopScotchData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchData hopScotchData, LintStoneMessageEventContext mec) {
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
        forwarder.accept(mec, hopScotchData);
    }

    private void ensureMessage(LintStoneMessageEventContext mec) {
        // only create it once, to reduce garbage
        if (incMessage == null)
            incMessage = new StatisticsActor.Increment(1L, mec.getName(), PROCESSED);
    }

    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }
}
