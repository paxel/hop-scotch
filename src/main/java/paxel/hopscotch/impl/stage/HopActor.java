package paxel.hopscotch.impl.stage;

import paxel.hopscotch.api.Config;
import paxel.hopscotch.api.Hop;
import paxel.lintstone.api.LintStoneActor;
import paxel.lintstone.api.LintStoneMessageEventContext;

public class HopActor implements LintStoneActor {
    public <D> HopActor(Hop<D> hop, String nextStageName, Config config) {
    }

    @Override
    public void newMessageEvent(LintStoneMessageEventContext mec) {
    // TODO: implement
    }
}
