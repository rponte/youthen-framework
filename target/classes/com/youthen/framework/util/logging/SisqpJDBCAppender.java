package com.youthen.framework.util.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;
import com.youthen.framework.common.beans.BeanDefineInfo;
import com.youthen.framework.common.fields.FieldSupportedMessage;
import com.youthen.framework.persistence.entity.OperationLog;
import com.youthen.framework.util.DataSourceProxy;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpJDBCAppender extends JDBCAppender {

    private static final String MESSAGE_KEY_SQL_EXECUTE_ERROR = "XFW72009";
    private static String sHostName;

    static {
        try {
            sHostName = InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            sHostName = "nowhere";
        }
    }

    /**
     */
    public SisqpJDBCAppender() {
        super();
    }

    /**
     * @see org.apache.log4j.jdbc.JDBCAppender#execute(java.lang.String)
     */
    @Override
    protected void execute(final String aSql) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(aSql);
            for (int i = 0; i < this.buffer.size(); i++) {
                final LoggingEvent event = (LoggingEvent) this.buffer.get(i);
                final OperationLog oplog = this.createOperationLog(event);

                int parameterIndex = 0;
                pstmt.setString(++parameterIndex, oplog.getLoggingTime());
                pstmt.setString(++parameterIndex, oplog.getLogLevel());
                pstmt.setString(++parameterIndex, oplog.getHostName());
                pstmt.setString(++parameterIndex, oplog.getThreadName());
                pstmt.setString(++parameterIndex, oplog.getCompanyCode());
                pstmt.setString(++parameterIndex, oplog.getUserid());
                pstmt.setString(++parameterIndex, oplog.getFunctionName());
                pstmt.setString(++parameterIndex, oplog.getOperationType());
                pstmt.setString(++parameterIndex, oplog.getMessage());
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (final SQLException e) {
            LogLog.error(new FieldSupportedMessage(MESSAGE_KEY_SQL_EXECUTE_ERROR).getMesg());
            LogLog.error(e.getMessage());
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

    /**
     * @see org.apache.log4j.jdbc.JDBCAppender#getConnection()
     */
    @Override
    protected Connection getConnection() throws SQLException {

        final DataSourceProxy dataSourceProxy =
                BeanDefineInfo.getApplicationContext().getBean(DataSourceProxy.class);
        return dataSourceProxy.getConnection();
    }

    /**
     * @see org.apache.log4j.jdbc.JDBCAppender#append(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    public void append(final LoggingEvent aEvent) {
        aEvent.setProperty("hostname", sHostName);
        super.append(aEvent);
    }

    /**
     */
    private OperationLog createOperationLog(final LoggingEvent aLoggingEvent) {
        final OperationLog oplog = new OperationLog();

        oplog.setLoggingTime(new Timestamp(aLoggingEvent.getTimeStamp()).toString());
        oplog.setLogLevel(aLoggingEvent.getLevel().toString());
        oplog.setHostName(aLoggingEvent.getProperty("hostName"));
        oplog.setThreadName(aLoggingEvent.getThreadName());
        oplog.setCompanyCode(aLoggingEvent.getProperty("operationCompanyCode"));
        oplog.setUserid(aLoggingEvent.getProperty("operationUserId"));
        oplog.setFunctionName(aLoggingEvent.getProperty("requestFunctionCd"));
        oplog.setOperationType(aLoggingEvent.getProperty("requestType"));
        oplog.setMessage(aLoggingEvent.getMessage().toString());

        return oplog;
    }

}
