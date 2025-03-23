package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.Value;
import paxel.hopscotch.api.enrichment.ValueQueryBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class ValueQueryBuilderImpl implements ValueQueryBuilder {
    /**
     * @param enrichments
     */
    public ValueQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
    }

    @Override
    public ValueQueryBuilder matchType(Value.Type type) {
        return null;
    }

    @Override
    public Optional<Integer> anyInteger() {
        return Optional.empty();
    }

    @Override
    public Collection<Integer> allInteger() {
        return List.of();
    }
}
