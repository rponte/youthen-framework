package com.youthen.framework.persistence.dao.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.SimpleAppMessage;
import com.youthen.framework.common.context.SessionContext;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;
import com.youthen.framework.common.exception.OptimisticLockStolenException;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.persistence.dao.EntityDao;
import com.youthen.framework.persistence.entity.CommonEntity;
import com.youthen.framework.persistence.entity.QueryAudittrail;
import com.youthen.framework.util.CommonEntityUtils;
import com.youthen.framework.util.CommonUtils;

/**
 * DAO基类
 * 
 * @copyright
 * @author LiXin
 * @param <E>
 * @Revision
 * @date 2014-7-14
 */
public abstract class EntityDaoImpl<E extends CommonEntity> extends HibernateDaoSupport implements EntityDao<E> {

    public Class<E> clazz;

    private Map<String, Object> paramMap;

    @Autowired
    public void setup(@Qualifier("sessionFactory") final SessionFactory aSessionFactory) {
        this.setSessionFactory(aSessionFactory);
    }

    public void setType(final Class<E> aClazz) {
        this.clazz = aClazz;
    }

    public abstract void injectType();

    @Override
    public Serializable insert(final E aEntity) throws DuplicateKeyException {
        if (aEntity == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002");
            throw new ApplicationException(message);
        }
        CommonEntityUtils.initializeCommonFields(aEntity);

        try {
            return getHibernateTemplate().save(aEntity);
        } catch (final DataAccessException e) {
            e.printStackTrace();
            final AppMessage message = new FieldSupportedMessage("EFW52002").format(aEntity);
            throw new DuplicateKeyException(message, e);
        }
    }

    @Override
    public Serializable specialInsert(final E aEntity) throws DuplicateKeyException {
        if (aEntity == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002");
            throw new ApplicationException(message);
        }
        try {
            aEntity.setVersionNo(0L);
            aEntity.setUpdTime(CommonEntityUtils.getSystime());
            return getHibernateTemplate().save(aEntity);
        } catch (final org.springframework.dao.DuplicateKeyException e) {
            final AppMessage message = new FieldSupportedMessage("EFW52002").format(aEntity);
            throw new DuplicateKeyException(message, e);
        }
    }

    /**
     * @see com.youthen.framework.persistence.dao.EntityDao#bulkInsert(java.lang.Iterable)
     */

    @Override
    public void bulkInsert(final Iterable<E> aEntityList) throws DuplicateKeyException {
        for (final E entity : aEntityList) {
            this.insert(entity);
        }
    }

    /**
     * @see com.youthen.framework.persistence.dao.EntityDao#bulkInsert(java.lang.Iterable)
     */

    @Override
    public void bulkSpecialInsert(final Iterable<E> aEntityList) throws DuplicateKeyException {
        for (final E entity : aEntityList) {
            this.specialInsert(entity);
        }
    }

    @Override
    public void delete(final E aEntity) throws ObjectNotFoundException, OptimisticLockStolenException {

        if (aEntity == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002");
            throw new ApplicationException(message);
        }
        this.checkVersionno(aEntity);

        getHibernateTemplate().delete(aEntity);
    }

    /**
     * @see com.youthen.framework.persistence.dao.EntityDao#bulkDelete(java.lang.Iterable)
     */

    @Override
    public void bulkDelete(final Iterable<E> aEntityList) throws ObjectNotFoundException,
            OptimisticLockStolenException {
        for (final E entity : aEntityList) {
            this.delete(entity);
        }
    }

