package paxel.hopscotch.api;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * The Interface of the Pipeline for the user.
 *
 * @param <D> The type of the Data.
 *            It should be immutable or not be muted.
 */
public interface HopScotchSystem<D> {

    /**
     * Add data to the pipeline
     *
     * @param data The new date
     */
    void add(D data);

    /**
     * Retrieve a Statistics object to see the current snapshot describing the status and progress of the pipeline
     *
     * @return The Statistics of the pipeline at the time.
     * @throws InterruptedException In case the Thread was interrupted.
     * @throws ExecutionException   In case the process encountered an error, it is wrapped as cause in this exception
     */
    Statistics getStatistics() throws ExecutionException, InterruptedException;

    /**
     * Awaits the processing of all added data.
     *
     * @return {@code true}
     */
    boolean awaitFinish();

    /**
     * Awaits the processing of all added data or returns after the given timeout.
     *
     * @param timeout The duration to wait for the result before returning false.
     * @return {@code true} if the data was finished before the timeout
     */
    boolean awaitFinish(Duration timeout);

}
