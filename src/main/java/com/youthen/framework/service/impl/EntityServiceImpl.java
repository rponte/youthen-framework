// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service.impl;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;
import com.youthen.framework.logic.EntityLogic;
import com.youthen.framework.persistence.entity.CommonEntity;
import com.youthen.framework.service.EntityService;

/**
 * @author chenxh
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 * @param <D>
 * @param <E>
 */
@Transactional(rollbackFor = Throwable.class)
public abstract class EntityServiceImpl<D, E extends CommonEntity> implements EntityService<D> {

    public abstract EntityLogic<D, E> getLogicImpl();

    @Override
    public D insert(final D aEntity) throws DuplicateKeyException {
        return getLogicImpl().insert(aEntity);
    }

    @Override
    public D update(final D aEntity) throws DuplicateKeyException {
        return getLogicImpl().update(aEntity);
    }

    @Override
    public List<D> selectAll() throws ObjectNotFoundException {
        return getLogicImpl().selectAll();
    }

    @Override
    public D getById(final Serializable aId) {
        return getLogicImpl().getById(aId);
    }

    @Override
    public List getByHql(final String aQueryString) throws ObjectNotFoundException {
        return getLogicImpl().getByHql(aQueryString);
    }

    @Override
    public List<D> getByEntity(final D aDto, final String aWhereHql, final String[] aExcludeParams) throws Exception {
        return getLogicImpl().getByEntity(aDto, aWhereHql, aExcludeParams);
    }

    @Override
    public PageBean<D> getByPageBean(final PageBean<D> aPageBean) throws Exception {
        return getLogicImpl().getByPageBean(aPageBean);
    }

    @Override
    public void bulkInsert(final Iterable<D> aDtoList) throws DuplicateKeyException {
        getLogicImpl().bulkInsert(aDtoList);
    }

    @Override
    public void bulkSpecialInsert(final Iterable<D> aDtoList) throws DuplicateKeyException {
        getLogicImpl().bulkSpecialInsert(aDtoList);
    }

    @Override
    public void bulkUpdate(final Iterable<D> aDtoList) throws DuplicateKeyException {
        getLogicImpl().bulkUpdate(aDtoList);
    }

    @Override
    public int excuteByHql(final String hql, final Object... values) throws Exception {
        return getLogicImpl().excuteByHql(hql, values);
    }

    @Override
    public D specialUpdate(final D aEntity) throws DuplicateKeyException {
        return getLogicImpl().specialUpdate(aEntity);
    }

}
