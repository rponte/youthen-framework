package com.youthen.framework.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */

@Component
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Scope(ScopeType.PROTOTYPE)
public @interface BusinessLogic {

    @Component
    @Target(value = ElementType.TYPE)
    @Retention(value = RetentionPolicy.RUNTIME)
    @Documented
    @Scope(ScopeType.SINGLETON)
    public @interface Singleton {

        String value() default "";
    }

    String value() default "";
}
