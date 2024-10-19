package paxel.hopscotch.impl.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class HopScotchDataInternal<D> {
    private final D data;
    private final UUID uuid;
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance.
     *
     * @param data The data
     */
    public HopScotchDataInternal(D data) {
        this(data, new ArrayList<>(), UUID.randomUUID());
    }

    /**
     * Constructs an instance with existing enrichments.
     *
     * @param data        The data
     * @param enrichments The existing enrichments
     * @param uuid        The existing uuid
     */
    public HopScotchDataInternal(D data, List<EnrichmentImpl> enrichments, UUID uuid) {
        this.data = requireNonNull(data);
        this.enrichments = requireNonNull(enrichments);
        this.uuid = requireNonNull(uuid);
    }


    /**
     * Retrieve the data
     *
     * @return the data
     */
    public D getData() {
        return data;
    }

    public HopScotchDataInternal<D> copy() {

        // For the future: make an immutable instance of the data.
        return new HopScotchDataInternal<>(data, new ArrayList<>(enrichments), uuid);
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

    public UUID getId() {
        return uuid;
    }
}