    @Override
    public void update(final E aEntity) {

        if (aEntity == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002");
            throw new ApplicationException(message);
        }

        try {
            this.checkVersionno(aEntity);
            CommonEntityUtils.updateCommonFields(aEntity);
            getHibernateTemplate().update(aEntity);
        } catch (final DataAccessException e) {
            final AppMessage message = new FieldSupportedMessage("EFW52002").format(aEntity);
            throw new ApplicationException(message);
        } catch (final ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (final OptimisticLockStolenException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void specialUpdate(final E aEntity) throws DuplicateKeyException {

        if (aEntity == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002");
            throw new ApplicationException(message);
        }

        try {
            /*
             * aEntity.setUpdTime(CommonEntityUtils.getSystime());
             * if (aEntity.getVersionNo() != null) {
             * aEntity.setVersionNo(aEntity.getVersionNo() + 1);
             * }
             */
            getHibernateTemplate().update(aEntity);
        } catch (final org.springframework.dao.DuplicateKeyException e) {
            final AppMessage message = new FieldSupportedMessage("EFW52002").format(aEntity);
            throw new DuplicateKeyException(message, e);
        }

    }

    /**
     * @see com.youthen.framework.persistence.dao.EntityDao#bulkUpdate(java.lang.Iterable)
     */

    @Override
    public void bulkUpdate(final Iterable<E> aEntityList) throws DuplicateKeyException {
        for (final E entity : aEntityList) {
            this.update(entity);
        }
    }

    @Override
    public List<E> selectAll() throws ObjectNotFoundException {
        List<E> all = new ArrayList<E>();
        all = getHibernateTemplate().loadAll(this.clazz);
        return all;
    }

    @Override
    public E getById(final Serializable id) {
        if (id == null) {
            final AppMessage message = new SimpleAppMessage("XFW52002").format(id);
            throw new ApplicationException(message);
        }
        return getHibernateTemplate().get(this.clazz, id);
    }

    @Override
    public E lock(final Object id) throws ObjectNotFoundException {
        final E result = getById((Serializable) id);
        if (result == null) {
            final AppMessage message = new FieldSupportedMessage("EFW52001").format(id);
            throw new ObjectNotFoundException(message);
        }
        return result;
    }

    /**
     * 。
     * 
     * @param aEntity
     * @throws ObjectNotFoundException
     * @throws OptimisticLockStolenException
     */
    protected void checkVersionno(final E aEntity) throws ObjectNotFoundException, OptimisticLockStolenException {
        final E result = this.lock(aEntity.getId());
        final Long versionnoOld = aEntity.getVersionNo();
        final Long versionnoNew = result.getVersionNo();
        if (versionnoOld == null || versionnoNew == null) {
            throw new ApplicationException("XFW52003", aEntity.getClass().getName());
        }
        if (versionnoOld.longValue() != versionnoNew.longValue()) {
            final AppMessage message = new FieldSupportedMessage("EFW52003").format(aEntity);
            throw new OptimisticLockStolenException(message);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getByHql(final String queryString) {
        return getSession().createQuery(queryString).list();
    }

    @SuppressWarnings("unchecked")
    public List<E> getByHql(final String hql, final Object[] paramValue) {
        final List<E> list = getHibernateTemplate().find(hql, paramValue);
        return list;
    }

    public int excuteByHql(final String hql, final Object... values) throws Exception {

        return getHibernateTemplate().bulkUpdate(hql, values);
    }

    public String getPropertyHqlByEntity(final Object targetObj, String whereHql,
            final String prefix,
            final Map<String, Object> paramMap, final String[] excludeParams)
            throws Exception {
        final Class<?> tmpClazz = targetObj.getClass();
        final PropertyDescriptor[] Pds = BeanUtils.getPropertyDescriptors(tmpClazz);
        for (final PropertyDescriptor pd : Pds) {
            if (pd.getWriteMethod() == null || pd.getReadMethod() == null || pd.getName().equals("versionNo")) {
                continue;
            }
            final Method readMethod = pd.getReadMethod();
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            final Object value = readMethod.invoke(targetObj);
            if (value != null && !value.getClass().isArray() && !Collection.class.isAssignableFrom(value.getClass())) {
                if (excludeParams != null) {
                    boolean unWhereParam = false;
                    for (int i = 0; i < excludeParams.length; i++) {
                        if (excludeParams[i].equals(pd.getName())) {
                            unWhereParam = true;
                            break;
                        }
                    }
                    if (unWhereParam) continue;
                }
                final String type = pd.getPropertyType().getName();
                if (type.equals("java.lang.Long") || type.equals("java.lang.Integer") || type.equals("int")
                        || type.equals("long") && value.toString().equals("0")) {
                    continue;
                }
                final String tmpPropertyName = prefix + pd.getName();
                if (pd.getPropertyType().getPackage().getName().startsWith("com.youthen")) {
                    whereHql += getPropertyHqlByEntity(value, whereHql, tmpPropertyName + ".", paramMap, excludeParams);
                } else {
                    final String tmpParamName = tmpPropertyName.replace(".", "_");
                    whereHql += " and " + tmpPropertyName + "=:" + tmpParamName;
                    paramMap.put(tmpParamName, value);
                }
            }
        }
        return whereHql;
    }

    /**
     * @param e 根据e中所有非空字段查询
     * @param whereHql 根据e中所有非空字段无法查询的，自己拼查询条件
     * @param excludeParams e中为非空字段(比如有默认值)，但不作为查询条件的字段名称
     * @throws Exception
     */
    @Override
    public List<E> getByEntity(final E aE, final String whereHql, final String[] excludeParams) throws Exception {
        final Map<String, Object> tmpParamMap = new HashMap<String, Object>();
        final StringBuffer hqlBuf = new StringBuffer("from " + this.clazz.getName() + " where 1=1");
        hqlBuf.append(getPropertyHqlByEntity(aE, "", "", tmpParamMap, excludeParams));
        if (StringUtils.isNotEmpty(whereHql)) {
            hqlBuf.append(" and " + whereHql);
        }
        System.out.println("=========================" + hqlBuf.toString() + "==============");
        final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        final Query query = session.createQuery(hqlBuf.toString());
        for (final String tmpKey : tmpParamMap.keySet()) {
            query.setParameter(tmpKey, tmpParamMap.get(tmpKey));
        }
        return query.list();
    }

    private void updateQueryIdSeq() {
        final Date now = new Date();
        final String seq = DateFormatUtils.format(now, "yyyyMMdd");
        final String sql =
                "UPDATE OS_RECORDER_TRANX SET CURRENT_NUM= CURRENT_NUM+1 WHERE TRANX_NAME='" + seq + "'";
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        query.executeUpdate();

    }

    private Long getQueryIdSeq() {
        String curNum = null;
        final Date now = new Date();
        final String seq = DateFormatUtils.format(now, "yyyyMMdd");
        String sql = "SELECT CURRENT_NUM FROM OS_RECORDER_TRANX where TRANX_NAME='"
                + seq + "'";
        final Query query = getSession().createSQLQuery(sql);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        final List<Map<String, Object>> list = query.list();
        final DecimalFormat df = new DecimalFormat("0000");
        if (CollectionUtils.isNotEmpty(list)) {
            final Map obj = list.get(0);
            if (obj != null && obj.get("CURRENT_NUM") != null) {
                curNum = df.format(obj.get("CURRENT_NUM"));
            }
        } else {

            String dataBaseName;
            try {
                dataBaseName = getSession().connection().getMetaData().getDatabaseProductName();
                if ("ORACLE".equalsIgnoreCase(dataBaseName)) {
                    sql =
                            "INSERT INTO OS_RECORDER_TRANX ( ID, TRANX_NAME, CURRENT_NUM, NUM_LEN, UPDATER_ID,VERSION_NO)  VALUES (OS_RECORDER_TRANX_SEQ.nextval,'"
                                    + seq + "', '1', '4','" + SessionContext.getUser().getUserId() + "', '0')";
                } else {
                    sql =
                            "INSERT INTO OS_RECORDER_TRANX (   TRANX_NAME, CURRENT_NUM, NUM_LEN, UPDATER_ID,VERSION_NO)  VALUES ('"
                                    + seq + "', '1', '4','" + SessionContext.getUser().getUserId() + "', '0')";
                }
            } catch (final DataAccessResourceFailureException e) {
                e.printStackTrace();
            } catch (final HibernateException e) {
                e.printStackTrace();
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            } catch (final SQLException e) {
                e.printStackTrace();
            }

            final SQLQuery query2 = this.getSession().createSQLQuery(sql);
            query2.executeUpdate();
            curNum = df.format(1);
        }

        return Long.valueOf(seq + curNum);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageBean<E> getByPageBean(final PageBean<E> pageBean) throws Exception {
        final Map<String, String> queryMap = new HashMap<String, String>();

        // 获取主键名
        String countField = null;
        final Field[] fields = this.clazz.getDeclaredFields();
        for (final Field field : fields) {
            if (!field.getName().equals("serialVersionUID")) {
                countField = field.getName();
                break;
            }
        }
        // 查询条件所在map
        final Map<String, String> whereMap = pageBean.getWhereMap();
        // 查询总数量hql
        final StringBuffer countHql =
                new StringBuffer("select count(" + countField + ") from " + this.clazz.getName()
                        + " tmpObj " + (pageBean.getFromHql() == null ? "" : pageBean.getFromHql()) + " where 1=1 "
                        + pageBean.getWhereHql());
        // 查询实体hql
        final StringBuffer hql =
                new StringBuffer("from " + this.clazz.getName() + " tmpObj "
                        + (pageBean.getFromHql() == null ? "" : pageBean.getFromHql()) + " where 1=1 "
                        + pageBean.getWhereHql());
        // 查询条件hql
        final StringBuffer whereHql = new StringBuffer();
        // 组HQL字符串
        for (final String key : whereMap.keySet()) {
            final String[] keyArray = key.split("_");
            if (keyArray.length > 1 && pageBean.getOperatorMap().get(keyArray[0]) != null
                    && StringUtils.isNotEmpty(whereMap.get(key))) {
                whereHql.append(" and tmpObj."
                        + key.replace(keyArray[0] + "_", "")
                        + " "
                        + pageBean.getOperatorMap().get(keyArray[0]) + ":" + key.replaceAll("\\.", ""));
                queryMap.put(key.replace(keyArray[0] + "_", ""), whereMap.get(key));
            }
        }
        countHql.append(whereHql);
        // 排序
        if (StringUtils.isNotEmpty(pageBean.getSortColumnName())) {
            if (StringUtils.isEmpty(pageBean.getAsc())) pageBean.setAsc("asc");
            whereHql.append(" order by " + pageBean.getSortColumnName());
            if (pageBean.getAsc().toLowerCase().equals("true") || pageBean.getAsc().toLowerCase().equals("asc")) {
                whereHql.append(" ASC");
            }
            if (pageBean.getAsc().toLowerCase().equals("false") || pageBean.getAsc().toLowerCase().equals("desc")) {
                whereHql.append(" DESC");
            }
            // 防止数据库中SortColumn重复导致出现重复数据，漏数据
            whereHql.append(",id DESC");
        }
        hql.append(whereHql);

        final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        final Query query = session.createQuery(hql.toString());
        final Query countQuery = session.createQuery(countHql.toString());

        // 参数类型转换
        this.setParamMap(whereMap);
        // 给查询条件放值
        for (final String key : whereMap.keySet()) {
            final String[] keyArray = key.split("_");
            if (keyArray.length > 1 && pageBean.getOperatorMap().get(keyArray[0]) != null
                    && StringUtils.isNotEmpty(whereMap.get(key))) {
                final Object paramObj = this.paramMap.get(key);
                query.setParameter(key.replaceAll("\\.", ""), paramObj);
                countQuery.setParameter(key.replaceAll("\\.", ""), paramObj);
            }
        }
        final int pageSize = pageBean.getPageSize().intValue();
        if (pageSize < 1000) {
            query.setFirstResult((pageBean.getCurrent().intValue() - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        final List list = countQuery.list();
        if (!list.isEmpty()) {
            pageBean.setRecordCount(new Integer(list.get(0).toString()));
            pageBean.setRecordList(query.list());
        }

        final QueryAudittrail queryAudittrail = new QueryAudittrail();
        queryAudittrail.setUserId(CommonEntityUtils.getUpdId());
        queryAudittrail.setQuerySql("");
        queryAudittrail.setQueryParameter(queryMap.toString());
        queryAudittrail.setBakColumn1(pageBean.getWhereHql());
        queryAudittrail.setQueryResult("");
        queryAudittrail.setQueryCount(pageBean.getRecordCount());
        queryAudittrail.setQueryTime(new Date());
        this.saveOrUpdate((E) queryAudittrail);

        final String aQueryId = getQueryIdSeq().toString();
        queryAudittrail.setQueryId(aQueryId);
        pageBean.setQueryCode(aQueryId);

        updateQueryIdSeq();

        return pageBean;
    }

    private void setParamMap(final Map<String, String> aParamMap) throws Exception {
        this.paramMap = new HashMap<String, Object>();
        final Iterator<String> it = aParamMap.keySet().iterator();
        while (it.hasNext()) {
            final String key = it.next();
            final String[] keyArray = key.split("_");
            if (new PageBean<E>().getOperatorMap().get(keyArray[0]) != null) {
                String fieldName = keyArray[1];
                Class tmpClass = this.clazz;
                if (fieldName.indexOf(".") > 0) {
                    final String[] tmpFileNameArray = fieldName.split("\\.");
                    final String tmpFileName = tmpFileNameArray[0];
                    tmpClass = tmpClass.getDeclaredField(tmpFileName).getType();
                    fieldName = tmpFileNameArray[1];
                }
                final Field field = tmpClass.getDeclaredField(fieldName);
                final String type = field.getType().getName().toString();
                String valStr = aParamMap.get(key);
                if (StringUtils.isNotEmpty(valStr)) {
                    if (type.equals("java.lang.String")) {
                        if (key.startsWith("like_")) {
                            valStr = "%" + valStr + "%";
                        }
                        this.paramMap.put(key, valStr);
                    } else if (type.equals("java.lang.Long")) {
                        this.paramMap.put(key, new Long(valStr));
                    } else if (type.equals("java.lang.Integer")) {
                        this.paramMap.put(key, new Integer(valStr));
                    } else if (type.equals("java.util.Date")) {
                        if (key.startsWith("date1_")) {
                            this.paramMap.put(key,
                                    CommonUtils.stringToDate(valStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                        } else if (key.startsWith("date2_")) {
                            this.paramMap.put(key,
                                    CommonUtils.stringToDate(valStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
                        }
                    } else if (type.equals("int")) {
                        this.paramMap.put(key, Integer.parseInt(valStr));
                    }
                }
            }

        }
    }

    public void saveOrUpdate(final E e) {
        this.getHibernateTemplate().saveOrUpdate(e);
    }

    public int getCount(final String sql) {
        return this.getByHql(sql).size();
    }

    @SuppressWarnings("unchecked")
    public List<E> getByPage(final String hql, final int gotoPage, final int pageSize) {
        final Query query = this.getSession().createQuery(hql);
        query.setFirstResult((gotoPage - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }
}
