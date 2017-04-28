package com.youthen.framework.persistence.entity;

import java.io.Serializable;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class OperationLog implements Serializable {

    private static final long serialVersionUID = -768385016777263761L;

    private String loggingTime;
    private String logLevel;
    private String hostName;
    private String threadName;
    private String companyCode;
    private String userid;
    private String functionName;
    private String operationType;
    private String message;

    public String getLoggingTime() {
        return this.loggingTime;
    }

    /**
     * setter for loggingTime.
     * 
     * @param aLoggingTime loggingTime
     */
    public void setLoggingTime(final String aLoggingTime) {
        this.loggingTime = aLoggingTime;
    }

    /**
     * getter for logLevel.
     * 
     * @return logLevel
     */
    public String getLogLevel() {
        return this.logLevel;
    }

    /**
     * setter for logLevel.
     * 
     * @param aLogLevel logLevel
     */
    public void setLogLevel(final String aLogLevel) {
        this.logLevel = aLogLevel;
    }

    /**
     * getter for hostName.
     * 
     * @return hostName
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * setter for hostName.
     * 
     * @param aHostName hostName
     */
    public void setHostName(final String aHostName) {
        this.hostName = aHostName;
    }

    /**
     * getter for threadName.
     * 
     * @return threadName
     */
    public String getThreadName() {
        return this.threadName;
    }

    /**
     * setter for threadName.
     * 
     * @param aThreadName threadName
     */
    public void setThreadName(final String aThreadName) {
        this.threadName = aThreadName;
    }

    /**
     * getter for companyCode.
     * 
     * @return companyCode
     */
    public String getCompanyCode() {
        return this.companyCode;
    }

    /**
     * setter for companyCode.
     * 
     * @param aCompanyCode companyCode
     */
    public void setCompanyCode(final String aCompanyCode) {
        this.companyCode = aCompanyCode;
    }

    /**
     * getter for userid.
     * 
     * @return userid
     */
    public String getUserid() {
        return this.userid;
    }

    /**
     * setter for userid.
     * 
     * @param aUserid userid
     */
    public void setUserid(final String aUserid) {
        this.userid = aUserid;
    }

    /**
     * getter for functionName.
     * 
     * @return functionName
     */
    public String getFunctionName() {
        return this.functionName;
    }

    /**
     * setter for functionName.
     * 
     * @param aFunctionName functionName
     */
    public void setFunctionName(final String aFunctionName) {
        this.functionName = aFunctionName;
    }

    /**
     * getter for operationType.
     * 
     * @return operationType
     */
    public String getOperationType() {
        return this.operationType;
    }

    /**
     * setter for operationType.
     * 
     * @param aOperationType operationType
     */
    public void setOperationType(final String aOperationType) {
        this.operationType = aOperationType;
    }

    /**
     * getter for message.
     * 
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * setter for message.
     * 
     * @param aMessage message
     */
    public void setMessage(final String aMessage) {
        this.message = aMessage;
    }

}
