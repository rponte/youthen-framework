package com.youthen.framework.persistence.dao;

import java.util.Collection;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

/**
 * The based detached hibernate criteria builder class usage:
 * Sample:
 * {
 * DetachedCriteriaBuilder builder =DetachedCriteriaBuilder.instance(SomeModel.class);
 * builder.addIn("branchCode", branchCodes).addIn("subBranchCode",subBranchCodes);
 * builder.addGtOrEq("applyDate", start).addLtOrEq("applyDate", end);
 * return builder.getDetachedCriteria();
 * }
 * 
 * @Revision
 * @date 2010-3-31
 */
public class DetachedCriteriaBuilder {

    /**
     * detached criteria
     */
    private final DetachedCriteria detachedCriteria;

    /**
     * @param detachedCriteria
     */
    private DetachedCriteriaBuilder(final DetachedCriteria detachedCriteria) {
        this.detachedCriteria = detachedCriteria;
    }

    /**
     * @param detachedCriteria
     * @return
     */
    public static DetachedCriteriaBuilder instance(final DetachedCriteria detachedCriteria) {
        return new DetachedCriteriaBuilder(detachedCriteria);
    }

    /**
     * @param detachedCriteria
     * @return
     */
    public static DetachedCriteriaBuilder instance(final Class<?> clazz) {
        return new DetachedCriteriaBuilder(DetachedCriteria.forClass(clazz));
    }

    /**
     * add equal condition
     * 
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addEq(final String property, final Object value) {
        if (null == value) {
            return this;
        }

        if (String.class.equals(value.getClass()) && StringUtils.isEmpty(String.valueOf(value).trim())) {
            return this;
        }

        if (Long.class.equals(value.getClass()) && Long.valueOf(String.valueOf(value).trim()) == 0L) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).eq(value));
        return this;
    }

    /**
     * add less-than condition
     * 
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addLt(final String property, final Object value) {
        if (null == value) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).lt(value));
        return this;
    }

    /**
     * @param property
     * @return
     */
    public DetachedCriteriaBuilder setGetProperty(final String property) {
        this.detachedCriteria.setProjection(Projections.property(property));
        return this;
    }

    /**
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addLtOrEq(final String property, final Object value) {
        if (null == value) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).le(value));
        return this;
    }

    /**
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addGt(final String property, final Object value) {
        if (null == value) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).gt(value));
        return this;
    }

    public DetachedCriteriaBuilder addGtOrEq(final String property, final Object value) {
        if (null == value) {
            return this;
        }
        this.detachedCriteria.add(Property.forName(property).ge(value));
        return this;
    }

    /**
     * add between condition
     * 
     * @param property
     * @param start
     * @param end
     * @return
     */
    public DetachedCriteriaBuilder addBetween(final String property, final Object start, final Object end) {
        if (null == start || null == end) {
            return this;
        }
        this.detachedCriteria.add(Property.forName(property).between(start, end));
        return this;
    }

    /**
     * left join in
     * 
     * @param property
     * @param alias
     * @return
     */
    public DetachedCriteriaBuilder leftJoin(final String property, final String alias) {
        this.detachedCriteria.createAlias(property, alias, CriteriaSpecification.LEFT_JOIN);
        return this;
    }

    /**
     * inner join in
     * 
     * @param property
     * @param alias
     * @return
     */
    public DetachedCriteriaBuilder innerJoin(final String property, final String alias) {
        this.detachedCriteria.createAlias(property, alias, CriteriaSpecification.INNER_JOIN);
        return this;
    }

    /**
     * @param property
     * @param alias
     * @return
     */
    public DetachedCriteriaBuilder createAliasInnerJoin(final String property, final String alias) {
        this.detachedCriteria.createAlias(property, alias);
        return this;
    }

    /**
     * @param property
     * @param values
     * @return
     */
    public DetachedCriteriaBuilder addIn(final String property, final Object[] values) {
        if (null == values) {
            return this;
        }
        if (ArrayUtils.isEmpty(values)) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).in(values));
        return this;
    }

    /**
     * @param property
     * @param values
     * @return
     */
    public DetachedCriteriaBuilder addIn(final String property, final Collection<?> values) {

        if (CollectionUtils.isEmpty(values)) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).in(values));
        return this;
    }

    /**
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addNotEq(final String property, final Object value) {
        if (null == value) {
            return this;
        }
        String toValue = null;

        if (String.class.equals(value.getClass())) {
            toValue = ((String) value).trim();
        }

        if (String.class.equals(value.getClass()) && StringUtils.isBlank(toValue)) {
            return this;
        }

        this.detachedCriteria.add(Property.forName(property).ne(toValue == null ? value : toValue));
        return this;
    }

    /**
     * @param property
     * @return
     */
    public DetachedCriteriaBuilder addIsNull(final String property) {
        this.detachedCriteria.add(Property.forName(property).isNull());
        return this;
    }

    /**
     * @param property
     * @return
     */
    public DetachedCriteriaBuilder addIsNotNull(final String property) {
        this.detachedCriteria.add(Property.forName(property).isNotNull());
        return this;
    }

    /**
     * @param property
     * @return
     */
    public DetachedCriteriaBuilder addOrderByAsc(final String property) {
        this.detachedCriteria.addOrder(Order.asc(property));
        return this;
    }

    /**
     * @param property
     * @return
     */
    public DetachedCriteriaBuilder addOrderByDesc(final String property) {
        this.detachedCriteria.addOrder(Order.desc(property));
        return this;
    }

    /**
     * @param property
     * @param value
     * @return
     */
    public DetachedCriteriaBuilder addLikeAny(final String property, final String value) {
        if (StringUtils.isNotBlank(value)) {
            this.detachedCriteria.add(Property.forName(property).like(value.trim(), MatchMode.ANYWHERE));
        }
        return this;
    }

    /**
     * @param property
     * @param lValue
     * @param rValue
     * @return
     */
    public DetachedCriteriaBuilder addOr(final String property, final Object lValue, final Object rValue) {
        this.detachedCriteria
                .add(Restrictions.or(Restrictions.eq(property, lValue), Restrictions.eq(property, rValue)));
        return this;
    }

    /**
     * @return the detachedCriteria
     */
    public DetachedCriteria getDetachedCriteria() {
        return this.detachedCriteria;
    }

}
