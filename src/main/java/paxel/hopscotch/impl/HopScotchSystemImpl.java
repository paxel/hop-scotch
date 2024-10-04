package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.hopscotch.api.HopScotchSystem;
import paxel.hopscotch.api.Statistics;
import paxel.lintstone.api.ActorSettings;
import paxel.lintstone.api.LintStoneSystem;
import paxel.lintstone.api.LintStoneSystemFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * The implementation of the Pipeline management.
 *
 * @param <D> The type of the data
 */
public class HopScotchSystemImpl<D> implements HopScotchSystem<D> {

    public static final String STATISTICS = "Statistics";
    public static final String CONSUMER = "Consumer";
    private final Config config;
    private LintStoneSystem lintStoneSystem;

    public HopScotchSystemImpl(Config config) {
        this.config = config;
    }

    public void start(Map<Integer, List<Object>> factories, Consumer<HopScotchData<D>> consumer) {
        if (factories.isEmpty())
            throw new IllegalArgumentException("factories cannot be empty");
        lintStoneSystem = LintStoneSystemFactory.create();

        // Collects statistics from all Actors and provides them on demand
        lintStoneSystem.registerActor(STATISTICS, () -> new StatisticsActor(), ActorSettings.DEFAULT);
        // Responsible to add all finished Data to the Consumer
        lintStoneSystem.registerActor(CONSUMER, () -> new ConsumerActor(consumer), ActorSettings.DEFAULT);

        String previousName = null;
        for (Map.Entry<Integer, List<Object>> integerListEntry : factories.entrySet()) {
            Integer key = integerListEntry.getKey();
            String name = "Stage-" + key;
            lintStoneSystem.registerActor(name, () -> new StageActor(key, integerListEntry.getValue()), ActorSettings.DEFAULT);
            if (previousName != null) {
                // we tell the previous actor what the next stage is
                lintStoneSystem.getActor(previousName).tell(name);
            }
            previousName = name;
        }


    }

    @Override
    public void add(D data) {
        // TODO: signal data to first stage
    }

    @Override
    public Statistics getStatistics() throws ExecutionException, InterruptedException {
        return lintStoneSystem.getActor(STATISTICS)
                .<Statistics>ask(StatisticsActor.REQUEST)
                .get();
    }

    @Override
    public boolean awaitFinish() {
        // TODO: await finish
        return false;
    }

    @Override
    public boolean awaitFinish(Duration timeout) {
        // TODO: await finish
        return false;
    }
}
