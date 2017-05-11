package com.youthen.framework.logic;

import java.io.Serializable;
import java.util.List;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;

/**
 * DAO基类
 * 
 * @copyright
 * @author chenxh
 * @param <D>
 * @param <E>
 * @Revision
 * @date 2010-3-26
 */
public interface EntityLogic<D, E> {

    D insert(D aDto) throws DuplicateKeyException;

    D update(D aDto) throws DuplicateKeyException;

    void bulkInsert(Iterable<D> aDtoList) throws DuplicateKeyException;

    void bulkUpdate(Iterable<D> aDtoList) throws DuplicateKeyException;

    List<D> selectAll() throws ObjectNotFoundException;

    public List<D> getByEntity(D aDto, String whereHql, String[] excludeParams) throws Exception;

    D getById(final Serializable id);

    List getByHql(final String queryString) throws ObjectNotFoundException;

    List<D> converToDtoList(final List<E> entityList);

    PageBean<D> getByPageBean(PageBean<D> pageBean) throws Exception;

    E getEntityInstance();

    D getDtoInstance();

    int excuteByHql(final String hql, final Object... values) throws Exception;

    void bulkSpecialInsert(Iterable<D> aDtoList) throws DuplicateKeyException;

    D specialInsert(D aDto) throws DuplicateKeyException;

    D specialUpdate(D aDto) throws DuplicateKeyException;
}
