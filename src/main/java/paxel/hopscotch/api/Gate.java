package paxel.hopscotch.api;

public interface Gate<D> {

    /**
     * The Method that decides if the Data can continue to the next stage
     *
     * @param data The previous stage data
     * @return {@code true} only if the next stage should process this data
     */
    boolean canPass(HopScotchData<D> data);
}
