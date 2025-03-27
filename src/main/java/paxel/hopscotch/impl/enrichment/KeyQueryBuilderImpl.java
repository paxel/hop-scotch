package paxel.hopscotch.impl.enrichment;

import paxel.hopscotch.api.enrichment.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implements the {@link KeyQueryBuilder} which reduces the available enrichments depending on the Creator.
 */
public class KeyQueryBuilderImpl implements KeyQueryBuilder {

    private final List<EnrichmentImpl> enrichments;

    /**
     * Constructs an instance with the remaining {@link Enrichment}s
     *
     * @param enrichments The remaining enrichments.
     */
    public KeyQueryBuilderImpl(List<EnrichmentImpl> enrichments) {
        this.enrichments = List.copyOf(enrichments);
    }

    @Override
    public KeyQueryBuilder matchExact(String key) {
        return new KeyQueryBuilderImpl(filter(enrichment -> enrichment.key().equals(KeyFactory.forString(key))).toList());
    }

    @Override
    public KeyQueryBuilder matchExact(Key key) {
        return new KeyQueryBuilderImpl(filter(enrichment -> enrichment.key().equals(key)).toList());
    }

    @Override
    public KeyQueryBuilder matchExact(String... path) {
        return new KeyQueryBuilderImpl(filter(enrichment -> enrichment.key().equals(KeyFactory.path(path))).toList());
    }

    @Override
    public KeyQueryBuilder matchExact(Collection<String> path) {
        return new KeyQueryBuilderImpl(filter(enrichment -> enrichment.key().equals(KeyFactory.collection(path))).toList());
    }

    @Override
    public KeyQueryBuilder containsAll(String... subPaths) {
        return new KeyQueryBuilderImpl(filter(enrichment -> {
            for (String subPath : subPaths) {
                if (!enrichment.key().asCollection().contains(subPath)) {
                    return false;
                }
            }
            return true;
        }).toList());
    }

    @Override
    public KeyQueryBuilder containsAny(String... subPaths) {
        return new KeyQueryBuilderImpl(filter(enrichment -> {
            for (String subPath : subPaths) {
                if (!enrichment.key().asCollection().contains(subPath)) {
                    return true;
                }
            }
            return false;
        }).toList());
    }

    @Override
    public KeyQueryBuilder containsInOrder(String... subPaths) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public KeyQueryBuilder matchRegex(String regex) {
        return new KeyQueryBuilderImpl(filterRegex(Pattern.compile(regex)).collect(Collectors.toList()));
    }

    @Override
    public Stream<Enrichment> stream() {
        return enrichments.stream().map(Function.identity());
    }

    private Stream<EnrichmentImpl> filterRegex(Pattern pattern) {
        return filter(enrichment -> pattern.matcher(enrichment.key().toString()).matches());
    }


    private Stream<EnrichmentImpl> filter(Predicate<EnrichmentImpl> predicate) {
        return enrichments.stream().filter(predicate);
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

}
