// ============================================================
// Copyright(c) Pro-Ship Incorporated All Right Reserved.
// File: $Id: DataSourceProxy.java 2159 2010-12-20 12:01:45Z m.hirabayashi $
// ============================================================
package com.youthen.framework.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.youthen.framework.common.SystemError;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 */
public class DataSourceProxy implements DataSource {

    @Autowired
    private ApplicationContext context;

    /**
     */
    public static String getDataSourceName() {
        return "ds" + "000";

        /*
         * if (SessionContext.getUser() == null) {
         * return "ds" + "000";
         * } else {
         * return "ds" + SessionContext.getUser().getCompanyCode();
         * }
         */

    }

    /**
     */
    private DataSource getDataSource() {
        final String dsName = getDataSourceName();
        try {
            return this.context.getBean(dsName, DataSource.class);
        } catch (final BeansException ex) {
            throw new SystemError(new FieldSupportedMessage("XFW70006").format(dsName), ex);
        }
    }

    /**
     */
    @Override
    public Connection getConnection() throws SQLException {
        return this.getDataSource().getConnection();

    }

    /**
     */
    @Override
    public Connection getConnection(final String aUsername, final String aPassword) throws SQLException {
        return this.getDataSource().getConnection(aUsername, aPassword);
    }

    /**
     * </ul>
     * 
     * @see javax.sql.CommonDataSource#getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.getDataSource().getLogWriter();
    }

    /**
     */
    @Override
    public void setLogWriter(final PrintWriter aOut) throws SQLException {
        this.getDataSource().setLogWriter(aOut);
    }

    /**
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return this.getDataSource().getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(final int aSeconds) throws SQLException {
        this.getDataSource().setLoginTimeout(aSeconds);

    }

    /**
     */
    @Override
    public boolean isWrapperFor(final Class<?> aIF) throws SQLException {
        return this.getDataSource().isWrapperFor(aIF);
    }

    /**
     */
    @Override
    public <T> T unwrap(final Class<T> aIF) throws SQLException {
        return this.getDataSource().unwrap(aIF);
    }

    /**
     * @see javax.sql.CommonDataSource#getParentLogger()
     */

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
