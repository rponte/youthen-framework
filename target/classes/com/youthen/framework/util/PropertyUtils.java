package com.youthen.framework.util;

import java.util.Properties;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.common.annotation.Utility;
import com.youthen.framework.common.beans.BeanDefineInfo;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.fields.FieldSupportedMessage;

@Utility
public final class PropertyUtils {

    private PropertyUtils() {
    }

    private static String getPropertyValue(final String aKey) {
        if (StringUtils.isNotEmpty(aKey)) {
            Properties properties;

            properties = BeanDefineInfo.getProperties();
            if (properties != null && StringUtils.isNotEmpty(properties.getProperty(aKey))) {
                return properties.getProperty(aKey);
            }

            properties = BeanDefineInfo.getFrameworkProperties();
            if (properties != null && StringUtils.isNotEmpty(properties.getProperty(aKey))) {
                return properties.getProperty(aKey);
            }
        }

        return "";
    }

    /**
     */
    public static String getPropertyValue(final IPropertyKeys aPropertyKeys) {
        if (aPropertyKeys != null && StringUtils.isNotEmpty(aPropertyKeys.toString())) {
            final String propertyKey = aPropertyKeys.toString();
            return getPropertyValue(propertyKey);
        }
        return "";
    }

    /**
     */
    public static String getPropertyValue(final IPropertyKeys aPropertyKeys, final String aDefaultValue) {
        String result = getPropertyValue(aPropertyKeys);
        if (StringUtils.isEmpty(result)) {
            result = aDefaultValue;
        }
        return result;
    }

    /**
     */
    public static long getPropertyValueOfLong(final IPropertyKeys aPropertyKeys) {
        long result;

        try {
            final String val = getPropertyValue(aPropertyKeys);
            if (val != null && !val.equals("")) {
                result = Long.valueOf(val).longValue();
            } else {
                result = 0L;
            }
        } catch (final NumberFormatException e) {
            throw new ApplicationException(new FieldSupportedMessage("XFW70005").format(aPropertyKeys, "long"));
        }

        return result;
    }

    /**
     */
    public static long getPropertyValueOfLong(final IPropertyKeys aPropertyKeys, final long aDefaultValue) {
        long result = getPropertyValueOfLong(aPropertyKeys);
        if (result == 0) {
            result = aDefaultValue;
        }
        return result;
    }
}
