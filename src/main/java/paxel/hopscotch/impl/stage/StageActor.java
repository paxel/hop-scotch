package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.*;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;
import paxel.hopscotch.impl.data.HopScotchEnrichedDataWrapper;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.ActorSettings;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneActorAccessor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

/**
 * Actor for muxing and demuxing and dropping data on each stage
 *
 * @param <D> The data type
 */
public class StageActor<D> implements LintStoneActor {
    private final Creator creator;
    private final Stage stage;
    private final int backPressure;

    private final List<Judge<D>> judges = new ArrayList<>();
    private final List<GateFactory<D>> gateFactories = new ArrayList<>();
    private Stage nextStage;
    private final HopMap hopMap = new HopMap();
    private final GateMap gateMap = new GateMap();
    private final DataAggregator<D> aggregator = new DataAggregator<>();
    private LintStoneActorAccessor nextStageActor;
    private LintStoneActorAccessor statistix;

    /**
     * Constructs an Actor.
     *
     * @param factories The factories of this stage
     * @param config    The config
     * @param stage     The stage
     */
    public StageActor(List<Object> factories, Config config, Stage stage) {
        backPressure = config.backPressure();
        this.creator = new Creator(stage.name());
        this.stage = stage;
        for (Object factory : factories) {
            switch (factory) {
                case JudgeFactory<?> hf -> judges.add(((JudgeFactory<D>) hf).createJudge());
                case GateFactory<?> gf -> gateFactories.add(((GateFactory<D>) gf));
                case null -> throw new IllegalArgumentException("Factory must not be null");
                default -> throw new IllegalArgumentException("Unknown factory type: " + factory.getClass());
            }
        }
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(Single.class, this::processSingle)
                .inCase(Fragment.class, this::processFragment)
                .inCase(Split.class, this::updateFragment)
                .inCase(Drop.class, this::dropData)
                .inCase(Stage.class, (ns, m) -> this.nextStage = ns)
                .otherwise(this::unknown);
    }

