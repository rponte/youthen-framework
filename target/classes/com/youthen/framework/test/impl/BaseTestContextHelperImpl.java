package com.youthen.framework.test.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import com.youthen.framework.common.security.AuthenticatedUser;
import com.youthen.framework.common.security.SisqpFwPrincipal;
import com.youthen.framework.test.BaseTestContextHelper;

public class BaseTestContextHelperImpl implements BaseTestContextHelper {

    @Override
    public void buildupSessionContext(final String aUserid, final String aKaishacd, final String aPassword,
            final String[] aAuths) {

        final SisqpFwPrincipal principal = new SisqpFwPrincipal(aUserid);
        final Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for (final String auth : aAuths) {
            auths.add(new GrantedAuthorityImpl(auth));
        }

        final AuthenticatedUser user =
                new AuthenticatedUser(principal, aPassword, true,
                        true, true, true, auths, 0);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, aPassword, auths));
        SecurityContextHolder.setContext(context);

    }
}
