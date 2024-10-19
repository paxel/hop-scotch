package paxel.hopscotch.impl.ingress;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.enrichment.Creator;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;
import paxel.hopscotch.impl.stage.StageActor;
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

    /**
     * The name of this actor
     */
    public static final String INGRESS = "Ingress";

    private StatisticsActor.Increment incMessage;
    private String firstStage;
    private BiConsumer<LintStoneMessageEventContext, Object> forwarder = (m, d) -> m.getActor(firstStage).tell(d);
    private final Creator creator;

    /**
     * Constructs an instance. This Stage has no Stage
     *
     * @param creator The creator
     * @param config  The config
     */
    public IngressActor(Creator creator, Config config) {
        this.creator = creator;
        int backPressure = config.backPressure();
        if (backPressure > 0) {
            // replacing the forwarder with backpressure variant if needed.
            forwarder = (m, d) -> {
                try {
                    m.getActor(firstStage)
                            .tellWithBackPressure(d, backPressure);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            };
        }
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(String.class, (firstStageName, b) -> this.firstStage = firstStageName)
                .inCase(HopScotchEnrichedData.class, this::processData)
                .otherwise(this::unknown);
    }

    private void ignore(String s, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // The first stage sends us its name. We know it already
    }

    private void processData(HopScotchEnrichedData<D> hopScotchData, LintStoneMessageEventContext mec) {
        ensureMessage(mec);
        mec.getActor(STATISTICS).tell(incMessage);
        forwarder.accept(mec, new StageActor.Single<>(hopScotchData));
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
