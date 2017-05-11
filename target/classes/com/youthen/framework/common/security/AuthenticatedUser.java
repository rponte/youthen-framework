package com.youthen.framework.common.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User {

    private static final long serialVersionUID = 3669592540222935141L;
    private final SisqpFwPrincipal user;

    private final long passwordErrorCount;

    private Long companyId;
    private Long departmentId;

    public AuthenticatedUser(
            final SisqpFwPrincipal aUser,
            final String aPassword,
            final boolean aEnabled,
            final boolean aAccountNonExpired,
            final boolean aCredentialsNonExpired,
            final boolean aAccountNonLocked,
            final Collection<GrantedAuthority> aAuthorities, final long aPasswordErrorCount) {

        super(aUser.getName(), aPassword, aEnabled, aAccountNonExpired, aCredentialsNonExpired,
                aAccountNonLocked, aAuthorities);
        this.passwordErrorCount = aPasswordErrorCount;
        this.user = aUser;
    }

    public SisqpFwPrincipal getUser() {
        return this.user;
    }

    public String getCompanyCode() {
        return this.user.getCompanyCode();
    }

    public String getUserId() {
        return this.user.getUserId();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    /**
     * getter for passwordErrorCount.
     * 
     * @return passwordErrorCount
     */
    public long getPasswordErrorCount() {
        return this.passwordErrorCount;
    }

    /**
     * @see org.springframework.security.core.userdetails.User#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object aRhs) {
        if ((aRhs == null) || !(aRhs instanceof AuthenticatedUser)) {
            return false;
        }

        return super.equals(aRhs);
    }

    /**
     * getter for companyId.
     * 
     * @return companyId
     */
    public Long getCompanyId() {
        return this.companyId;
    }

    /**
     * setter for companyId.
     * 
     * @param aCompanyId companyId
     */
    public void setCompanyId(final Long aCompanyId) {
        this.companyId = aCompanyId;
    }

    /**
     * getter for departmentId.
     * 
     * @return departmentId
     */
    public Long getDepartmentId() {
        return this.departmentId;
    }

    /**
     * setter for departmentId.
     * 
     * @param aDepartmentId departmentId
     */
    public void setDepartmentId(final Long aDepartmentId) {
        this.departmentId = aDepartmentId;
    }

    /**
     * @see org.springframework.security.core.userdetails.User#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode() % 5;
    }
}
