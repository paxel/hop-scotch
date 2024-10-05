package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.GateFactory;
import paxel.hopscotch.api.JudgeFactory;
import paxel.hopscotch.api.HopScotchData;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.List;

public class StageActor<D> implements LintStoneActor {
    private final Integer stage;

    private JudgedActorMap<D> judges = new JudgedActorMap<>();
    private Gates<D> bill = new Gates<>();
    private String nextStageName;


    public StageActor(Integer stage, List<Object> factories, Config config) {
        this.stage = stage;
        for (Object factory : factories) {
            switch (factory) {
                case JudgeFactory<?> hf -> {
                    judges.add((JudgeFactory<D>) hf);
                }
                case GateFactory<?> gf -> {
                    bill.add((GateFactory<D>) gf);
                }
                default -> {
                    // TODO: error handler
                }
            }
        }
    }


    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(String.class, (nextStageName, b) -> this.nextStageName = nextStageName)
                .inCase(HopScotchData.class,this::processData)
                .otherwise(this::unknown);
    }

    private void processData(HopScotchData hopScotchData, LintStoneMessageEventContext lintStoneMessageEventContext) {
    }

    private void unknown(Object o, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // TODO: error handling
    }
}
