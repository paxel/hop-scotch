package paxel.hopscotch.impl.data;

import paxel.hopscotch.impl.enrichment.EnrichmentImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * The Container for the original data and the enrichments.
 *
 * @param <D> The data type
 */
public class HopScotchEnrichedData<D> {
    private final D data;
    private final UUID uuid;
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance.
     *
     * @param data The data
     */
    public HopScotchEnrichedData(D data) {
        this(data, new ArrayList<>(), UUID.randomUUID());
    }

    /**
     * Constructs an instance with existing enrichments.
     *
     * @param data        The data
     * @param enrichments The existing enrichments
     * @param uuid        The existing uuid
     */
    public HopScotchEnrichedData(D data, List<EnrichmentImpl> enrichments, UUID uuid) {
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

    /**
     * Create a mutable copy of this data.
     *
     * @return a copy of this data
     */
    public HopScotchEnrichedData<D> copy() {

        // For the future: make an immutable instance of the data.
        return new HopScotchEnrichedData<>(data, new ArrayList<>(enrichments), uuid);
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

    /**
     * Retrieve the unique ID
     *
     * @return The unique ID of this data
     */
    public UUID getId() {
        return uuid;
    }
}
