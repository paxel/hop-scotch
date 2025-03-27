package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.Stage;
import paxel.hopscotch.api.enrichment.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Implements the {@link StageQueryBuilder} which reduces the available enrichments depending on the Creator.
 */
public class StageQueryBuilderImpl implements StageQueryBuilder {
    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with the remaining {@link Enrichment}s
     *
     * @param enrichments The remaining enrichments.
     */
    public StageQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = enrichments;
    }

    @Override
    public StageQueryBuilder matchExact(Stage stage) {
        return new StageQueryBuilderImpl(enrichments.stream().filter(e -> e.stage().equals(stage)).toList());
    }

    @Override
    public Stream<Enrichment> stream() {
        return enrichments.stream().map(Function.identity());
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
