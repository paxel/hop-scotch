package paxel.hopscotch.api;

public interface HopFactory<M, D> {
    /**
     * Rerieve the stage number (must be >= 0)
     *
     * @return the stage
     */
    int getStage();

    /**
     * Create a Hop with Meta Data
     *
     * @param m the Meta Data to create the Hop
     * @return The new hop
     */
    Hop<D> createHop(M m);

    /**
     * Create a Judge to judge the data
     * @return
     */
    Judge<M, D> createJudge();
}
