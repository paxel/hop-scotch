package paxel.hopscotch.api;

/**
 * The result of a Judge. If the Judgement is accepted, a new Hop can be created with the Meta.
 */
public interface Judgement {
    boolean isAccepted();

    Object getMeta();
}
