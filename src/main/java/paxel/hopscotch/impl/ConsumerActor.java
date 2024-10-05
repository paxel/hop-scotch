package paxel.hopscotch.impl;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.HopScotchData;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

import java.util.function.Consumer;

public class ConsumerActor<D> implements LintStoneActor {
    public static final String CONSUMER = "Consumer";

    private final Consumer<HopScotchData<D>> consumer;
    private final Config config;

    public ConsumerActor(Consumer<HopScotchData<D>> consumer, Config config) {
        this.consumer = consumer;
        this.config = config;
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
