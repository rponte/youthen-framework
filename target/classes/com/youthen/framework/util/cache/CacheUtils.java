// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id: CacheUtils.java 9651 2012-05-11 12:51:26Z m.hirabayashi $
// ============================================================
package com.youthen.framework.util.cache;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class CacheUtils {

    private static final String SEP_CACHE_MESSAGE = ":";
    private static final Log LOG = LogFactory.getLog(CacheUtils.class);
    private static final String AREA_PPFW = "youthen";
    private static final String AREA_APP = "app";
    private static CacheMessageProducer sCacheMessageProducer;

    private CacheUtils() {
    }

    public static void start() {
        sCacheMessageProducer.start();
    }

    public static Serializable getField(final String aKey) {
        LOG.debug(String.format("getField(key:%s)", aKey));
        final Serializable o = get(AREA_PPFW, createKeyPath(CacheNodeGroup.COMPANIES, CacheNodeType.FIELDS, aKey));
        if (o != null) {
            return o;
        }
        return get(AREA_PPFW, createKeyPath(CacheNodeGroup.COMMON, CacheNodeType.FIELDS, aKey));
    }

    public static Serializable getProfile(final String aKey) {
        Serializable o = get(AREA_PPFW, createKeyPath(CacheNodeGroup.USERS, CacheNodeType.PROFILES, aKey));
        if (o == null) {
            o = get(AREA_PPFW, createKeyPath(CacheNodeGroup.COMPANIES, CacheNodeType.PROFILES, aKey));
        }
        if (o == null) {
            o = get(AREA_PPFW, createKeyPath(CacheNodeGroup.COMMON, CacheNodeType.PROFILES, aKey));
        }
        return o;
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public static void put(final CacheNodeGroup aGroup, final String aKey, final Object aObject) {
        LOG.debug(String.format("put(group:%s, key:%s, object:%s)", aGroup.name(), aKey, aObject));
        put(AREA_PPFW, createKeyPath(aGroup, aKey), aObject);
    }

    public static Object get(final CacheNodeGroup aGroup, final String aKey) {
        return createClone(get(AREA_PPFW, createKeyPath(aGroup, aKey)));
    }

    public static void remove(final CacheNodeGroup aGroup, final String aKey) {
        remove(AREA_PPFW, createKeyPath(aGroup, aKey));
    }

    public static void removeCascade(final CacheNodeGroup aGroup) {
        // final String keyPath = aGroup.createPath();
        // removeCascade(AREA_PPFW, keyPath);
    }

    public static void putField(final CacheNodeGroup aGroup, final String aKey, final Object aObject) {
        LOG.debug(String.format("putField(group:%s, key:%s, object:%s)", aGroup, aKey, aObject));
        put(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.FIELDS, aKey), aObject);
    }

    public static void putProfile(final CacheNodeGroup aGroup, final String aKey, final Object aObject) {
        put(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.PROFILES, aKey), aObject);
    }

    public static Object getField(final CacheNodeGroup aGroup, final String aKey) {
        return get(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.FIELDS, aKey));
    }

    public static Object getProfile(final CacheNodeGroup aGroup, final String aKey) {
        return get(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.PROFILES, aKey));
    }

    public static void removeField(final CacheNodeGroup aGroup, final String aKey) {
        remove(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.FIELDS, aKey));
    }

    public static void removeProfile(final CacheNodeGroup aGroup, final String aKey) {
        remove(AREA_PPFW, createKeyPath(aGroup, CacheNodeType.PROFILES, aKey));
    }

    public static void removeCascadeField(final CacheNodeGroup aGroup) {
        removeCascade(AREA_PPFW, CacheNodeType.FIELDS.createPath(aGroup));
    }

    public static void removeCascadeProfile(final CacheNodeGroup aGroup) {
        removeCascade(AREA_PPFW, CacheNodeType.PROFILES.createPath(aGroup));
    }

    public static void put(final String aCacheKey, final Object aObject) {
        put(AREA_APP, aCacheKey, aObject);
    }

    public static Object get(final String aCacheKey) {
        return get(AREA_APP, aCacheKey);
    }

    public static void remove(final String aCacheKey) {
        remove(AREA_APP, aCacheKey);
    }

    public static void removeCascade(final String aPrefix) {
        removeCascade(AREA_APP, aPrefix);
    }

    private static void put(final String aArea, final String aCacheKey, final Object aObject) {
        findCache(aArea).put(new Element(aCacheKey, aObject));
    }

    private static Serializable get(final String aArea, final String aCacheKey) {
        LOG.debug(String.format("get(area:%s, key:%s)", aArea, aCacheKey));
        final Element element = findCache(aArea).get(aCacheKey);
        if (element == null) {
            return null;
        }
        return element.getValue();
    }

    private static void remove(final String aArea, final String aCacheKey) {
        findCache(aArea).remove(aCacheKey);
        sCacheMessageProducer.send(aArea + SEP_CACHE_MESSAGE + aCacheKey);
    }

    @SuppressWarnings("unchecked")
    private static void removeCascade(final String aArea, final String aCacheKeyPrefix) {
        final Cache cache = findCache(aArea);
        final List<String> cacheKeys = cache.getKeys();
        for (final String cacheKey : cacheKeys) {
            if (cacheKey.startsWith(aCacheKeyPrefix)) {
                findCache(aArea).remove(cacheKey);
                sCacheMessageProducer.send(aArea + SEP_CACHE_MESSAGE + cacheKey);
            }
        }
    }

    // ----------------------------------------------------------------
    private static Cache findCache(final String aArea) {
        final Cache cache = CacheManager.getInstance().getCache(aArea);
        if (cache == null) {
            throw new ApplicationException(new FieldSupportedMessage("XFW71001").format(aArea));
        }
        return cache;
    }

    private static String createKeyPath(final CacheNodeGroup aGroup, final String aKey) {
        return aGroup.createPath() + aKey + CacheNodeGroup.SEP;
    }

    private static String createKeyPath(final CacheNodeGroup aGroup, final CacheNodeType aType, final String aKey) {
        return aType.createPath(aGroup) + aKey + CacheNodeGroup.SEP;
    }

    @SuppressWarnings("unused")
    @Resource
    private void setCacheMessageProducer(final CacheMessageProducer aCacheMessageProducer) {
        LOG.info(new FieldSupportedMessage("IFW71001").format());
        sCacheMessageProducer = aCacheMessageProducer;
        sCacheMessageProducer.start();
    }

    private static Object createClone(final Serializable aSerializable) {
        return SerializationUtils.clone(aSerializable);
    }
}
