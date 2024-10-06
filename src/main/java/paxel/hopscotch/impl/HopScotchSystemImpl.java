package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.HopScotchSystem;
import paxel.hopscotch.api.Statistics;
import paxel.hopscotch.api.enrichment.Stage;
import paxel.hopscotch.impl.data.HopScotchDataImpl;
import paxel.hopscotch.impl.egress.ConsumerActor;
import paxel.hopscotch.impl.egress.TerminatorActor;
import paxel.hopscotch.impl.ingress.IngressActor;
import paxel.hopscotch.impl.stage.StageActor;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.ActorSettings;
import paxel.lintstone.api.LintStoneActorAccessor;
import paxel.lintstone.api.LintStoneSystem;
import paxel.lintstone.api.LintStoneSystemFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static paxel.hopscotch.impl.egress.ConsumerActor.CONSUMER;
import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * The implementation of the Pipeline management.
 *
 * @param <D> The type of the data
 */
public class HopScotchSystemImpl<D> implements HopScotchSystem<D> {

    public static final String INGRESS = "Ingress";
    public static final String TERMINATOR = "Terminator";


    private final Config config;
    private LintStoneSystem lintStoneSystem;
    private LintStoneActorAccessor ingress;

    public HopScotchSystemImpl(Config config) {
        this.config = config;
    }

    public void start(Map<Integer, List<Object>> factories, Consumer<HopScotchData<D>> consumer) {
        if (factories.isEmpty())
            throw new IllegalArgumentException("factories cannot be empty");
        lintStoneSystem = LintStoneSystemFactory.create();

        // Collects statistics from all Actors and provides them on demand
        lintStoneSystem.registerActor(STATISTICS, () -> new StatisticsActor(config), ActorSettings.DEFAULT);
        // Responsible to add all finished Data to the Consumer
        lintStoneSystem.registerActor(CONSUMER, () -> new ConsumerActor(consumer), ActorSettings.DEFAULT);

        ingress = lintStoneSystem.registerActor(INGRESS, () -> new IngressActor(config), ActorSettings.DEFAULT);

        String previousName = INGRESS;
        for (Map.Entry<Integer, List<Object>> integerListEntry : factories.entrySet()) {
            Integer stage = integerListEntry.getKey();
            String name = "Stage-" + stage;
            lintStoneSystem.registerActor(name, () -> new StageActor(new Stage(stage), integerListEntry.getValue(), config), ActorSettings.DEFAULT);
            if (previousName != null) {
                // we tell the previous actor what the next stage is
                lintStoneSystem.getActor(previousName).tell(name);
            }
            previousName = name;
        }

        lintStoneSystem.getActor(previousName).tell(TERMINATOR);
        lintStoneSystem.registerActor(TERMINATOR, () -> new TerminatorActor(), ActorSettings.DEFAULT);

    }

    @Override
    public void add(D data) {
        ingress.tell(new HopScotchDataImpl<>(data));
    }

    @Override
    public Statistics getStatistics() throws ExecutionException, InterruptedException {
        return lintStoneSystem.getActor(STATISTICS)
                .<Statistics>ask(StatisticsActor.REQUEST)
                .get();
    }

    @Override
    public boolean awaitFinish() {
        // TODO: poison pill and await finish
        return false;
    }

    @Override
    public boolean awaitFinish(Duration timeout) {
        // TODO: poison pill and await finish
        return false;
    }
}
