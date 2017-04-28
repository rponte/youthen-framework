package com.youthen.framework.persistence.dao.impl;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import com.youthen.framework.persistence.dao.EntityDaoExtender;

/**
 * DAO的AOP。
 * 
 * @author Lixin
 */
public class EntityDaoExtenderIntroductionAdvisor extends DefaultIntroductionAdvisor {

    private static final long serialVersionUID = 5638240335795930528L;

    private static ClassFilter sFilter = new ClassFilter() {

        @Override
        public boolean matches(final Class<?> aClazz) {
            return EntityDaoExtender.class.isAssignableFrom(aClazz);
        }
    };

    public EntityDaoExtenderIntroductionAdvisor() {
        super(new EntityDaoExtenderIntroductionInterceptor());
    }

    @Override
    public ClassFilter getClassFilter() {
        return sFilter;
    }

}
