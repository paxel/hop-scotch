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

    @Test
    void testStartAndEnd() throws InterruptedException {
        HopScotchSystemBuilder<String> classUnderTest = HopScotchSystemBuilder.builder();
        classUnderTest.add(new GateFactory<>() {
            @Override
            public int getStage() {
                return 1;
            }

            @Override
            public Gate<String> createGate() {
                return new Gate<>() {
                    @Override
                    public boolean canPass(HopScotchData<String> data) {
                        return false;
                    }
                };
            }
        });

        classUnderTest.add(new JudgeFactory<>() {
            @Override
            public int getStage() {
                return 2;
            }

            @Override
            public Judge<String> createJudge() {
                return new Judge<>() {

                    @Override
                    public Judgment<String> judge(HopScotchData<String> data) {
                        return new Judgment<>() {
                            @Override
                            public boolean isAccepted() {
                                return false;
                            }

                            @Override
                            public HopId getId() {
                                return new HopId("1");
                            }

                            @Override
                            public Hop<String> createHop() {
                                return new Hop<String>() {
                                    @Override
                                    public void process(HopScotchData<String> data) {
                                        // nope
                                    }
                                };
                            }
                        };
                    }
                };
            }
        });

        HopScotchSystem<String> build = classUnderTest.build();
        build.awaitFinish();
    }
}