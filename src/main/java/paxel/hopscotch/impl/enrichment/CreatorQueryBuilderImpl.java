package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.enrichment.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Implements the {@link CreatorQueryBuilder} which reduces the available enrichments depending on the Creator.
 */
public class CreatorQueryBuilderImpl implements CreatorQueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with the remaining {@link Enrichment}s
     *
     * @param enrichments The remaining enrichments.
     */
    public CreatorQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }

    @Override
    public CreatorQueryBuilder matchExact(Creator creator) {
        return new CreatorQueryBuilderImpl(enrichments.stream().filter(e -> e.creator().equals(creator)).toList());
    }

    @Override
    public Stream<Enrichment> stream() {
        return enrichments.stream().map(Function.identity());
    }

    @Override
    public StageQueryBuilder queryStage() {
        return new StageQueryBuilderImpl(enrichments);
    }

    @Override
    public ValueQueryBuilder queryValue() {
        return new ValueQueryBuilderImpl(enrichments);
    }

    @Override
    public CreatorQueryBuilder queryCreator() {
        return new CreatorQueryBuilderImpl(enrichments);
    }

    @Override
    public KeyQueryBuilder queryKey() {
        return new KeyQueryBuilderImpl(enrichments);
    }
}
