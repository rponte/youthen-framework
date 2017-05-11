// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.test;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public interface BaseTestContextHelper {

    void buildupSessionContext(final String aUserid, final String aKaishacd, final String aPassword,
            final String[] aAuths);

}
