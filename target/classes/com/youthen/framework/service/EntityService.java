// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service;

import java.io.Serializable;
import java.util.List;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;

/**
 * @author chenxh
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 * @param <D>
 */
public interface EntityService<D> {

    D insert(D aDto) throws DuplicateKeyException;

    D update(D aDto) throws DuplicateKeyException;

    void bulkInsert(Iterable<D> aDtoList) throws DuplicateKeyException;

    void bulkUpdate(Iterable<D> aDtoList) throws DuplicateKeyException;

    List<D> selectAll() throws ObjectNotFoundException;

    /**
     * @param aDto 根据aDto中所有非空字段查询
     * @param whereHql 根据aDto中所有非空字段无法查询的，自己拼查询条件
     * @param excludeParams aDto中为非空字段(比如有默认值)，但不作为查询条件的字段名称
     * @throws Exception
     */
    List<D> getByEntity(D aDto, String whereHql, String[] excludeParams) throws Exception;

    D getById(final Serializable id) throws ObjectNotFoundException;

    List getByHql(final String queryString) throws ObjectNotFoundException;

    PageBean<D> getByPageBean(PageBean<D> pageBean) throws Exception;

    int excuteByHql(final String hql, final Object... values) throws Exception;

    void bulkSpecialInsert(Iterable<D> aDtoList) throws DuplicateKeyException;

    D specialUpdate(D aDto) throws DuplicateKeyException;
}
