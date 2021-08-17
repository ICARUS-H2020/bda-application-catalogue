package it.eng.alidalab.applicationcatalogue.util;

import it.eng.alidalab.applicationcatalogue.domain.AccessLevel;

import javax.persistence.Query;

public class QueryUtils {
    public static Query createQueryWithAccesLevel(Query query, String ownerId, String organizationId){

        query.setParameter("team", AccessLevel.ACCESS_LEVEL.TEAM);
        query.setParameter("public", AccessLevel.ACCESS_LEVEL.PUBLIC);
        query.setParameter("private", AccessLevel.ACCESS_LEVEL.PRIVATE);
        query.setParameter("ownerId", ownerId);
        query.setParameter("organizationId", organizationId);
        return query;
    }
}
