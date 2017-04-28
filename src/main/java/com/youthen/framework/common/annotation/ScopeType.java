package com.youthen.framework.common.annotation;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
final class ScopeType {

    static final String SINGLETON = "singleton";

    static final String PROTOTYPE = "prototype";

    static final String SESSION = "session";

    static final String REQUEST = "request";

    private ScopeType() {
    }

}
