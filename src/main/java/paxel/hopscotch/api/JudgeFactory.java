package paxel.hopscotch.api;

import java.util.function.Supplier;

/**
 * This is used to create Judges which will create Hops depending on the Data.
 *
 * @param <D> The data type
 */
public interface JudgeFactory<D> {
    /**
     * Retrieve the stage number (must be >= 0)
     *
     * @return the stage
     */
    int getStage();


    /**
     * Create a Judge to judge the data
     *
     * @return the {@link Judge<D>}
     */
    Judge<D> createJudge();

    /**
     * Creates a simple Factory for a stage with a supplier for the {@link Judge}
     *
     * @param stage    The stage
     * @param supplier The supplier for the gate
     * @param <T>      The data type
     * @return an anonymous instance of the GateFactory
     */
    static <T> JudgeFactory<T> create(int stage, Supplier<Judge<T>> supplier) {
        return new JudgeFactory<>() {
            @Override
            public int getStage() {
                return stage;
            }

            @Override
            public Judge<T> createJudge() {
                return supplier.get();
            }
        };
    }
}
