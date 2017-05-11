package com.youthen.framework.persistence.dao;

import com.youthen.framework.common.exception.DuplicateKeyException;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public interface EntityDaoExtender<E> {

    void executeInsert(E aEntity) throws DuplicateKeyException;

}
