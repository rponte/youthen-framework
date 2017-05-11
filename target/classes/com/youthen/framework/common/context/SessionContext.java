package com.youthen.framework.common.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.youthen.framework.common.security.AuthenticatedUser;

/**
 */
public final class SessionContext {

    private static final String XFW11001 = "XFW11001";

    private SessionContext() {
    }

    /**
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     */
    public static List<String> getAuthorities() {
        final Authentication auth = getAuthentication();
        if (auth == null) {
            return null;
            // throw new ApplicationException(new SimpleAppMessage(XFW11001).format());
        }

        final Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        final List<String> authorityList = new ArrayList<String>(authorities.size());
        for (final GrantedAuthority authority : authorities) {
            authorityList.add(authority.getAuthority());
        }
        return authorityList;
    }

    /**
     */
    public static AuthenticatedUser getUser() {
        final Authentication auth = getAuthentication();
        if (auth == null) {
            return null;
            // throw new ApplicationException(new SimpleAppMessage(XFW11001).format());
        }
        AuthenticatedUser authUser = null;
        final Object user = auth.getPrincipal();
        if (user instanceof AuthenticatedUser) {
            authUser = (AuthenticatedUser) user;
        }

        return authUser;
    }

    /**
     */
    public static boolean hasAuthority(final String aAuthority) {
        return getAuthorities().contains(aAuthority);
    }

    /**
     */
    public static boolean isAuthenticated() {
        boolean result = false;
        if (getAuthentication() != null) {
            result = true;
        }
        return result;
    }

}
