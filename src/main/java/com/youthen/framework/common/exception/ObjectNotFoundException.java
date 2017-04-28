package com.youthen.framework.common.exception;

import java.util.List;
import com.youthen.framework.common.AppMessage;

/**
 * 对象不存在异常。
 * 
 * @author Xin.Li
 */
public class ObjectNotFoundException extends BusinessException {

    /** 番号 */
    private static final long serialVersionUID = -3375593521167909976L;

    /**
     * 构造函数
     * 
     * @param aMessage AppMessage
     */
    public ObjectNotFoundException(final AppMessage aMessage) {
        super(aMessage);
    }

    /**
     * 构造函数
     * 
     * @param aMessage AppMessage
     * @param aCause 异常
     */
    public ObjectNotFoundException(final AppMessage aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 複数AppMessage
     */
    public ObjectNotFoundException(final List<AppMessage> aMessages) {
        super(aMessages);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 複数AppMessage
     * @param aCause 异常
     */
    public ObjectNotFoundException(final List<AppMessage> aMessages, final Throwable aCause) {
        super(aMessages, aCause);
    }
}
