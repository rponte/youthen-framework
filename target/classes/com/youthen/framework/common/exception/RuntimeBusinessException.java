package com.youthen.framework.common.exception;

import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.AppMessageHolder;
import com.youthen.framework.common.SimpleAppMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class RuntimeBusinessException extends RuntimeException implements AppMessageHolder {

    private static final long serialVersionUID = 3750641619927045832L;

    private final AppMessage appMessage;

    public RuntimeBusinessException(final Throwable aThrow, final String aCode, final Object... aObjects) {
        super(aThrow);
        this.appMessage = new SimpleAppMessage(aCode, aObjects);
    }

    public RuntimeBusinessException(final String aCode, final Object... aObjects) {
        this(null, aCode, aObjects);
    }

    public RuntimeBusinessException(final AppMessage aMessage) {
        this.appMessage = aMessage;
    }

    public RuntimeBusinessException(final AppMessage aMessage, final Throwable aCause) {
        super(aCause);
        this.appMessage = aMessage;
    }

    public RuntimeBusinessException(final BusinessException aCause) {
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

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException();
    }

}
