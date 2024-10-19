package paxel.hopscotch.api;

import java.util.function.Supplier;

/**
 * The user can implement this factory or use the {@link #create(int, Supplier)} function.
 *
 * @param <D> the data type
 */
public interface GateFactory<D> {
    /**
     * Retrieve the stage number (must be >= 0)
     *
     * @return the stage
     */
    int getStage();


    /**
     * Create a Gate to pass or block the data
     *
     * @return the {@link Gate<D>}
     */

    Gate<D> createGate();

    /**
     * Creates a simple Factory for a stage with a supplier for the {@link Gate}
     *
     * @param stage    The stage
     * @param supplier The supplier for the gate
     * @param <T>      The data type
     * @return an anonymous instance of the GateFactory
     */
    static <T> GateFactory<T> create(int stage, Supplier<Gate<T>> supplier) {
        return new GateFactory<>() {
            @Override
            public int getStage() {
                return stage;
            }

            @Override
            public Gate<T> createGate() {
                return supplier.get();
            }
        };
    }
}
