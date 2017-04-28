package com.youthen.framework.common.security;

import javax.servlet.http.HttpServletRequest;

public class AjaxRequestDetector {

    public boolean isAjaxRequest(final HttpServletRequest aRequest) {
        if (aRequest == null) {
            return false;
        }
        if ("XMLHttpRequest".equals(aRequest.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }
}
