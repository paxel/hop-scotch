package paxel.hopscotch.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HopScotchSystemBuilderTest {

    @Test
    void testNoBuilderShouldFail() {
        HopScotchSystemBuilder<String> classUnderTest = HopScotchSystemBuilder.builder();
        assertThatThrownBy(classUnderTest::build)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Need at least one Hop or GateFactory");
    }
}