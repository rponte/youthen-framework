// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id: CacheNodeGroup.java 8864 2012-04-06 08:17:20Z t.yasuda $
// ============================================================
package com.youthen.framework.util.cache;

import com.youthen.framework.common.context.SessionContext;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.common.security.AuthenticatedUser;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public enum CacheNodeGroup {
    COMPANIES("companies"),
    USERS("users"),
    COMMON("common");

    static final String SEP = "/";
    private String tag;

    /**
     * 
     */
    private CacheNodeGroup(final String aTag) {
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
    String createPath() {
        final AuthenticatedUser user = SessionContext.getUser();
        if (user == null) {
            throw new ApplicationException("XFW71005");
        }
        final String kaishacd = "000";
        final String userid = user.getUsername();
        String path = null;
        if (this == CacheNodeGroup.COMPANIES) {
            path = this.createCompanyPath(kaishacd);
        } else if (this == CacheNodeGroup.USERS) {
            path = this.createUserPath(kaishacd, userid);
        } else if (this == CacheNodeGroup.COMMON) {
            path = this.createCommonPath();
        } else {
            throw new ApplicationException(new FieldSupportedMessage("XFW71002").format(this.getTag()));
        }
        return path;
    }

    private String createCompanyPath(final String aKaishacd) {
        return SEP + COMPANIES.getTag() + SEP + aKaishacd + SEP;
    }

    private String createUserPath(final String aKaishacd, final String aUserid) {
        return this.createCompanyPath(aKaishacd) + USERS.getTag() + SEP + aUserid + SEP;
    }

    private String createCommonPath() {
        return SEP + COMMON.getTag() + SEP;
    }
}
