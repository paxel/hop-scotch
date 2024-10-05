package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.*;
import paxel.hopscotch.impl.statistic.StatisticsActor;
import paxel.lintstone.api.ActorSettings;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneActorAccessor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static paxel.hopscotch.impl.statistic.StatisticsActor.STATISTICS;

public class StageActor<D> implements LintStoneActor {
    private final Integer stage;
    private final Config config;

    private List<Judge<D>> judges = new ArrayList<>();
    private List<GateFactory<D>> gateFactories = new ArrayList<>();
    private String nextStageName;
    private HopMap hopMap = new HopMap();
    private GateMap gateMap = new GateMap();
    private final DataAggregator<D> aggregator = new DataAggregator();

    public StageActor(Integer stage, List<Object> factories, Config config) {
        this.stage = stage;
        this.config = config;
        for (Object factory : factories) {
            switch (factory) {
                case JudgeFactory<?> hf -> {
                    judges.add(((JudgeFactory<D>) hf).createJudge());
                }
                case GateFactory<?> gf -> {
                    gateFactories.add(((GateFactory<D>) gf));
                }
                case null -> {
                    throw new IllegalArgumentException("Factory must not be null");
                }
                default -> {
                    throw new IllegalArgumentException("Unknown factory type: " + factory.getClass());
                }
            }
        }
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(Single.class, this::processSingle)
                .inCase(Fragment.class, this::processFragment)
                .inCase(Split.class, this::updateFragment)
                .inCase(Drop.class, this::dropData)
                .inCase(String.class, (nextStageName, b) -> this.nextStageName = nextStageName)
                .otherwise(this::unknown);
    }

    private void dropData(Drop drop, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "aggregation", "dropped"));
        aggregator.drop(drop.hopScotchData());
    }


    /**
     * A Single Data was received. There is no need to aggregate it.
     *
     * @param single The single containing the data
     * @param mec    The context
     */
    private void processSingle(Single single, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "single"));
        processCompleteData(single.hopScotchData(), mec);
    }

    /**
     * A split message was received for data that was sent to multiple Gates or Hops. It can only be processed after all results have been received
     *
     * @param split Defines the number of fragments to await
     * @param mec   The context
     */
    private void updateFragment(Split split, LintStoneMessageEventContext mec) {
        Optional<HopScotchData<D>> aggregation = aggregator.update(split.hopScotchData(), split.sent());
        if (aggregation.isPresent()) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "aggregation", "by_split"));
            processCompleteData(aggregation.get(), mec);

        }
    }

    private void processFragment(Fragment fragment, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "fragment", StatisticsActor.PROCESSED));
        Optional<HopScotchData<D>> aggregation = aggregator.add(fragment.hopScotchData());

        if (aggregation.isPresent()) {
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "aggregation", "by_fragment"));
            processCompleteData(aggregation.get(), mec);
        }
    }

    private void processCompleteData(HopScotchData<D> data, LintStoneMessageEventContext mec) {

        int sent = distributeData(mec, data);
        // Notify the next stage about the number of splits to merge
        if (sent == 0) {
            // this data was not given to any Gate or Hop, so we just forward it to the next stage
            mec.getActor(nextStageName).tell(new Single<>(data));
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(sent, mec.getName(), "fragmented"));
        } else {
            mec.getActor(nextStageName).tell(new Split<>(data, sent));
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(sent, mec.getName(), "forwarded"));
        }
    }

    private int distributeData(LintStoneMessageEventContext mec, HopScotchData<D> aggregation) {
        int hops = sendToHops(mec, aggregation);
        int gates = sendToGates(mec, aggregation);
        return hops + gates;
    }

    private int sendToGates(LintStoneMessageEventContext mec, HopScotchData<D> aggregation) {
        int gateNumber = 0;
        for (GateFactory<D> gate : gateFactories) {
            gateNumber++;
            LintStoneActorAccessor actor = getGateActor(mec, gateNumber, gate);
            // forward message to hop
            actor.tell(aggregation);
        }
        return gateNumber;
    }

    private int sendToHops(LintStoneMessageEventContext mec, HopScotchData<D> aggregation) {
        int sent = 0;
        int hopNumber = 0;
        for (Judge<D> judge : judges) {
            hopNumber++;
            Judgment<D> judgement = judge.judge(aggregation);
            if (judgement.isAccepted()) {
                LintStoneActorAccessor actor = getHopActor(mec, hopNumber, judgement.getId(), judgement);
                // forward message to hop
                actor.tell(aggregation);
                sent++;
            }
        }
        return sent;
    }

    private LintStoneActorAccessor getGateActor(LintStoneMessageEventContext mec, int gateNumber, GateFactory<D> gateFactory) {
        return gateMap.computeIfAbsent(gateNumber, () -> {
            Gate<D> gate = gateFactory.createGate();
            String name = "Gate." + stage + "." + gateNumber + "." + gate.getClass().getSimpleName();
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "gate", "created"));
            return mec.registerActor(name, () -> new GateActor(gate, nextStageName, config), ActorSettings.DEFAULT);
        });
    }

    private LintStoneActorAccessor getHopActor(LintStoneMessageEventContext mec, int hopNumber, HopId id, Judgment<D> judgement) {
        return hopMap.computeIfAbsent(hopNumber, id, () -> {
            Hop<D> hop = judgement.createHop();
            String name = "Hop." + stage + "." + hopNumber + "." + hop.getClass().getSimpleName() + "." + id.id();
            mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1, mec.getName(), "hop", "created"));
            return mec.registerActor(name, () -> new HopActor(hop, nextStageName, config), ActorSettings.DEFAULT);
        });
    }


    private void unknown(Object o, LintStoneMessageEventContext mec) {
        mec.getActor(STATISTICS).tell(new StatisticsActor.Increment(1L, mec.getName(), "unknown_message", o.getClass().getSimpleName()));
    }

    public record Split<D>(HopScotchData<D> hopScotchData, int sent) {
    }

    public record Drop<D>(HopScotchData<D> hopScotchData) {
    }

    public record Single<D>(HopScotchData<D> hopScotchData) {
    }

    public record Fragment<D>(HopScotchData<D> hopScotchData) {
    }
}
