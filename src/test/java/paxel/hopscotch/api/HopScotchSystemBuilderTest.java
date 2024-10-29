package paxel.hopscotch.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HopScotchSystemBuilderTest {

    @Test
    void testNoBuilderShouldFail() {
        HopScotchSystemBuilder<String> classUnderTest = HopScotchSystemBuilder.builder();
        assertThatThrownBy(classUnderTest::build)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Need at least one Hop or GateFactory");
    }

    @Test
    void testStartAndEnd() throws InterruptedException {
        HopScotchSystemBuilder<String> classUnderTest = HopScotchSystemBuilder.builder();
        classUnderTest.add(GateFactory.create(1, () -> d -> false));
        classUnderTest.add(JudgeFactory.create(2, () -> Judge.create(d -> Judgment.create(true, mock(), new HopId("a")))));

        HopScotchSystem<String> build = classUnderTest.build();
        build.awaitFinish();
    }
}