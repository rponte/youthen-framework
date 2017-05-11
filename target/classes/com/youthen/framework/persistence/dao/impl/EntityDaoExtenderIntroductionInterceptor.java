package com.youthen.framework.persistence.dao.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import com.youthen.framework.persistence.dao.EntityDao;
import com.youthen.framework.persistence.dao.EntityDaoExtender;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class EntityDaoExtenderIntroductionInterceptor implements IntroductionInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(final MethodInvocation aMethodInvocation) throws Throwable {

        if (this.isDeclaredInEntityDao(aMethodInvocation)) {
            return aMethodInvocation.proceed();
        }

        final EntityDaoExtender BaseDaoExtender = (EntityDaoExtender) aMethodInvocation.getThis();
        final Object[] arguments = aMethodInvocation.getArguments();
        final String methodName = aMethodInvocation.getMethod().getName();
        Object ret;
        if (methodName.startsWith("insert")) {
            BaseDaoExtender.executeInsert(arguments[0]);
            ret = null;
        } else {
            ret = aMethodInvocation.proceed();
        }
        return ret;
    }

    private boolean isDeclaredInEntityDao(final MethodInvocation aMethodInvocation) {
        if (EntityDao.class.equals(aMethodInvocation.getMethod().getDeclaringClass())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean implementsInterface(final Class<?> aIntf) {
        return aIntf.isInterface() && EntityDaoExtender.class.isAssignableFrom(aIntf);
    }
}
