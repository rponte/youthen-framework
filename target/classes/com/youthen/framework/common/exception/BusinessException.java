// ============================================================
// Copyright(c) youthen All Right Reserved.
// File: $Id: BusinessException.java 2159 2014-07-01 12:00:00Z Xin.Li $
// ============================================================
package com.youthen.framework.common.exception;

import java.util.ArrayList;
import java.util.List;
import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.AppMessageHolder;

/**
 * 业务逻辑异常类。
 * 
 * @author lixin
 */
public class BusinessException extends Exception implements AppMessageHolder {

    /** 製品番号 */
    private static final long serialVersionUID = 426685295548223540L;

    /** messsage */
    private final List<AppMessage> messages = new ArrayList<AppMessage>(0);

    /**
     * @param aMessage AppMessage
     */
    public BusinessException(final AppMessage aMessage) {
        this.messages.add(aMessage);
    }

    /**
     * 构造函数
     * 
     * @param aMessage AppMessage
     * @param aCause 异常
     */
    public BusinessException(final AppMessage aMessage, final Throwable aCause) {
        super(aCause);
        this.messages.add(aMessage);
    }

    /**
     * @param aMessages 复数AppMessage
     */
    public BusinessException(final List<? extends AppMessage> aMessages) {
        this.messages.addAll(aMessages);
    }

    /**
     * @param aMessages 复数AppMessage
     * @param aCause 异常
     */
    public BusinessException(final List<? extends AppMessage> aMessages, final Throwable aCause) {
        super(aCause);
        this.messages.addAll(aMessages);
    }

    /**
     * AppMessage取得
     * 
     * @return AppMessage
     */
    public AppMessage getAppMessage() {
        if (this.messages.size() == 0) {
            return null;
        }
        return this.messages.get(0);
    }

    /**
     * 复数AppMessage取得
     * 
     * @return 复数AppMessage
     */
    public List<AppMessage> getAppMessages() {
        return this.messages;
    }

    /**
     * <pre>
     * AppMessage取得。
     * </pre>
     * 
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        final StringBuilder builder = new StringBuilder();
        for (final AppMessage message : this.messages) {
            builder.append(message.toString());
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    /**
     * 类名取得
     */
    @Override
    public String getClassName() {
        return BusinessException.class.getName();
    }

}
