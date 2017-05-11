package com.youthen.framework.presentation.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.common.security.AuthenticatedUser;
import com.youthen.framework.common.security.CredentialsLockedException;
import com.youthen.framework.common.security.SisqpFwPrincipal;

public class SisqpFwUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHENTICATION_ERROR_MESSAGE_KEY = "AUTHENTICATION_ERROR_MESSAGE";

    public static final String COMPANY_CODE_KEY = "COMPANY_CODE";
    public static final String USERID_KEY = "USERID";

    protected static final String RESULT_SUCCESS = "SUCCESS";

    protected static final String RESULT_FAIL = "FAIL";

    protected static final String STATUS_NOT_FOUND = "ACCOUNT_NOT_FOUND";

    protected static final String STATUS_PASSWORD_UNMATCH = "PASSWORD_UNMATCH";

    protected static final String STATUS_PASSWORD_EXPIRED = "PASSWORD_EXPIRED";

    protected static final String STATUS_PASSWORD_LOCKED = "PASSWORD_LOCKED";

    protected static final String STATUS_ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED";

    protected static final String STATUS_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";

    protected static final String STATUS_NORMAL = "NORMAL";

    protected static final String STATUS_UNKNOWN = "UNKNOWN";

    private static final String USER_AGENT = "User-Agent";

    private static final String MDC_KEY_SYSTEM_CD = "systemCd";
    private static final String MDC_KEY_IP_ADDRESS = "ipAddress";

    private static final String MDC_KEY_USER_AGENT = "userAgent";

    private static final String MDC_KEY_KAISHA_CD = "kaishaCd";

    private static final String MDC_KEY_USER_ID = "userId";

    private static final String MDC_KEY_ACCOUNT_STATUS = "accountStatus";

    private static final String MDC_KEY_LOGIN_RESULT = "loginResult";

    private static final String ACCOUNT_NOT_FOUND_MESSSAGE_CODE = "EFW56001";

    private static final String LOCKED_MESSAGE_CODE = "EFW56003";

    private static final String EXPIRED_MESSAGE_CODE = "EFW56004";

    private static final String ACCOUNT_MESSAGE_CODE = "EFW56005";

    private static final String PASSWORD_MESSAGE_CODE = "EFW56006";

    private static final String ADMIN_MESSAGE_CODE = "EFW56007";

    private static final String CHANGE_PASSWORD_MESSAGE_CODE = "EFW56008";

    private static final String SYSTEM_ERROR_MESSAGE_CODE = "EFW56009";

    private final Log systemLog = LogFactory.getLog(SisqpFwUsernamePasswordAuthenticationFilter.class);

    private final Log loginLog = LogFactory.getLog("login");

    protected void preProcessForSuccessfulAuthentication(final HttpServletRequest aRequest,
            final HttpServletResponse aResponse,
            final Authentication aAuthResult) throws IOException, ServletException {
    }

    protected void postProcessForSuccessfulAuthentication(final HttpServletRequest aRequest,
            final HttpServletResponse aResponse,
            final Authentication aAuthResult) throws IOException, ServletException {

        final SisqpFwPrincipal principal = new SisqpFwPrincipal(aAuthResult.getName());

        this.writeLoginLog(aRequest, principal, STATUS_NORMAL, RESULT_SUCCESS);

        final HttpSession session = aRequest.getSession(false);
        if (session != null) {
            session.removeAttribute(AUTHENTICATION_ERROR_MESSAGE_KEY);
            session.removeAttribute(COMPANY_CODE_KEY);
            session.removeAttribute(USERID_KEY);
            session.removeAttribute(SPRING_SECURITY_FORM_USERNAME_KEY);
            session.removeAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY);
            session.removeAttribute(SPRING_SECURITY_LAST_EXCEPTION_KEY);
            session.removeAttribute(SPRING_SECURITY_LAST_USERNAME_KEY);
        }

        // final AuthenticatedUser user = (AuthenticatedUser) aAuthResult.getPrincipal();
        // if (user.getPasswordErrorCount() != 0) {
        // // this.clearPasswordErrorCount(principal.getUserId());
        // }

    }

    /**
     * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    @Override
    protected void successfulAuthentication(final HttpServletRequest aRequest, final HttpServletResponse aResponse,
            final Authentication aAuthResult) throws IOException, ServletException {
        preProcessForSuccessfulAuthentication(aRequest, aResponse, aAuthResult);
        super.successfulAuthentication(aRequest, aResponse, aAuthResult);
        postProcessForSuccessfulAuthentication(aRequest, aResponse, aAuthResult);

    }

    protected void preProcessForUnsuccessfulAuthentication(final HttpServletRequest aRequest,
            final HttpServletResponse aResponse,
            final AuthenticationException aFailed) throws IOException, ServletException {

        final SisqpFwPrincipal principal = new SisqpFwPrincipal(aFailed.getAuthentication().getName());

        final StatusMessage statusMessage = this.getStatusMessage(aFailed);

        if (STATUS_PASSWORD_UNMATCH.equals(statusMessage.getStatus())) {
            this.setSecurityContext(aFailed);

            // this.incrementPasswordErrorCount(principal.getUserId());
        }

        this.writeLoginLog(aRequest, principal, statusMessage.getStatus(), RESULT_FAIL);

        final HttpSession session = aRequest.getSession(false);
        if (session != null) {
            session.setAttribute(AUTHENTICATION_ERROR_MESSAGE_KEY, statusMessage.getMessage());
            session.setAttribute(COMPANY_CODE_KEY, principal.getCompanyCode());
            session.setAttribute(USERID_KEY, principal.getUserId());
        }

    }

    protected void postProcessForUnsuccessfulAuthentication(final HttpServletRequest aRequest,
            final HttpServletResponse aResponse,
            final AuthenticationException aFailed) throws IOException, ServletException {

        final StatusMessage statusMessage = this.getStatusMessage(aFailed);

        if (STATUS_PASSWORD_EXPIRED.equals(statusMessage.getStatus())) {
            this.setSecurityContext(aFailed);

            try {
                final Class<?> c = aResponse.getClass().getSuperclass();
                final Field field = c.getDeclaredField("contextSaved");
                field.setAccessible(true);
                field.setBoolean(aResponse, false);
            } catch (final Exception e) {
                throw new ApplicationException(new FieldSupportedMessage("XFW56002"), e);
            }
        }

    }

    @Override
    /**
     * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    protected void unsuccessfulAuthentication(final HttpServletRequest aRequest, final HttpServletResponse aResponse,
            final AuthenticationException aFailed) throws IOException, ServletException {

        preProcessForUnsuccessfulAuthentication(aRequest, aResponse, aFailed);
        super.unsuccessfulAuthentication(aRequest, aResponse, aFailed);
        postProcessForUnsuccessfulAuthentication(aRequest, aResponse, aFailed);

    }

    protected StatusMessage getStatusMessage(final AuthenticationException aFailed) {
        String status;
        String errorMessage;
        if (aFailed instanceof UsernameNotFoundException) {
            status = STATUS_NOT_FOUND;
            errorMessage = new FieldSupportedMessage(ACCOUNT_NOT_FOUND_MESSSAGE_CODE).getMesg();
        } else if (aFailed instanceof BadCredentialsException) {
            status = STATUS_PASSWORD_UNMATCH;
            errorMessage = new FieldSupportedMessage(ACCOUNT_NOT_FOUND_MESSSAGE_CODE).getMesg();
        } else if (aFailed instanceof LockedException) {
            status = STATUS_ACCOUNT_LOCKED;
            errorMessage =
                    new FieldSupportedMessage(LOCKED_MESSAGE_CODE).format(
                            new FieldSupportedMessage(ACCOUNT_MESSAGE_CODE).getMesg(),
                            new FieldSupportedMessage(ADMIN_MESSAGE_CODE).getMesg()).getMesg();
        } else if (aFailed instanceof AccountExpiredException) {
            status = STATUS_ACCOUNT_EXPIRED;
            errorMessage =
                    new FieldSupportedMessage(EXPIRED_MESSAGE_CODE).format(
                            new FieldSupportedMessage(ACCOUNT_MESSAGE_CODE).getMesg(),
                            new FieldSupportedMessage(ADMIN_MESSAGE_CODE).getMesg()).getMesg();
        } else if (aFailed instanceof CredentialsLockedException) {
            status = STATUS_PASSWORD_LOCKED;
            errorMessage =
                    new FieldSupportedMessage(LOCKED_MESSAGE_CODE).format(
                            new FieldSupportedMessage(PASSWORD_MESSAGE_CODE).getMesg(),
                            new FieldSupportedMessage(ADMIN_MESSAGE_CODE).getMesg()).getMesg();
        } else if (aFailed instanceof CredentialsExpiredException) {
            status = STATUS_PASSWORD_EXPIRED;
            errorMessage =
                    new FieldSupportedMessage(EXPIRED_MESSAGE_CODE).format(
                            new FieldSupportedMessage(PASSWORD_MESSAGE_CODE).getMesg(),
                            new FieldSupportedMessage(CHANGE_PASSWORD_MESSAGE_CODE).getMesg()).getMesg();
        } else {
            status = STATUS_UNKNOWN;
            errorMessage =
                    new FieldSupportedMessage(SYSTEM_ERROR_MESSAGE_CODE).format(
                            new FieldSupportedMessage(ADMIN_MESSAGE_CODE).getMesg())
                            .getMesg();
            if (this.systemLog.isFatalEnabled()) {
                this.systemLog.fatal(new FieldSupportedMessage("XFW56001"), aFailed);
            }
        }
        return new StatusMessage(status, errorMessage);
    }

    protected void setSecurityContext(final AuthenticationException aFailed) {
        final AuthenticatedUser user = (AuthenticatedUser) aFailed.getExtraInformation();
        final Authentication auth = aFailed.getAuthentication();
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), user.getAuthorities());
        token.setDetails(auth.getDetails());
        token.setAuthenticated(false);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

    protected void writeLoginLog(final HttpServletRequest aRequest, final SisqpFwPrincipal aPrincipal,
            final String aStatus, final String aResult) {
        if (this.loginLog.isInfoEnabled()) {
            MDC.put(MDC_KEY_SYSTEM_CD, "youthen");
            MDC.put(MDC_KEY_IP_ADDRESS, aRequest.getRemoteAddr());
            MDC.put(MDC_KEY_USER_AGENT, aRequest.getHeader(USER_AGENT));
            // MDC.put(MDC_KEY_KAISHA_CD, aPrincipal.getCompanyCode());
            MDC.put(MDC_KEY_USER_ID, aPrincipal.getUserId());
            MDC.put(MDC_KEY_ACCOUNT_STATUS, aStatus);
            MDC.put(MDC_KEY_LOGIN_RESULT, aResult);

            this.loginLog.info(null);
        }
    }

    protected class StatusMessage {

        private final String status;

        private final String message;

        public StatusMessage(final String aStatus, final String aMessage) {
            this.status = aStatus;
            this.message = aMessage;
        }

        /**
         * getter for status.
         * 
         * @return status
         */
        public String getStatus() {
            return this.status;
        }

        /**
         * getter for message.
         * 
         * @return message
         */
        public String getMessage() {
            return this.message;
        }

    }
}
