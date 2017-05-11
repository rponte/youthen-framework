package com.youthen.framework.presentation.action;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;
import com.youthen.framework.common.AppMessageHolder;
import com.youthen.framework.common.SimpleAppMessage;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.exception.BusinessException;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
@Controller
@Results({
        @Result(name = AbstractAjaxAction.INTERNAL_ERROR, type = "json", params = {
                "statusCode", "500",
                "ignoreHierarchy", "false",
                "root", "errorMessage"}),
        @Result(name = AbstractAjaxAction.SECURITY_ERROR, type = "json", params = {
                "statusCode", "403",
                "ignoreHierarchy", "false",
                "root", "errorMessage"}),
        @Result(name = AbstractAjaxAction.BUSINESS_ERROR, type = "json", params = {
                "statusCode", "200",
                "ignoreHierarchy", "false",
                "root", "errorMessage"}),
        @Result(name = AbstractAjaxAction.JSON_SUCCESS, type = "json", params = {
                "root", "resultBean",
                "ignoreHierarchy", "false",
                "contentType", "text/html",
                "statusCode", "200"})})
public abstract class AbstractAjaxAction extends BaseAction {

    /**
     * INTERNAL_ERROR。
     */
    public static final String INTERNAL_ERROR = "InternalError";
    /**
     * BUSINESS_ERROR。
     */
    public static final String BUSINESS_ERROR = "BusinessError";
    /**
     * SECURITY_ERROR。
     */
    public static final String SECURITY_ERROR = "SecurityError";

    /**
     * JSON结果返回
     */
    public static final String JSON_SUCCESS = "json-success";
    /** serial UID。 */
    private static final long serialVersionUID = 1L;
    /** log */
    private static final Log LOG = LogFactory.getLog(AbstractAjaxAction.class);
    /**
     * error message
     */
    private AppMessageHolder errorMessage;

    /** 参数 */
    private String args;

    /** 方法名 */
    private String method;

    /** 转变为JSON形式的Bean */
    private Object resultBean;

    protected Class<?> getArgsClass() {
        return String.class;
    }

    @Override
    @Action(interceptorRefs = @InterceptorRef("youthenDefaultAjaxStack"))
    public String execute() throws Exception {
        return this.doExecutor(new Executor() {

            @Override
            public Object doExecute(final Object aArgs) throws Exception {
                return AbstractAjaxAction.this.doExecute(aArgs);
            }
        });
    }

    protected final String doExecutor(final Executor aExecuter) throws Exception {
        try {
            final Object json = processParams(this.getArgs());
            if (this.method == null) this.resultBean = aExecuter.doExecute(json);
            else {
                final Class<? extends AbstractAjaxAction> clazz = this.getClass();
                this.resultBean = clazz.getMethod(this.method).invoke(this);
            }
            return AbstractAjaxAction.JSON_SUCCESS;
        } catch (final ApplicationException e) {
            this.setErrorMessage(e);
            LOG.error(e.getMessage(), e);
            return INTERNAL_ERROR;
        } catch (final BusinessException e) {
            this.setErrorMessage(e);
            LOG.debug(e.getMessage(), e);
            return BUSINESS_ERROR;
        } catch (final JSONException e) {
            LOG.error(e.getMessage(), e);
            this.setErrorMessage(new NotificationMessageBusinessException(new SimpleAppMessage("ICO00010")));
            return BUSINESS_ERROR;
        } catch (final Exception e) {
            this.setErrorMessage(new ApplicationException(e, "XFW55001"));
            LOG.error(e.getMessage(), e);
            return INTERNAL_ERROR;
        }

    }

    protected abstract Object doExecute(Object aArgs) throws Exception;

    protected Object processParams(final String aJSONString) throws BusinessException {
        if (aJSONString == null) {
            return null;
        }
        if (this.getArgsClass().equals(String.class)) {
            return trimQuote(aJSONString);
        }
        return validate(JSON.decode(validateJSON(aJSONString), this.getArgsClass()));

    }

    /**
     * 。
     * 
     * @param aJSONString
     * @return JSON文字列
     * @throws BusinessException
     */
    protected String validateJSON(final String aJSONString) throws BusinessException {
        return aJSONString;
    }

    protected Object validate(final Object aJson) throws BusinessException {
        return aJson;
    }

    private String trimQuote(final String aValue) {
        String result = trimPrefix("\"", aValue);
        result = trimPrefix("'", result);
        return result;
    }

    private String trimPrefix(final String aPrefix, final String aValue) {
        if (aValue.startsWith(aPrefix)) {
            return aValue.substring(1, aValue.length() - 1);
        }
        return aValue;
    }

    /**
     * getter for resultBean.
     * 
     * @return resultBean
     */
    public Object getResultBean() {
        return this.resultBean;
    }

    /**
     * setter for resultBean.
     * 
     * @param aResultBean resultBean
     */
    public void setResultBean(final Object aResultBean) {
        this.resultBean = aResultBean;
    }

    /**
     * getter for args.
     * 
     * @return args
     */
    public String getArgs() {
        return this.args;
    }

    /**
     * setter for args.
     * 
     * @param aArgs args
     */
    public void setArgs(final String aArgs) {
        this.args = aArgs;
    }

    /**
     * getter for errorMessage.
     * 
     * @return errorMessage
     */
    public AppMessageHolder getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * setter for errorMessage.
     * 
     * @param aErrorMessage errorMessage
     */
    public void setErrorMessage(final AppMessageHolder aErrorMessage) {
        this.errorMessage = aErrorMessage;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(final String aMethod) {
        this.method = aMethod;
    }

}
