package com.youthen.framework.common.security;

import org.springframework.security.authentication.AccountStatusException;

public class CredentialsLockedException extends AccountStatusException {

    private static final long serialVersionUID = -4696300267465029877L;

    public CredentialsLockedException(final String aMsg) {
        super(aMsg);
    }

    public CredentialsLockedException(final String aMsg, final Throwable aT) {
        super(aMsg, aT);
    }

    public CredentialsLockedException(final String aMsg, final Object aExtraInformation) {
        super(aMsg, aExtraInformation);
    }

}
