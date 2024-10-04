package paxel.hopscotch.api;

import java.time.Duration;

/**
 * The Interface of the Pipeline for the user.
 *
 * @param <D> The type of the Data. it should be immutable, or not be muted.
 */
public interface HopScotchSystem<D> {

    /**
     * Add data to the pipeline
     *
     * @param data The new date
     */
    void add(D data);

    /**
     * Retrieve a Statistics object to see the current snapshot of the status of the pipeline
     *
     * @return The Statistics of the pipeline at the time.
     */
    Statistics getStatistics();

    /**
     * Awaits the processing of all added data.
     *
     * @return {@code true}
     */
    boolean awaitFinish();

    /**
     * Awaits the processing of all added data or returns after the given timeout.
     *
     * @return {@code true} if the data was finished before the timeout
     */
    boolean awaitFinish(Duration timeout);

}
