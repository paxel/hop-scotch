package paxel.hopscotch.api;


import paxel.hopscotch.api.enrichment.KeyBuilder;
import paxel.hopscotch.api.enrichment.QueryBuilder;


public interface HopScotchData<D> {

    D getData();

    KeyBuilder startEnrichment();

    QueryBuilder startEnrichmentQuery();

}
