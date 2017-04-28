package com.youthen.framework.persistence.dao;

import java.io.Serializable;
import java.util.List;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;
import com.youthen.framework.common.exception.OptimisticLockStolenException;
import com.youthen.framework.persistence.entity.CommonEntity;

/**
 * DAO基类
 * 
 * @copyright
 * @author LiXin
 * @Revision
 * @date 2010-3-26
 */
public interface EntityDao<E extends CommonEntity> {

    /**
     * 。
     * 
     * @param aEntity
     * @throws ObjectNotFoundException
     * @throws OptimisticLockStolenException
     */
    void update(final E aEntity);

    void specialUpdate(final E aEntity) throws DuplicateKeyException;

    void bulkUpdate(Iterable<E> aEntityList) throws DuplicateKeyException;

    /**
     * 。
     * 
     * @param aEntity
     * @throws DuplicateKeyException
     */
    Serializable insert(E aEntity) throws DuplicateKeyException;

    public Serializable specialInsert(final E aEntity) throws DuplicateKeyException;

    void bulkInsert(Iterable<E> aEntityList) throws DuplicateKeyException;

    void bulkSpecialInsert(Iterable<E> aEntityList) throws DuplicateKeyException;

    /**
     * 。
     * 
     * @param aEntity
     * @throws ObjectNotFoundException
     * @throws OptimisticLockStolenException
     */
    void delete(E aEntity) throws ObjectNotFoundException, OptimisticLockStolenException;

    void bulkDelete(Iterable<E> aEntityList) throws ObjectNotFoundException, OptimisticLockStolenException;

    /**
     * 。
     * 
     * @return
     * @throws ObjectNotFoundException
     */
    List<E> selectAll() throws ObjectNotFoundException;

    /**
     * 。
     * 
     * @param aParameterObject
     * @return
     * @throws ObjectNotFoundException
     */
    E lock(final Object id) throws ObjectNotFoundException;

    /**
     * 。
     * 
     * @param id
     * @return
     */
    public E getById(final Serializable id);

    public List getByHql(final String queryString);

    public List<E> getByHql(final String hql, Object[] paramValue);

    /**
     * @param e 根据e中所有非空字段查询
     * @param whereHql 根据e中所有非空字段无法查询的，自己拼查询条件
     * @param excludeParams e中为非空字段(比如有默认值)，但不作为查询条件的字段名称
     * @throws Exception
     */
    public List<E> getByEntity(E e, String whereHql, String[] excludeParams) throws Exception;

    public PageBean<E> getByPageBean(PageBean<E> pageBean) throws Exception;

    public int excuteByHql(final String hql, Object... values) throws Exception;

}
