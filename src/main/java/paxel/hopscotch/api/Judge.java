package paxel.hopscotch.api;

import java.util.function.Function;

/**
 * Used to judge if a Hop should handle the Data.
 *
 * @param <D> The type of the Data.
 */
public interface Judge<D> {
    /**
     * The Judgment provides the result of the Judges decision.
     *
     * @param data The data to be judged
     * @return the Judgment
     */
    Judgment<D> judge(HopScotchData<D> data);

    /**
     * Creates a simple Judge.
     *
     * @param factory The hopScotchData that comes in.
     * @param <T>     The type of the data
     * @return The Judge.
     */
    static <T> Judge<T> create(Function<HopScotchData<T>, Judgment<T>> factory) {
        return new Judge<T>() {
            @Override
            public Judgment<T> judge(HopScotchData<T> data) {
                return factory.apply(data);
            }
        };
    }
}
