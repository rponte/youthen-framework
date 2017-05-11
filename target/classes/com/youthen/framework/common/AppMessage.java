// ============================================================
// Copyright(c) youthen All Right Reserved.
// File: $Id: AppMessage.java 2159 2014-07-01 12:00:00Z Xin.Li $
// ============================================================
package com.youthen.framework.common;

import java.util.Locale;

/**
 * 消息类接口
 * 
 * @author LiXin
 */
public interface AppMessage {

    /**
     * message格式化变换
     * 
     * @param aObjs 变长参数
     * @return AppMessage
     */
    AppMessage format(final Object... aObjs);

    /**
     * code取得
     * 
     * @return code
     */
    String getCode();

    /**
     * AppMessage取得
     * 
     * @return AppMessage
     */
    String getMesg();

    /**
     * AppMessage取得
     * 
     * @param aLocale Locale
     * @return AppMessage
     */
    String getMesg(final Locale aLocale);

    /**
     * 完全AppMessage文字列表現
     * 
     * @return 文字列表現
     */
    String toString();

    /**
     * 完全AppMessage文字列表現(指定Locale)。
     * 
     * @param aLocale ロケール
     * @return 文字列表現
     */
    String toString(final Locale aLocale);

}
