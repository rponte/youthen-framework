package com.youthen.framework.common.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import com.youthen.framework.common.SimpleAppMessage;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.common.SystemError;
import com.youthen.framework.common.beans.BeanDefineInfo;
import com.youthen.framework.common.exception.ApplicationException;

public class SisqpFwUserDetailsManager extends JdbcUserDetailsManager {

    @Override
    protected List<UserDetails> loadUsersByUsername(final String aUsername) {
        try {
            final SisqpFwPrincipal principal = new SisqpFwPrincipal(aUsername);
            this.setDataSource(principal);

            return this.getJdbcTemplate().query(
                    this.getUsersByUsernameQuery(),
                    new String[] {principal.getUserId()},
                    new RowMapper<UserDetails>() {

                        public UserDetails mapRow(final ResultSet aRs, final int aRowNum) throws SQLException {
                            final Date currentDate =
                                    DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.DATE);

                            final String password = aRs.getString(2);
                            final boolean enabled = true;
                            final boolean accountNonLocked = aRs.getBoolean(4);
                            final int passwordErrorCount = 0;
                            /*
                             * final String passwordLimit = aRs.getString(4);
                             * boolean credentialNonExpired = true;
                             * if (StringUtils.isNotEmpty(passwordLimit)) {
                             * credentialNonExpired =
                             * !(DateFormatUtils.parse("yyyyMMdd", passwordLimit).before(currentDate));
                             * }
                             * final String accountLimit = aRs.getString(5);
                             * boolean accountNonExpired = true;
                             * if (StringUtils.isNotEmpty(accountLimit)) {
                             * accountNonExpired =
                             * !(DateFormatUtils.parse("yyyyMMdd", accountLimit).before(currentDate));
                             * }
                             */

                            return new AuthenticatedUser(
                                    principal,
                                    password,
                                    enabled,
                                    accountNonLocked,
                                    true,
                                    true,
                                    AuthorityUtils.NO_AUTHORITIES, passwordErrorCount);
                        }

                    });
        } catch (final SystemError ex) {
            throw new RecoverableDataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(final String aUsername) {
        try {
            final SisqpFwPrincipal principal = new SisqpFwPrincipal(aUsername);
            this.setDataSource(principal);

            return this.getJdbcTemplate().query(
                    this.getAuthoritiesByUsernameQuery(),
                    new String[] {principal.getUserId()},
                    new RowMapper<GrantedAuthority>() {

                        @SuppressWarnings("synthetic-access")
                        public GrantedAuthority mapRow(final ResultSet aRs, final int aRowNum) throws SQLException {
                            final String roleName = getRolePrefix() + aRs.getString(2);
                            final GrantedAuthorityImpl authority = new GrantedAuthorityImpl(roleName);
                            return authority;
                        }
                    });
        } catch (final SystemError ex) {
            throw new RecoverableDataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    protected UserDetails createUserDetails(final String aUsername, final UserDetails aUserFromUserQuery,
            final List<GrantedAuthority> aCombinedAuthorities) {
        AuthenticatedUser user = null;
        if (aUserFromUserQuery instanceof AuthenticatedUser) {
            user = (AuthenticatedUser) aUserFromUserQuery;
        } else {
            throw new ApplicationException(new SimpleAppMessage("XFW90004"));
        }

        return new AuthenticatedUser(
                new SisqpFwPrincipal(aUsername),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                aCombinedAuthorities, user.getPasswordErrorCount());
    }

    private void setDataSource(final SisqpFwPrincipal aPrincipal) {
        final String companyCode = aPrincipal.getCompanyCode();
        if (StringUtils.isEmpty(companyCode)) {
            // throw new InvalidKaishacdException(new SimpleAppMessage("XFW73002").toString());
        }
        final String dsName = "ds" + companyCode;
        try {
            final ApplicationContext context = BeanDefineInfo.getApplicationContext();
            final DataSource dataSource = context.getBean(dsName, DataSource.class);
            setDataSource(dataSource);
        } catch (final BeansException e) {
            // throw new InvalidKaishacdException(new SimpleAppMessage("XFW70006").format(dsName).toString(), e);
        }
    }
}
