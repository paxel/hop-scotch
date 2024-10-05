package paxel.hopscotch.api;

/**
 * The result of a Judge. If the Judgement is accepted, a new Hop can be created with the Meta.
 */
public interface Judgment<D> {
    /**
     * Retrieve if the given Data should be processed by a Hop of this Judge.
     *
     * @return {@code true} if yes
     */
    boolean isAccepted();

    /**
     * Retrieve the ID of the Hop that should be processing the data.
     *
     * @return the HopId
     */
    HopId getId();

    /**
     * Create the Hop to handle the Data
     *
     * @return The new hop
     */
    Hop<D> createHop();
}
