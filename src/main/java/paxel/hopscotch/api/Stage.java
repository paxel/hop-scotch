package paxel.hopscotch.api;

/**
 * Defines a Stage. Stages are processed in incrementing number order.
 *
 * @param number the number of the stage
 */
public record Stage(int number, String name) {
}
