package com.youthen.framework.common.exception;

import java.util.List;
import com.youthen.framework.common.AppMessage;

/**
 * 乐观的逻辑失败异常
 * 
 * @author Xin.Li
 */
public class OptimisticLockStolenException extends BusinessException {

    /** 製品番号 */
    private static final long serialVersionUID = 2664220496111066264L;

    /**
     * 构造函数
     * 
     * @param aMessage AppMessage
     */
    public OptimisticLockStolenException(final AppMessage aMessage) {
        super(aMessage);
    }

    /**
     * 构造函数
     * 
     * @param aMessage AppMessage
     * @param aCause 异常
     */
    public OptimisticLockStolenException(final AppMessage aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 複数AppMessage
     */
    public OptimisticLockStolenException(final List<AppMessage> aMessages) {
        super(aMessages);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 複数AppMessage
     * @param aCause 异常
     */
    public OptimisticLockStolenException(final List<AppMessage> aMessages, final Throwable aCause) {
        super(aMessages, aCause);
    }
}
