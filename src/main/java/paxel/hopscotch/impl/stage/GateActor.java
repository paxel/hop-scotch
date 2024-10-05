package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Gate;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

public class GateActor implements LintStoneActor {
    public <D> GateActor(Gate<D> gate, String nextStageName, Config config) {
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
        // TODO: implement
    }
}
