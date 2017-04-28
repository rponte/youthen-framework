package com.youthen.framework.common;

import java.io.Serializable;
import java.util.Locale;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SimpleAppMessage implements Serializable, AppMessage {

    private static final long serialVersionUID = 1504199246364642507L;
    private final String code;
    private Object[] params;

    public SimpleAppMessage(final String aCode) {
        this.code = aCode;
    }

    public SimpleAppMessage(final String aCode, final Object... aObjects) {
        this.code = aCode;
        this.format(aObjects);
    }

    public SimpleAppMessage format(final Object... aObjs) {
        final Object[] objs = ArrayUtils.clone(aObjs);
        this.params = objs;
        return this;
    }

    /**
     * @see com.youthen.framework.common.AppMessage#getCode()
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @see com.youthen.framework.common.AppMessage#getMesg()
     */
    public String getMesg() {
        return this.getMesg(LocaleContextHolder.getLocale());

    }

    /**
     * @see com.youthen.framework.common.AppMessage#getMesg(java.util.Locale)
     */
    public String getMesg(final Locale aLocale) {

        final String msg = AppMsgUtils.create(this.code, aLocale, this.params);
        return msg;
    }

    /**
     * @see com.youthen.framework.common.AppMessage#toString()
     */
    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

    /**
     * @see com.youthen.framework.common.AppMessage#toString(java.util.Locale)
     */
    public String toString(final Locale aLocale) {
        final String msg = this.getMesg(aLocale);
        return AppMsgUtils.create("XFW10001", aLocale, msg, this.code);
    }

}
