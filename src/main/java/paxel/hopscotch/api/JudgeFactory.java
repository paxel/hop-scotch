package paxel.hopscotch.api;

public interface JudgeFactory<D> {
    /**
     * Rerieve the stage number (must be >= 0)
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
}
