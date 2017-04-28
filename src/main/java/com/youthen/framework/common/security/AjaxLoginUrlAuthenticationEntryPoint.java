package com.youthen.framework.common.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private AjaxRequestDetector ajaxRequestDetector = new AjaxRequestDetector();

    /**
     * @see org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */

    @Override
    public void commence(final HttpServletRequest aRequest, final HttpServletResponse aResponse,
            final AuthenticationException aAuthException) throws IOException, ServletException {

        if (this.ajaxRequestDetector.isAjaxRequest(aRequest)) {

            aResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            super.commence(aRequest, aResponse, aAuthException);
        }
    }

    /**
     * setter for ajaxRequestDetector.
     * 
     * @param aAjaxRequestDetector ajaxRequestDetector
     */
    public void setAjaxRequestDetector(final AjaxRequestDetector aAjaxRequestDetector) {
        this.ajaxRequestDetector = aAjaxRequestDetector;
    }

}
