package paxel.hopscotch.api;

/**
 * The Business logic of processing the data
 *
 * @param <D> The type of the initial data
 */
public interface Hop<D> {

    /**
     * This process can read the HopScotchData and
     *
     * @param data The previous stage data
     * @return The enriched data
     */

    void process(HopScotchData<D> data);
}
