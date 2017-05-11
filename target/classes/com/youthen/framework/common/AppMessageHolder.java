// ============================================================
// Copyright(c) youthen All Right Reserved.
// File: $Id: AppMessageHolder.java 2159 2014-07-01 12:00:00Z Xin.Li $
// ============================================================
package com.youthen.framework.common;

import org.omg.CORBA.portable.ApplicationException;

/**
 * @see BusinessException
 * @see ApplicationException
 * @author Xin.Li
 */
public interface AppMessageHolder {

    /**
     * 取得AppMessage。
     * 
     * @return AppMessage
     */
    AppMessage getAppMessage();

    /**
     * 取得包含包名的Class名　
     * 
     * @return 包含包名的Class名　
     */
    String getClassName();
}
