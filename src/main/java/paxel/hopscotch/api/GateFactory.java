package paxel.hopscotch.api;

public interface GateFactory<D> {
    int getStage();

    Gate<D> createGate();

}
