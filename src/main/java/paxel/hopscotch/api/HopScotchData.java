package paxel.hopscotch.api;


import java.util.Properties;

public interface HopScotchData<D> {

    D getData();

    Properties getEnrichements();

}
