package com.youthen.framework.util.logging;

import java.util.Locale;
import org.apache.log4j.or.ObjectRenderer;
import com.youthen.framework.common.AppMessage;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpLogRenderer implements ObjectRenderer {

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     */
    @Override
    public String doRender(final Object aO) {
        if (aO instanceof FieldSupportedMessage) {
            final AppMessage appMessage = (AppMessage) aO;
            return appMessage.toString(Locale.getDefault());
        }

        return aO.toString();
    }

}
