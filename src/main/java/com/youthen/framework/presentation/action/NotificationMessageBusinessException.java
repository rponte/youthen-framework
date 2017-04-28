package com.youthen.framework.presentation.action;

import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.exception.BusinessException;

public class NotificationMessageBusinessException extends BusinessException {

    /**
     * serial UID。
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param aAppMessage
     */
    public NotificationMessageBusinessException(final AppMessage aAppMessage) {
        super(aAppMessage);
    }

    @Override
    public String getClassName() {
        return NotificationMessageBusinessException.class.getName();
    }

}
