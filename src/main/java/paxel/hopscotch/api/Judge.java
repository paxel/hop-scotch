package paxel.hopscotch.api;

/**
 * Used to judge if a Hop should handle the Data.
 *
 * @param <M> The type of the Meta data.
 * @param <D> The type of the Data.
 */
public interface Judge<M, D> {
    Judgement<M> judge(HopScotchData<D> data);
}
