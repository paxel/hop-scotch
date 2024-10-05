package paxel.hopscotch.api;

/**
 * Used to judge if a Hop should handle the Data.
 *
 * @param <D> The type of the Data.
 */
public interface Judge<D> {
    Judgment judge(HopScotchData<D> data);
}
