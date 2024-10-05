package paxel.hopscotch.api;

/**
 * Used to judge if a Hop should handle the Data.
 *
 * @param <D> The type of the Data.
 */
public interface Judge<D> {
    /**
     * The Judgment provides the result of the decision of the Judge.
     *
     * @param data The data to be judged
     * @return the Judgment
     */
    Judgment<D> judge(HopScotchData<D> data);
}
