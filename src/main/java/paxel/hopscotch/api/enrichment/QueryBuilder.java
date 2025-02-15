package paxel.hopscotch.api.enrichment;

/**
 * The main QueryBuilder
 */
public interface QueryBuilder {

    /**
     * @return a query builder for keys
     */
    KeyQueryBuilder queryKey();

    /**
     * @return return a query builder for stages
     */
    StageQueryBuilder queryStage();

    /**
     * @return a query builder for creators
     */
    CreatorQueryBuilder queryCreator();

    /**
     * @return a query builder for values
     */
    ValueQueryBuilder queryValue();

}
