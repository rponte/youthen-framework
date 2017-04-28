package com.youthen.framework.util.logging;

import java.util.Arrays;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class LoggingUtils {

    public static final String MESSAGE_DELIMITER = ", ";

    private LoggingUtils() {
    }

    public static String getStringExpression(final Object aObj) {
        String result;
        if (aObj == null) {
            result = null;
        } else if (aObj.getClass().isArray()) {
            final String componentTypeName = aObj.getClass().getComponentType().getName();
            if (boolean.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((boolean[]) aObj);
            } else if (byte.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((byte[]) aObj);
            } else if (char.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((char[]) aObj);
            } else if (double.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((double[]) aObj);
            } else if (float.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((float[]) aObj);
            } else if (int.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((int[]) aObj);
            } else if (long.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((long[]) aObj);
            } else if (short.class.getName().equals(componentTypeName)) {
                result = Arrays.toString((short[]) aObj);
            } else {
                result = Arrays.toString((Object[]) aObj);
            }
        } else {
            result = aObj.toString();
        }

        return result;
    }
}
