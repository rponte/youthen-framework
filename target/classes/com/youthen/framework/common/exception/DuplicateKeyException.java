package com.youthen.framework.common.exception;

import java.util.List;
import com.youthen.framework.common.AppMessage;

/**
 * 主键重复异常类
 * 
 * @author LiXin
 */
public class DuplicateKeyException extends BusinessException {

    /** 番号 */
    private static final long serialVersionUID = -4158535681389349542L;

    /**
     * 构造函数
     * 
     * @param aMessage 消息
     */
    public DuplicateKeyException(final AppMessage aMessage) {
        super(aMessage);
    }

    /**
     * 构造函数
     * 
     * @param aMessage 消息
     * @param aCause 异常抛出
     */
    public DuplicateKeyException(final AppMessage aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 复数消息
     */
    public DuplicateKeyException(final List<AppMessage> aMessages) {
        super(aMessages);
    }

    /**
     * 构造函数
     * 
     * @param aMessages 复数消息
     * @param aCause 异常抛出
     */
    public DuplicateKeyException(final List<AppMessage> aMessages, final Throwable aCause) {
        super(aMessages, aCause);
    }
}
