package paxel.hopscotch.impl.data;

import java.util.ArrayList;
import java.util.List;

public class HopScotchDataInternal<D> {
    private final D data;

    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with existing enrichments.
     * @param data The data
     * @param enrichments The existing enrichments
     */
    public HopScotchDataInternal(D data, List<EnrichmentImpl> enrichments) {
        this.data = data;
        this.enrichments = enrichments;
    }

    /**
     * Constructs an instance.
     * @param data The data
     */
    public HopScotchDataInternal(D data) {
        this.data = data;
        this.enrichments = new ArrayList<>();
    }

    /**
     * Retrieve the data
     * @return the data
     */
    public D getData() {
        return data;
    }

    public HopScotchDataInternal<D> copy() {

        // For the future: make an immutable instance of the data.
        return new HopScotchDataInternal<>(data, new ArrayList<>(enrichments));
    }

    /**
     * Adds a new Enrichment to the list
     *
     * @param enrichment the new Enrichment
     */
    public void add(EnrichmentImpl enrichment) {
        enrichments.add(enrichment);
    }

    /**
     * Provides a view on the existing list.
     *
     * @return current added enrichments
     */
    public List<EnrichmentImpl> getEnrichments() {
        return List.copyOf(enrichments);
    }
}
