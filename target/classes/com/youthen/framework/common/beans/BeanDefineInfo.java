package com.youthen.framework.common.beans;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import com.youthen.framework.common.SimpleAppMessage;
import com.youthen.framework.common.SystemError;
import com.youthen.framework.common.annotation.Common;

/**
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
@Common.Singleton
@Lazy(false)
public final class BeanDefineInfo {

    private static ApplicationContext sApplicationContext;

    private static PropertiesFactoryBean sFrameworkPropertyBean;

    private static PropertiesFactoryBean sPropertyBean;

    private BeanDefineInfo() {
    }

    @SuppressWarnings("unused")
    @Autowired
    private void setApplicationContext(final ApplicationContext aApplicationContext) {
        sApplicationContext = aApplicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return sApplicationContext;
    }

    /**
     * @SuppressWarnings("unused")
     * @Autowired
     * @Qualifier(value = "frameworkPropertyBean")
     *                  private void setFrameworkPropertyBean(final PropertiesFactoryBean aFrameworkPropertyBean) {
     *                  sFrameworkPropertyBean = aFrameworkPropertyBean;
     *                  }
     *                  /**
     */
    public static PropertiesFactoryBean getFrameworkPropertyBean() {
        return sFrameworkPropertyBean;
    }

    /**
     */
    @SuppressWarnings("unused")
    @Autowired(required = false)
    @Qualifier(value = "propertyBean")
    private void setPropertyBean(final PropertiesFactoryBean aPropertyBean) {
        sPropertyBean = aPropertyBean;
    }

    /**
     */
    public static PropertiesFactoryBean getPropertyBean() {
        return sPropertyBean;
    }

    /**
     */
    public static Properties getFrameworkProperties() {
        try {
            if (sFrameworkPropertyBean != null) {
                return getFrameworkPropertyBean().getObject();
            }
            return null;
        } catch (final IOException e) {
            throw new SystemError(new SimpleAppMessage("framework的properties文件读入失败！。"));
        }
    }

    public static Properties getProperties() {
        try {
            if (sPropertyBean != null) {
                return getPropertyBean().getObject();
            }
            return null;
        } catch (final IOException e) {
            throw new SystemError(new SimpleAppMessage("customer的properties文件读入失败！。"));
        }
    }
}
