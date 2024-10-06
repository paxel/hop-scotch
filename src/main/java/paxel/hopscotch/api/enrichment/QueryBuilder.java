package paxel.hopscotch.api.enrichment;

/**
 * The start of the Query of the
 */
public interface QueryBuilder {

    KeyQueryBuilder queryKey();

    StageQueryBuilder queryStage();

    CreatorQueryBuilder queryCreator();

    ValueQueryBuilder queryValue();

}
