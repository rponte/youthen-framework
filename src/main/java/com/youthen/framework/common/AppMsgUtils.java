package com.youthen.framework.common;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class AppMsgUtils {

    private static Log sLog = LogFactory.getLog(AppMsgUtils.class);

    private static final String MSG_BUNDLE = "youthen_messages";

    private static final String APP_BUNDLE = "app_messages";

    /**
     */
    private AppMsgUtils() {
    }

    public static String create(final String aKey, final Locale aLocale, final Object... aObjs) {

        String msg = null;
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(APP_BUNDLE, aLocale);
            msg = bundle.getString(aKey);
        } catch (final MissingResourceException e) {
            try {
                final ResourceBundle bundle = ResourceBundle.getBundle(MSG_BUNDLE, aLocale);
                msg = bundle.getString(aKey);
            } catch (final MissingResourceException ee) {
                sLog.warn(create("XFW70001", Locale.getDefault(), aKey));
                return aKey;
            }
        }
        return MessageFormat.format(msg, aObjs);
    }

    public static String[] getParamAddFirst(final Object[] aParams, final Object aAddParam) {

        final String[] target;
        if (aParams != null) {
            final String[] paramsConversion = new String[aParams.length + 1];
            paramsConversion[0] = aAddParam.toString();
            for (int i = 0; i < aParams.length; i++) {
                paramsConversion[i + 1] = aParams[i].toString();
            }
            target = paramsConversion;
        } else {
            target = new String[1];
            target[0] = aAddParam.toString();
        }
        return target;
    }
}
