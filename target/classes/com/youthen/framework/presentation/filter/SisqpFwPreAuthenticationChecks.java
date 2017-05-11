// ============================================================
// Copyright(c) Pro-Ship Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.presentation.filter;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.common.security.AuthenticatedUser;
import com.youthen.framework.common.security.CredentialsLockedException;

public class SisqpFwPreAuthenticationChecks implements UserDetailsChecker {

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    /**
     * @see org.springframework.security.core.userdetails.UserDetailsChecker#check(org.springframework.security.core.userdetails.UserDetails)
     */
    @Override
    public void check(final UserDetails aToCheck) {
        if (!aToCheck.isAccountNonLocked()) {
            throw new LockedException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"), aToCheck);
        }

        if (!aToCheck.isEnabled()) {
            throw new DisabledException(this.messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"), aToCheck);
        }

        if (!aToCheck.isAccountNonExpired()) {
            throw new AccountExpiredException(this.messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"), aToCheck);
        }

        if (aToCheck instanceof AuthenticatedUser) {
            final AuthenticatedUser user = (AuthenticatedUser) aToCheck;
            if (true) {
                // if (user.getPasswordErrorCount() >= 3L) {
                throw new CredentialsLockedException(new FieldSupportedMessage("XFW73001").toString(), user);
            }
        }
    }
}
