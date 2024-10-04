package paxel.hopscotch.impl;

import paxel.hopscotch.api.GateFactory;
import paxel.hopscotch.api.HopFactory;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.List;

public class StageActor<D> implements LintStoneActor {
    private final Integer stage;

    private JudgedActorMap< D> judges = new JudgedActorMap<>();
    private Gates<D> bill = new Gates<>();

    public StageActor(Integer stage, List<Object> factories) {
        this.stage = stage;
        for (Object factory : factories) {
            switch (factory) {
                case HopFactory<?> hf -> {
                    judges.add((HopFactory<D>) hf);
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

    }
}
