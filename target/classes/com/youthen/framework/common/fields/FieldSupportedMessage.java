package com.youthen.framework.common.fields;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.AppMsgUtils;
import com.youthen.framework.common.StringUtils;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class FieldSupportedMessage implements Serializable, AppMessage {

    private static final Pattern NAMEID_PATTERN = Pattern.compile("%%([^%]+?)%%");
    private static final long serialVersionUID = 1504199246364642507L;
    private final String code;

    private Object[] params;

    public FieldSupportedMessage(final String aCode) {
        this.code = aCode;
    }

    /**
     * @see com.youthen.framework.common.AppMessage#format(Object...)
     */
    public FieldSupportedMessage format(final Object... aObjs) {
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
        final String replaced = this.replaceName(msg);
        return replaced;
    }

    private String replaceName(final String aString) {

        if (StringUtils.isEmpty(aString)) {
            return aString;
        }

        final StringBuilder buff = new StringBuilder(500);
        int lastIndex = 0;
        final Matcher matcher = NAMEID_PATTERN.matcher(aString);
        while (matcher.find()) {

            buff.append(aString.substring(lastIndex, matcher.start()));
            lastIndex = matcher.end();
        }
        buff.append(aString.substring(lastIndex));
        return buff.toString();

    }

    /**
     * @see com.youthen.framework.common.AppMessage#toString()
     */
    @Override
    public String toString() {
        return this.toString(LocaleContextHolder.getLocale());
    }

    /**
     * @see com.youthen.framework.common.AppMessage#toString(java.util.Locale)
     */
    public String toString(final Locale aLocale) {
        final String msg = this.getMesg(aLocale);
        return AppMsgUtils.create("XFW10001", aLocale, msg, this.code);
    }

}
