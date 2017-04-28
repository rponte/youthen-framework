package com.youthen.framework.persistence.entity;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public abstract class AbstractCommonEntity implements CommonEntity {

    private static final long serialVersionUID = 4795797890904133831L;

    private Long versionNo = 0l;

    private String updId;

    private String updTime;

    // 审查追踪修改内容
    String changedContent;

    // 审查追踪修改原因
    String reason;

    String actionName;

    String objectName;

    String tableName;

    String sectionId;

    List logList;

    @Override
    public Long getVersionNo() {
        return this.versionNo;
    }

    @Override
    public void setVersionNo(final Long aVersionNo) {
        this.versionNo = aVersionNo;
    }

    @Override
    public String getUpdId() {
        return this.updId;
    }

    @Override
    public void setUpdId(final String aUpdId) {
        this.updId = aUpdId;
    }

    @Override
    public String getUpdTime() {
        return this.updTime;
    }

    @Override
    public void setUpdTime(final String aUpdTime) {
        this.updTime = aUpdTime;
    }

    @Override
    public String toString() {
        return "";
        // return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(final Object aObject) {
        return EqualsBuilder.reflectionEquals(this, aObject);
    }

    @Override
    public int hashCode() {
        return 0;// HashCodeBuilder.reflectionHashCode(this);
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
     * getter for sectionId.
     * 
     * @return sectionId
     */
    public String getSectionId() {
        return this.sectionId;
    }

    /**
     * setter for sectionId.
     * 
     * @param aSectionId sectionId
     */
    public void setSectionId(final String aSectionId) {
        this.sectionId = aSectionId;
    }

    /**
     * getter for logList.
     * 
     * @return logList
     */
    @Override
    public List getLogList() {
        return this.logList;
    }

    /**
     * setter for logList.
     * 
     * @param aLogList logList
     */
    @Override
    public void setLogList(final List aLogList) {
        this.logList = aLogList;
    }

    @Override
    public int getSysAndArea() {
        return 0;
    }
}
