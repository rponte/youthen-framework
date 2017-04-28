// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id: PpfwPrincipal.java 2198 2011-02-22 11:43:37Z m.hirabayashi $
// ============================================================
package com.youthen.framework.common.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpFwPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 3514201766945088595L;
    private static final String DELIM = "/";

    private String userId;
    private String companyCode;

    public SisqpFwPrincipal() {
    }

    public SisqpFwPrincipal(final String aUsername) {
        final int pos = aUsername.indexOf(DELIM);
        if (pos < 0) {
            this.userId = aUsername;
        } else {
            this.userId = aUsername.substring(pos + 1);
        }
    }

    public SisqpFwPrincipal(final String aCompanyCode, final String aUserId) {
        this.companyCode = aCompanyCode;
        this.userId = aUserId;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * getter for userId.
     * 
     * @return userId
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * setter for userId.
     * 
     * @param aUserId userId
     */
    public void setUserId(final String aUserId) {
        this.userId = aUserId;
    }

    /**
     * getter for companyCode.
     * 
     * @return companyCode
     */
    public String getCompanyCode() {
        return this.companyCode;
    }

    /**
     * setter for companyCode.
     * 
     * @param aCompanyCode companyCode
     */
    public void setCompanyCode(final String aCompanyCode) {
        this.companyCode = aCompanyCode;
    }

    /**
     * @see java.security.Principal#getName()
     */

    @Override
    public String getName() {
        return this.userId;
    }

}
