package com.youthen.framework.common.exception;

import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.AppMessageHolder;
import com.youthen.framework.common.SimpleAppMessage;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class ApplicationException extends RuntimeException implements AppMessageHolder {

    private static final long serialVersionUID = -1056723638966592126L;

    private final AppMessage appMessage;

    public ApplicationException(final Throwable aT, final String aCode, final Object... aObjects) {
        super(aT);
        this.appMessage = new SimpleAppMessage(aCode, aObjects);
    }

    public ApplicationException(final String aCode, final Object... aObjects) {
        this(null, aCode, aObjects);
    }

    public ApplicationException(final AppMessage aMessage) {
        this.appMessage = aMessage;
    }

    public ApplicationException(final AppMessage aMessage, final Throwable aCause) {
        super(aCause);
        this.appMessage = aMessage;
    }

    public ApplicationException(final BusinessException aCause) {
        super(aCause);
        this.appMessage = aCause.getAppMessage();
    }

    public AppMessage getAppMessage() {
        return this.appMessage;
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return this.appMessage.toString();
    }

    /**
     * @see com.youthen.framework.common.AppMessageHolder#getClassName()
     */

    @Override
    public String getClassName() {
        return ApplicationException.class.getName();
    }

}
