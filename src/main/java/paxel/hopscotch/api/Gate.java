package paxel.hopscotch.api;

/**
 * A gate needs to be implemented by the user.
 * It will receive all Data passing a Stage and can decide for each that it should be dropped.
 * If any Gate returns false, the next Stage will drop the aggregated data.
 *
 * @param <D> The data type
 */
public interface Gate<D> {

    /**
     * The Method that decides if the Data can continue to the next stage
     *
     * @param data The previous stage data
     * @return {@code true} only if the next stage should process this data
     */
    boolean canPass(HopScotchData<D> data);
}
