package com.youthen.framework.presentation.action;

import java.util.Arrays;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.youthen.framework.common.SimpleAppMessage;
import com.youthen.framework.common.context.SessionContext;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.security.AuthenticatedUser;

public class BaseAction extends ActionSupport implements Preparable {

    private static final long serialVersionUID = -903679144271522857L;

    public static final String LIST = "list";

    AuthenticatedUser sessionUser;

    /** log */
    @SuppressWarnings("hiding")
    private static final Log LOG = LogFactory.getLog(BaseAction.class);

    /**
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception {
        final HttpServletRequest aRequest = ServletActionContext.getRequest();
        final Locale local = (Locale) aRequest.getSession().getAttribute("WW_TRANS_I18N_LOCALE");
        ActionContext.getContext().setLocale(local);
    }

    public AuthenticatedUser getSessionUser() {
        return this.sessionUser = SessionContext.getUser();
    }

    /**
     * 取得 AppMessage
     * 
     * @param aArgs 第一个参数AppCode名
     * @return　国際化了的AppMessage
     */
    public String getAppMessage(final Object... aArgs) {
        if (aArgs.length <= 0) {
            throw new ApplicationException("XCO00004", this.getClass().getName(), "getAppMessage");
        }
        final String message =
                new SimpleAppMessage(aArgs[0].toString(), Arrays.copyOfRange(aArgs, 1, aArgs.length)).getMesg();
        LOG.debug("appMessage:" + message);
        return message;
    }

    /**
     * @return hashCode
     */
    public int getUniqueId() {
        return super.hashCode();
    }

}
