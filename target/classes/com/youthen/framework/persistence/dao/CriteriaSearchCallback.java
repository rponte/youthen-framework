package com.youthen.framework.persistence.dao;

import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @copyright
 * @author William
 * @Revision
 * @date 2010-3-30
 */
public class CriteriaSearchCallback implements HibernateCallback {

    private boolean needPagination = false;

    private boolean singleValue = false;

    private String currentPage = "";

    private String maxNum = "";

    private String goTo = "";

    private DetachedCriteria criteria;

    @Override
    public Object doInHibernate(final Session session) throws HibernateException, SQLException {

        if (this.singleValue) {
            return this.criteria.getExecutableCriteria(session).uniqueResult();
        }

        if (!this.needPagination || (StringUtils.isEmpty(this.currentPage) && StringUtils.isEmpty(this.goTo))) {
            return this.criteria.getExecutableCriteria(session).list();
        } else {
            return QueryPaginationUtils.doPagination(this.criteria.getExecutableCriteria(session), this.currentPage,
                    this.maxNum, this.goTo).list();
        }

    }

    /**
     * @param criteria
     */
    public CriteriaSearchCallback(final DetachedCriteria criteria) {
        this.criteria = criteria;
    }

    public CriteriaSearchCallback() {

    }

    /**
     * @param criteria
     *            the criteria to set
     */
    public CriteriaSearchCallback setCriteria(final DetachedCriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public CriteriaSearchCallback setCurrentPage(final String currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    /**
     * @param maxNum
     *            the maxNum to set
     */
    public CriteriaSearchCallback setMaxNum(final String maxNum) {
        this.maxNum = maxNum;
        return this;
    }

    /**
     * @param goTo
     *            the goTo to set
     */
    public CriteriaSearchCallback setGoTo(final String goTo) {
        this.goTo = goTo;
        return this;
    }

    /**
	 * 
	 */
    public CriteriaSearchCallback doPagination() {
        this.needPagination = true;
        return this;
    }

    /**
     * @return
     */
    public CriteriaSearchCallback toReturnSingleValue() {
        this.singleValue = true;
        return this;
    }

}
