package paxel.hopscotch.api;

public interface GateFactory<D> {
    /**
     * Rerieve the stage number (must be >= 0)
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

}
