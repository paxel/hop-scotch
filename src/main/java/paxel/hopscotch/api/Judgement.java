package paxel.hopscotch.api;

/**
 * The result of a Judge. If the Judgement is accepted, a new Hop can be created with the Meta.
 *
 * @param <M> The Meta data.
 */
public interface Judgement<M> {
    boolean isAccepted();

    M getMeta();
}
