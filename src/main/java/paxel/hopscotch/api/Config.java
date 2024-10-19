package paxel.hopscotch.api;

/**
 * The configuration for the framework running the pipeline
 *
 * @param backPressure This is the number of messages queued to a stage that cause the stage processor to wait until there are fewer messages in the queue.
 */
public record Config(int backPressure) {
}
