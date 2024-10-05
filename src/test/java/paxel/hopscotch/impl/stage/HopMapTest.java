package paxel.hopscotch.impl.stage;

import org.junit.jupiter.api.Test;
import paxel.hopscotch.api.HopId;
import paxel.lintstone.api.LintStoneActorAccessor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class HopMapTest {

    @Test
    void testCreate() {
        assertThat(new HopMap<>().computeIfAbsent(1, mock(), () -> mock())).isNotNull();
    }

    @Test
    void testRecreate() {
        HopMap<String> hopMap = new HopMap<>();
        HopId mockId = mock();
        LintStoneActorAccessor mock = mock();
        hopMap.computeIfAbsent(1, mockId, () -> mock);

        assertThat(hopMap.computeIfAbsent(1, mockId, () -> null)).isEqualTo(mock);
    }
}