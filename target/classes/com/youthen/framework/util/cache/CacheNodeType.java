// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id: CacheNodeType.java 725 2010-06-29 08:55:39Z t.shimokawa $
// ============================================================
package com.youthen.framework.util.cache;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
enum CacheNodeType {
    FIELDS("fields"),
    PROFILES("profiles");

    private String tag;

    /**
     * 
     */
    private CacheNodeType(final String aTag) {
        this.tag = aTag;
    }

    /**
     * 
     */
    private String getTag() {
        return this.tag;
    }

    /**
     * 
     */
    String createPath(final CacheNodeGroup aGroup) {
        return aGroup.createPath() + getTag() + CacheNodeGroup.SEP;
    }
}
