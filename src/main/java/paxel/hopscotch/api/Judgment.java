package paxel.hopscotch.api;

/**
 * The result of a Judge. If the Judgment is accepted, a new Hop can be created with the Meta.
 *
 * @param <D> The data type
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


    /**
     * Creates a simple Judgment.
     *
     * @param accepted if the Judge accepted the data.
     * @param hop      The Hop to use.
     * @param hopId    The hop ID
     * @param <T>      The type of the data
     * @return a new Judgment
     */
    static <T> Judgment<T> create(boolean accepted, Hop<T> hop, HopId hopId) {
        return new Judgment<T>() {
            @Override
            public boolean isAccepted() {
                return accepted;
            }

            @Override
            public HopId getId() {
                return hopId;
            }

            @Override
            public Hop<T> createHop() {
                return hop;
            }
        };
    }
}
