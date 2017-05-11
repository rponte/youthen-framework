// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service.dto;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 。
 * 
 * @author LiXin
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
public abstract class BaseDto implements Serializable {

    private Long versionNo;

    private String updId;

    private String updTime;

    // 审查追踪修改内容
    String changedContent;

    // 审查追踪修改原因
    String reason;
    String cntReason;

    String actionName;

    String objectName;

    String tableName;

    List logList;

    abstract public Serializable getId();

    /**
     * getter for versionNo.
     * 
     * @return versionNo
     */
    public Long getVersionNo() {
        return this.versionNo;
    }

    /**
     * setter for versionNo.
     * 
     * @param aVersionNo versionNo
     */
    public void setVersionNo(final Long aVersionNo) {
        this.versionNo = aVersionNo;
    }

    /**
     * getter for updId.
     * 
     * @return updId
     */
    public String getUpdId() {
        return this.updId;
    }

    /**
     * setter for updId.
     * 
     * @param aUpdId updId
     */
    public void setUpdId(final String aUpdId) {
        this.updId = aUpdId;
    }

    /**
     * getter for updTime.
     * 
     * @return updTime
     */
    public String getUpdTime() {
        return this.updTime;
    }

    /**
     * setter for updTime.
     * 
     * @param aUpdTime updTime
     */
    public void setUpdTime(final String aUpdTime) {
        this.updTime = aUpdTime;
    }

    /**
     * getter for changedContent.
     * 
     * @return changedContent
     */
    public String getChangedContent() {
        return this.changedContent;
    }

    /**
     * setter for changedContent.
     * 
     * @param aChangedContent changedContent
     */
    public void setChangedContent(final String aChangedContent) {
        this.changedContent = aChangedContent;
    }

    /**
     * getter for reason.
     * 
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * setter for reason.
     * 
     * @param aReason reason
     */
    public void setReason(final String aReason) {
        this.reason = aReason;
    }

    @Override
    public int hashCode() {
        return 0; // HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object aObject) {
        return EqualsBuilder.reflectionEquals(this, aObject);
    }

    /**
     * getter for actionName.
     * 
     * @return actionName
     */
    public String getActionName() {
        return this.actionName;
    }

    /**
     * setter for actionName.
     * 
     * @param aActionName actionName
     */
    public void setActionName(final String aActionName) {
        this.actionName = aActionName;
    }

    /**
     * getter for objectName.
     * 
     * @return objectName
     */
    public String getObjectName() {
        return this.objectName;
    }

    /**
     * setter for objectName.
     * 
     * @param aObjectName objectName
     */
    public void setObjectName(final String aObjectName) {
        this.objectName = aObjectName;
    }

    /**
     * getter for tableName.
     * 
     * @return tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * setter for tableName.
     * 
     * @param aTableName tableName
     */
    public void setTableName(final String aTableName) {
        this.tableName = aTableName;
    }

    /**
     * getter for logList.
     * 
     * @return logList
     */
    public List getLogList() {
        return this.logList;
    }

    /**
     * setter for logList.
     * 
     * @param aLogList logList
     */
    public void setLogList(final List aLogList) {
        this.logList = aLogList;
    }

    /**
     * getter for cntReason.
     * 
     * @return cntReason
     */
    public String getCntReason() {
        return this.cntReason;
    }

    /**
     * setter for cntReason.
     * 
     * @param aCntReason cntReason
     */
    public void setCntReason(final String aCntReason) {
        this.cntReason = aCntReason;
    }

}
