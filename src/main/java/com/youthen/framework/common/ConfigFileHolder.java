/**
 * ISSG Sony China 2010
 */
package com.youthen.framework.common;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * 读取配置文件的工具类
 * 
 * @author 杨新伦
 * @version $Revision: $<br>
 *          $Date: 2014-07-28
 */
public final class ConfigFileHolder {

    private static Configuration config;

    private ConfigFileHolder() {

    }

    public static void setFilePath(final String filePath) {
        try {
            if (filePath.endsWith(".properties")) {
                config = new PropertiesConfiguration(filePath);
            }
            if (filePath.endsWith(".xml")) {
                config = new XMLConfiguration(filePath);
            }
        } catch (final ConfigurationException e) {

        }
    }

    /**
     * 从配置文件中读取Key对应的整数值
     * 
     * @param key
     * @return 整数
     */
    public static int getInt(final String key) {
        if (config == null) {
            throw new RuntimeException("请首先调用setFilePath(String filePath)方法,进行配置文件的初始化操作");
        }
        return config.getInt(key);
    }

    /**
     * 从配置文件中读取Key对应的整数值数组
     * 
     * @param key
     * @return 整数数组
     */
    public static int[] getIntArray(final String key) {
        if (config == null) {
            throw new RuntimeException("请首先调用setFilePath(String filePath)方法,进行配置文件的初始化操作");
        }
        final String[] strArray = config.getStringArray(key);
        final int[] intArr = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArr[i] = Integer.parseInt(strArray[i]);
        }
        return intArr;
    }

    /**
     * 从配置文件中读取Key对应的字符串值
     * 
     * @param key
     * @return 字符串值
     */
    public static String getString(final String key) {
        if (config == null) {
            throw new RuntimeException("请首先调用setFilePath(String filePath)方法,进行配置文件的初始化操作");
        }
        return config.getString(key);
    }

    public static String getString(final String key, final String[] param) {
        if (config == null) {
            throw new RuntimeException("请首先调用setFilePath(String filePath)方法,进行配置文件的初始化操作");
        }

        String source = config.getString(key);
        if (param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                source = source.replace("{" + i + "}", param[i]);
            }
        }

        return source;

    }

    /**
     * 从配置文件中读取Key对应的字符串值数组
     * 
     * @param key
     * @return 字符串值数组
     */
    public static String[] getStringArray(final String key) {
        if (config == null) {
            throw new RuntimeException("请首先调用setFilePath(String filePath)方法,进行配置文件的初始化操作");
        }
        return config.getStringArray(key);
    }

    public static void main(final String[] args) {
        /*
         * ConfigFileHolder.setFilePath("src/test/resources/smtp-test.properties");
         * final String[] a = new String[2];
         * a[0] = "fdf";
         * a[1] = "ddd";
         * final String c = getString("smtp.password", a);
         * System.out.println(c);
         */

    }

}
