package com.youthen.framework.common;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SystemError extends RuntimeException {

    private static final long serialVersionUID = -2271168268497534199L;

    private final AppMessage appMessage;

    public SystemError(final AppMessage aMessage) {
        this.appMessage = aMessage;
    }

    public SystemError(final AppMessage aMessage, final Throwable aCause) {
        super(aCause);
        this.appMessage = aMessage;
    }

    public AppMessage getAppMessage() {
        return this.appMessage;
    }

    @Override
    public String getMessage() {
        return this.appMessage.toString();
    }
}
