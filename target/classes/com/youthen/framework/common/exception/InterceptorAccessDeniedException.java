// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.common.exception;

import com.youthen.framework.common.AppMessage;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class InterceptorAccessDeniedException extends RuntimeBusinessException {

    /**
     * serialVersionUID。
     */
    private static final long serialVersionUID = 395205897106701485L;

    /**
     * @param aThrow
     * @param aCode
     * @param aObjects
     */
    public InterceptorAccessDeniedException(final Throwable aThrow, final String aCode, final Object... aObjects) {
        super(aThrow, aCode, aObjects);
    }

    /**
     * @param aCode
     * @param aObjects
     */
    public InterceptorAccessDeniedException(final String aCode, final Object... aObjects) {
        super(aCode, aObjects);
    }

    /**
     */
    public InterceptorAccessDeniedException(final AppMessage aMessage) {
        super(aMessage);
    }

    /**
     * 
     */
    public InterceptorAccessDeniedException(final AppMessage aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     */
    public InterceptorAccessDeniedException(final BusinessException aCause) {
        super(aCause);
    }

    /**
     */
    public String getErrorMessage() {
        return getAppMessage().getMesg();
    }
}
