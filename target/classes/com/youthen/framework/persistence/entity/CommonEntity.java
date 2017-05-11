package com.youthen.framework.persistence.entity;

import java.io.Serializable;
import java.util.List;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public interface CommonEntity extends Serializable {

    /*
     * void setCreaterId(String createrId);
     * String getCreaterId();
     */

    /*
     * Long getCompanyId();
     * void setCompanyId(Long companyId);
     * void setCreateDate(Date date);
     * Date getCreateDate();
     */

    Serializable getId();

    /**
     * getter for versionno.
     * 
     * @return versionno
     */
    Long getVersionNo();

    /**
     * setter for versionno.
     * 
     * @param aVersionNo versionno
     */
    void setVersionNo(Long aVersionNo);

    /**
     * getter for updid.
     * 
     * @return updid
     */
    String getUpdId();

    /**
     * setter for updid.
     * 
     * @param aUpdId updid
     */
    void setUpdId(String aUpdId);

    /**
     * getter for upditme.
     * 
     * @return updtime
     */
    String getUpdTime();

    /**
     * setter for updtime.
     * 
     * @param aUpdTime updtime
     */
    void setUpdTime(String aUpdTime);

    /**
     * getter for changedContent.
     * 
     * @return changedContent
     */
    public String getChangedContent();

    /**
     * setter for changedContent.
     * 
     * @param aChangedContent changedContent
     */
    public void setChangedContent(String aChangedContent);

    /**
     * getter for reason.
     * 
     * @return reason
     */
    public String getReason();

    /**
     * setter for reason.
     * 
     * @param aReason reason
     */
    public void setReason(String aReason);

    /**
     * getter for actionName.
     * 
     * @return actionName
     */
    public String getActionName();

    /**
     * setter for actionName.
     * 
     * @param aActionName actionName
     */
    public void setActionName(String aActionName);

    /**
     * getter for objectName.
     * 
     * @return objectName
     */
    public String getObjectName();

    /**
     * setter for objectName.
     * 
     * @param aObjectName objectName
     */
    public void setObjectName(String aObjectName);

    /**
     * getter for tableName.
     * 
     * @return tableName
     */
    public String getTableName();

    /**
     * setter for tableName.
     * 
     * @param aTableName tableName
     */
    public void setTableName(String aTableName);

    /**
     * getter for sectionId.
     * 
     * @return sectionId
     */
    public String getSectionId();

    /**
     * setter for sectionId.
     * 
     * @param aSectionId sectionId
     */
    public void setSectionId(String aSectionId);

    /**
     * getter for logList.
     * 
     * @return logList
     */
    public List getLogList();

    /**
     * setter for logList.
     * 
     * @param aLogList logList
     */
    public void setLogList(final List aLogList);

    public int getSysAndArea();

}
