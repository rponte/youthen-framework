// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.logic.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.youthen.framework.common.PageBean;
import com.youthen.framework.common.exception.DuplicateKeyException;
import com.youthen.framework.common.exception.ObjectNotFoundException;
import com.youthen.framework.logic.EntityLogic;
import com.youthen.framework.persistence.dao.impl.EntityDaoImpl;
import com.youthen.framework.persistence.entity.CommonEntity;
import com.youthen.framework.util.BeanUtils;

/**
 * @author chenxh
 * @param <D>
 * @param <E>
 */
public abstract class EntityLogicImpl<D, E extends CommonEntity> implements EntityLogic<D, E> {

    public abstract EntityDaoImpl<E> getDaoImpl();

    @Override
    public E getEntityInstance() {
        try {
            return getDaoImpl().clazz.newInstance();
        } catch (final Exception e) {
            return null;
        }
    }

    @Override
    public D insert(final D aDto) throws DuplicateKeyException {
        final E e = this.getEntityInstance();
        BeanUtils.copyNullableProperties(aDto, e);
        getDaoImpl().insert(e);
        BeanUtils.copyProperties(e, aDto);
        return aDto;
    }

    @Override
    public D update(final D aDto) throws DuplicateKeyException {
        final E e = this.getEntityInstance();
        BeanUtils.copyNullableProperties(aDto, e);
        getDaoImpl().update(e);
        BeanUtils.copyProperties(e, aDto);
        return aDto;
    }

    @Override
    public List<D> selectAll() throws ObjectNotFoundException {
        return converToDtoList(getDaoImpl().selectAll());
    }

    @Override
    public D getById(final Serializable aId) {
        final D d = getDtoInstance();
        final E e = getDaoImpl().getById(aId);
        if (e != null) BeanUtils.copyProperties(e, d);
        return d;
    }

    @Override
    public List getByHql(final String aQueryString) throws ObjectNotFoundException {
        return getDaoImpl().getByHql(aQueryString);
    }

    @Override
    public List<D> getByEntity(final D aDto, final String aWhereHql, final String[] aExcludeParams) throws Exception {
        final E e = this.getEntityInstance();
        BeanUtils.copyNullableProperties(aDto, e);
        return converToDtoList(getDaoImpl().getByEntity(e, aWhereHql, aExcludeParams));
    }

    @Override
    public PageBean<D> getByPageBean(final PageBean<D> aPageBean) throws Exception {
        final PageBean<E> newPageBean =
                new PageBean<E>(aPageBean.getCurrent().intValue(), aPageBean.getPageSize().intValue());
        newPageBean.setAsc(aPageBean.getAsc());
        newPageBean.setWhereMap(aPageBean.getWhereMap());
        newPageBean.setWhereHql(aPageBean.getWhereHql());
        newPageBean.setSortColumnName(aPageBean.getSortColumnName());
        final PageBean<E> resultPageBean = getDaoImpl().getByPageBean(newPageBean);
        aPageBean.setRecordCount(resultPageBean.getRecordCount());
        aPageBean.setRecordList(converToDtoList(resultPageBean.getRecordList()));
        aPageBean.setQueryCode(resultPageBean.getQueryCode());
        return aPageBean;
    }

    @Override
    public List<D> converToDtoList(final List<E> entityList) {
        final List<D> dtoList = new ArrayList<D>();
        if (entityList != null) {
            for (int i = 0; i < entityList.size(); i++) {
                final D d = getDtoInstance();
                BeanUtils.copyProperties(entityList.get(i), d);
                dtoList.add(d);
            }
        }
        return dtoList;
    }

    @Override
    public void bulkInsert(final Iterable<D> aDtoList) throws DuplicateKeyException {
        for (final D d : aDtoList) {
            this.insert(d);
        }
    }

    @Override
    public void bulkSpecialInsert(final Iterable<D> aDtoList) throws DuplicateKeyException {
        for (final D d : aDtoList) {
            this.specialInsert(d);
        }
    }

    @Override
    public D specialInsert(final D aDto) throws DuplicateKeyException {
        final E e = this.getEntityInstance();
        BeanUtils.copyNullableProperties(aDto, e);
        getDaoImpl().specialInsert(e);
        BeanUtils.copyProperties(e, aDto);
        return aDto;
    }

    @Override
    public D specialUpdate(final D aDto) throws DuplicateKeyException {
        final E e = this.getEntityInstance();
        BeanUtils.copyNullableProperties(aDto, e);
        getDaoImpl().specialUpdate(e);
        BeanUtils.copyProperties(e, aDto);
        return aDto;
    }

    @Override
    public void bulkUpdate(final Iterable<D> aDtoList) throws DuplicateKeyException {
        for (final D d : aDtoList) {
            this.update(d);
        }
    }

    public int excuteByHql(final String hql, final Object... values) throws Exception {
        return getDaoImpl().excuteByHql(hql, values);
    }

}
