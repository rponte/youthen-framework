// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.youthen.framework.persistence.entity.CommonEntity;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class BeanUtils {

    private static Log sLog = LogFactory.getLog(BeanUtils.class);

    private static String sMsgClassMustNotBeNull = "Class must not be null";
    private static String sMsgSourceMustNotBeNull = "Source must not be null";
    private static String sMsgTargetMustNotBeNull = "Target must not be null";

    private BeanUtils() {
    }

    public static void copyProperties(final Object aSource, final Object aTarget, final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, mergeIgnoreProperties(aIgnoreProperties, aTarget), false, null);
    }

    public static void copyProperties(final Object aSource, final Object aTarget, final Class<?> aEditable) {
        Assert.notNull(aEditable, sMsgClassMustNotBeNull);
        copyProperties(aSource, aTarget, aEditable, mergeIgnoreProperties(null, aTarget), false, null);
    }

    public static void copyProperties(final Object aSource, final Object aTarget, final Map<String, String> aRemapDef,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, mergeIgnoreProperties(aIgnoreProperties, aTarget), false, aRemapDef);
    }

    public static void copyNullableProperties(final Object aSource, final Object aTarget,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, mergeIgnoreProperties(aIgnoreProperties, aTarget), true, null);
    }

    public static void copyNullableProperties(final Object aSource, final Object aTarget, final Class<?> aEditable) {
        Assert.notNull(aEditable, sMsgClassMustNotBeNull);
        copyProperties(aSource, aTarget, aEditable, mergeIgnoreProperties(null, aTarget), true, null);
    }

    public static void copyNullableProperties(final Object aSource, final Object aTarget,
            final Map<String, String> aRemapDef,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, mergeIgnoreProperties(aIgnoreProperties, aTarget), true, aRemapDef);
    }

    public static void copyAllProperties(final Object aSource, final Object aTarget, final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, aIgnoreProperties, false, null);
    }

    public static void copyAllProperties(final Object aSource, final Object aTarget, final Class<?> aEditable) {
        Assert.notNull(aEditable, sMsgClassMustNotBeNull);
        copyProperties(aSource, aTarget, aEditable, null, false, null);
    }

    public static void copyAllProperties(final Object aSource, final Object aTarget,
            final Map<String, String> aRemapDef,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, aIgnoreProperties, false, aRemapDef);
    }

    public static void copyAllNullableProperties(final Object aSource, final Object aTarget,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, aIgnoreProperties, true, null);
    }

    public static void copyAllNullableProperties(final Object aSource, final Object aTarget, final Class<?> aEditable) {
        Assert.notNull(aEditable, sMsgClassMustNotBeNull);
        copyProperties(aSource, aTarget, aEditable, null, true, null);
    }

    public static void copyAllNullableProperties(final Object aSource, final Object aTarget,
            final Map<String, String> aRemapDef,
            final String... aIgnoreProperties) {
        copyProperties(aSource, aTarget, null, aIgnoreProperties, true, aRemapDef);
    }

    private static String[] mergeIgnoreProperties(final String[] aIgnoreProperties, final Object aTargetObject) {

        final String[] typeForToArray = new String[0];

        final Set<String> propertiesSet = new HashSet<String>();
        CollectionUtils.mergeArrayIntoCollection(aIgnoreProperties, propertiesSet);

        if (aTargetObject instanceof CommonEntity) {
            final List<String> ignoreProperties = new ArrayList<String>();
            final PropertyDescriptor[] targetPds =
                    org.springframework.beans.BeanUtils.getPropertyDescriptors(CommonEntity.class);
            for (final PropertyDescriptor pd : targetPds) {
                if (pd.getWriteMethod() != null) {
                    ignoreProperties.add(pd.getName());
                }
            }
            CollectionUtils.mergeArrayIntoCollection(ignoreProperties.toArray(typeForToArray), propertiesSet);
        }

        return propertiesSet.toArray(typeForToArray);
    }

    private static void copyProperties(final Object aSource, final Object aTarget, final Class<?> aEditable,
            final String[] aIgnoreProperties, final boolean aNullable, final Map<String, String> aRemapDef)
            throws BeansException {

        Assert.notNull(aSource, sMsgSourceMustNotBeNull);
        Assert.notNull(aTarget, sMsgTargetMustNotBeNull);

        Class<?> actualEditable = aTarget.getClass();
        if (aEditable != null) {
            if (!aEditable.isInstance(aTarget)) {
                throw new IllegalArgumentException("Target class [" + aTarget.getClass().getName() +
                        "] not assignable to Editable class [" + aEditable.getName() + "]");
            }
            actualEditable = aEditable;
        }
        final PropertyDescriptor[] targetPds =
                org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = null;
        if (aIgnoreProperties != null) {
            ignoreList = Arrays.asList(aIgnoreProperties);
        }

        for (final PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() == null ||
                    (ignoreList != null && (ignoreList.contains(targetPd.getName())))) {
                continue;
            }

            String actualSourcePropertyName = targetPd.getName();
            if (aRemapDef != null) {
                final String sourcePropertyName = aRemapDef.get(targetPd.getName());
                if (sourcePropertyName != null) {
                    actualSourcePropertyName = sourcePropertyName;
                }
            }

            final PropertyDescriptor sourcePd =
                    org.springframework.beans.BeanUtils.getPropertyDescriptor(aSource.getClass(),
                            actualSourcePropertyName);

            if (sourcePd == null || sourcePd.getReadMethod() == null) {
                continue;
            }

            try {
                final Method readMethod = sourcePd.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                final Object value = readMethod.invoke(aSource);
                if (!aNullable || value != null) {
                    final Method writeMethod = targetPd.getWriteMethod();
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(aTarget, value);
                }
            } catch (final Throwable ex) {
                final String msg = String
                        .format(
                                "Could not copy property. property:%s, sourceType:%s, targetType:%s",
                                actualSourcePropertyName,
                                sourcePd.getPropertyType(),
                                targetPd.getPropertyType());
                throw new FatalBeanException(msg, ex);
            }
        }
    }

    public static <K, V> Map<K, V> listToMap(final List<V> aList, final String aKeyPropertyName) {

        Assert.notNull(aList, "aList must not be null.");

        final Map<K, V> map = new HashMap<K, V>();
        if (aList.isEmpty()) {
            return map;
        }

        final V firstElement = aList.get(0);
        final PropertyDescriptor propertyDescriptor =
                org.springframework.beans.BeanUtils.getPropertyDescriptor(firstElement.getClass(), aKeyPropertyName);
        Assert.notNull(propertyDescriptor, "aKeyPropertyName is invalid.");
        final Method readMethod = propertyDescriptor.getReadMethod();

        try {
            for (final V value : aList) {
                @SuppressWarnings("unchecked")
                final K key = (K) readMethod.invoke(value);
                final V ejected = map.put(key, value);
                if (ejected != null) {
                    sLog.debug(String.format("an element ejected. key:%s, value:%s", key, ejected));
                }
            }
        } catch (final Throwable ex) {
            final String msg = String
                    .format(
                            "Could not read property. property:%s, type:%s",
                            aKeyPropertyName,
                            propertyDescriptor.getPropertyType());
            throw new FatalBeanException(msg, ex);
        }
        return map;
    }

    /**
     * 将null属性设置为"N.A."
     * 
     * @param obj
     * @return obj
     */
    public static Object setNAProperty(final Object obj) {
        if (obj == null) {
            return null;
        }
        final Field[] fields = obj.getClass().getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                try {
                    if (field.get(obj) == null || field.get(obj).equals("")) {
                        field.set(obj, "N.A.");
                    }
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
