package paxel.hopscotch.impl;

import paxel.hopscotch.api.HopScotchData;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.Consumer;

public class ConsumerActor<D> implements LintStoneActor {
    private final Consumer<HopScotchData<D>> consumer;

    public ConsumerActor(Consumer<HopScotchData<D>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        mec.inCase(HopScotchData.class, this::dataReceived)
                .otherwise(this::unknown);
    }

    private void unknown(Object o, LintStoneMessageEventContext lintStoneMessageEventContext) {
        // TODO: error handling
    }

    private void dataReceived(HopScotchData<D> hopScotchData, LintStoneMessageEventContext lintStoneMessageEventContext) {
        if (consumer != null) {
            consumer.accept(hopScotchData);
        }
    }
}
