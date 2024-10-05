package paxel.hopscotch.impl.stage;

import org.junit.jupiter.api.Test;
import paxel.lintstone.api.LintStoneActorAccessor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class GateMapTest {

    @Test
    void testCreate() {
        assertThat(new GateMap().computeIfAbsent(1, () -> mock())).isNotNull();
    }

    @Test
    void testRecreate() {
        GateMap gateMap = new GateMap();
        LintStoneActorAccessor mock = mock();
        gateMap.computeIfAbsent(1, () -> mock);

        assertThat(gateMap.computeIfAbsent(1, () -> null)).isEqualTo(mock);
    }
}