    private void dropData(Drop<D> drop, LintStoneMessageEventContext mec) {
        statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "aggregation", "dropped"));
        aggregator.drop(drop.hopScotchData());
    }

    private LintStoneActorAccessor statistix(LintStoneMessageEventContext mec) {
        if (this.statistix != null)
            return statistix;
        this.statistix = mec.getActor(STATISTICS);
        return statistix;
    }


    /**
     * A Single Data was received. There is no need to aggregate it.
     *
     * @param single The single containing the data
     * @param mec    The context
     */
    private void processSingle(Single<D> single, LintStoneMessageEventContext mec) {
        statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "single"));
        processCompleteData(single.hopScotchData(), mec);
    }

    /**
     * A split message was received for data that was expectedFragments to multiple Gates or Hops. It can only be processed after all results have been received
     *
     * @param split Defines the number of fragments to await
     * @param mec   The context
     */
    private void updateFragment(Split<D> split, LintStoneMessageEventContext mec) {
        Optional<HopScotchEnrichedData<D>> aggregation = aggregator.update(split.hopScotchData(), split.expectedFragments());
        if (aggregation.isPresent()) {
            statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "aggregation", "by_split"));
            processCompleteData(aggregation.get(), mec);
        }
    }

    private void processFragment(Fragment<D> fragment, LintStoneMessageEventContext mec) {
        statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "fragment", StatisticsActor.PROCESSED));
        Optional<HopScotchEnrichedData<D>> aggregation = aggregator.add(fragment.hopScotchData());

        if (aggregation.isPresent()) {
            statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "aggregation", "by_fragment"));
            processCompleteData(aggregation.get(), mec);
        }
    }

    private void processCompleteData(HopScotchEnrichedData<D> data, LintStoneMessageEventContext mec) {

        int sent = distributeData(mec, data);
        // Notify the next stage about the number of splits to merge
        if (sent == 0) {
            // this data was not given to any Gate or Hop, so we just forward it to the next stage
            tellNextStage(mec, new Single<>(data.copy()));
            statistix(mec).tell(new StatisticsActor.Increment(sent, stage, creator, mec.getName(), "forwarded"));
        } else {
            tellNextStage(mec, new Split<>(data.copy(), sent));
            statistix(mec).tell(new StatisticsActor.Increment(sent, stage, creator, mec.getName(), "fragmented"));
        }
    }

    private void tellNextStage(LintStoneMessageEventContext mec, Object message) {
        if (backPressure > 0) {
            try {
                nextStage(mec).tellWithBackPressure(message, backPressure);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        } else
            nextStage(mec).tell(message);
    }

    private LintStoneActorAccessor nextStage(LintStoneMessageEventContext mec) {
        if (nextStageActor != null)
            return nextStageActor;
        nextStageActor = mec.getActor(nextStage.name());
        return nextStageActor;
    }


    private int distributeData(LintStoneMessageEventContext mec, HopScotchEnrichedData<D> aggregation) {
        int hops = sendToHops(mec, aggregation);
        int gates = sendToGates(mec, aggregation);
        return hops + gates;
    }

    private int sendToGates(LintStoneMessageEventContext mec, HopScotchEnrichedData<D> aggregation) {
        int gateNumber = 0;
        for (GateFactory<D> gate : gateFactories) {
            gateNumber++;
            LintStoneActorAccessor actor = getGateActor(mec, gateNumber, gate);
            // forward message to hop
            actor.tell(aggregation.copy());
        }
        return gateNumber;
    }

    private int sendToHops(LintStoneMessageEventContext mec, HopScotchEnrichedData<D> aggregation) {
        int sent = 0;
        int hopNumber = 0;
        HopScotchEnrichedDataWrapper<D> data = new HopScotchEnrichedDataWrapper<>(aggregation, stage, creator);
        for (Judge<D> judge : judges) {
            hopNumber++;
            Judgment<D> judgement = judge.judge(data);
            if (judgement.isAccepted()) {
                LintStoneActorAccessor actor = getHopActor(mec, hopNumber, judgement.getId(), judgement);
                // forward message to hop
                actor.tell(aggregation.copy());
                sent++;
            }
        }
        return sent;
    }

    private LintStoneActorAccessor getGateActor(LintStoneMessageEventContext mec, int gateNumber, GateFactory<D> gateFactory) {
        return gateMap.computeIfAbsent(gateNumber, () -> {
            Gate<D> gate = gateFactory.createGate();
            String name = "Gate_" + stage + "_" + gateNumber + "_" + gate.getClass().getSimpleName();
            statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "gate", "created"));
            return mec.registerActor(name, () -> new GateActor<>(gate, nextStage, stage, new Creator(gate.getClass().getSimpleName() + "_" + gateNumber)), ActorSettings.DEFAULT);
        });
    }

    private LintStoneActorAccessor getHopActor(LintStoneMessageEventContext mec, int hopNumber, HopId id, Judgment<D> judgement) {
        return hopMap.computeIfAbsent(hopNumber, id, () -> {
            Hop<D> hop = judgement.createHop();
            String name = "Hop." + stage + "." + hopNumber + "." + hop.getClass().getSimpleName() + "." + id.id();
            statistix(mec).tell(new StatisticsActor.Increment(1, stage, creator, mec.getName(), "hop", "created"));
            return mec.registerActor(name, () -> new HopActor<>(hop, nextStage, stage, new Creator(hop.getClass().getSimpleName() + "_" + hopNumber)), ActorSettings.DEFAULT);
        });
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        statistix(mec).tell(new StatisticsActor.Increment(1L, stage, creator, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }

    /**
     * A Split message defines the number of fragments created on the previous stage.
     *
     * @param hopScotchData     The data
     * @param expectedFragments The number of fragments
     * @param <D>               The data type
     */
    public record Split<D>(HopScotchEnrichedData<D> hopScotchData, int expectedFragments) {
    }

    /**
     * A Drop message defines a Gate decision to drop on the previous stage
     *
     * @param hopScotchData The data
     * @param <D>           The data type
     */
    public record Drop<D>(HopScotchEnrichedData<D> hopScotchData) {
    }

    /**
     * The message was not delegated to any Hop or Gate on the previous stage
     *
     * @param hopScotchData The data
     * @param <D>           The data type
     */
    public record Single<D>(HopScotchEnrichedData<D> hopScotchData) {
    }

    /**
     * This is one result from a Gate or a Hop of multiple.
     *
     * @param hopScotchData The data
     * @param <D>           The data type
     */
    public record Fragment<D>(HopScotchEnrichedData<D> hopScotchData) {
    }
}
