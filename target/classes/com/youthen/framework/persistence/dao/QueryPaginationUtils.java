package com.youthen.framework.persistence.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * @copyright
 * @Revision
 * @date 2010-3-26
 */
public class QueryPaginationUtils {

    /**
     * decide pagination information
     * 
     * @param query
     * @param currentPage
     * @param maxNum
     * @param goTo
     * @return
     */
    public static Query doPagination(final Query query, final String currentPage, String maxNum, final String goTo) {

        if (StringUtils.isEmpty(maxNum)) {
            maxNum = "0";
        }

        checkIllegal(currentPage, maxNum, goTo);
        query.setMaxResults(Integer.valueOf(maxNum));

        if (StringUtils.isNotEmpty(currentPage)) {
            query.setFirstResult((Integer.valueOf(currentPage) - 1) * Integer.valueOf(maxNum));
        } else {
            query.setFirstResult((Integer.valueOf(goTo) - 1) * Integer.valueOf(maxNum));
        }
        return query;
    }

    /**
     * @param query
     * @param currentPage
     * @param maxNum
     * @return
     */
    public static Query doPagination(final Query query, final int currentPage, final int maxNum) {
        if (currentPage < 0 || maxNum < 0) {
            throw new RuntimeException("curreant page or max number can be invegative!");
        }
        query.setMaxResults(maxNum);
        query.setFirstResult((currentPage - 1) * maxNum);
        return query;
    }

    /**
     * @param currentPage
     * @param maxNum
     * @param goTo
     */
    private static void checkIllegal(final String currentPage, String maxNum, final String goTo) {
        if (StringUtils.isEmpty(maxNum)) {
            maxNum = "1";
        }
        if (StringUtils.isEmpty(currentPage) && StringUtils.isEmpty(goTo)) {
            throw new RuntimeException("Pagination number null value error!");
        }

        if ((StringUtils.isEmpty(currentPage) && Integer.valueOf(goTo) < 0) || (StringUtils.isEmpty(goTo) &&
                Integer.valueOf(currentPage) < 0) || Integer.valueOf(maxNum) < 1) {
            throw new RuntimeException("Pagination number null value error!");
        }
    }

    /**
     * @param criteria
     * @param currentPage
     * @param maxNum
     * @param goTo
     * @return
     */
    public static Criteria doPagination(final Criteria criteria, final String currentPage, String maxNum,
            final String goTo) {
        if (StringUtils.isEmpty(maxNum)) {
            maxNum = "1";
        }

        checkIllegal(currentPage, maxNum, goTo);
        criteria.setMaxResults(Integer.valueOf(maxNum));

        if (StringUtils.isNotEmpty(currentPage)) {
            criteria.setFirstResult((Integer.valueOf(currentPage) - 1) * Integer.valueOf(maxNum));
        } else {
            criteria.setFirstResult((Integer.valueOf(goTo) - 1) * Integer.valueOf(maxNum));
        }
        return criteria;
    }

}
