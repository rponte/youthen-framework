package com.youthen.framework.common.exception;

import com.youthen.framework.common.AppMessage;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class BusinessCheckException extends BusinessException {

    /**
     * 。
     * 
     * @param aMessage
     */
    public BusinessCheckException(final AppMessage aMessage) {
        super(aMessage);

    }

    /**
     * 。
     */
    private static final long serialVersionUID = 1L;

}
