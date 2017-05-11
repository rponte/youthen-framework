// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 。
 * 
 * @author Administrator
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
public class QueryAudittrailDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String userId; // 查询用户
    private String queryId; // 查询编号
    private String queryParameter; // 查询参数
    private String querySql; // 查询SQL
    private String queryResult; // 查询结果
    private Integer queryCount; // 查询结果总数
    private Date queryTime; // 查询时间
    private String bakColumn1;
    private String bakColumn2;
    private String bakColumn3;
    private String bakColumn4;
    private String bakColumn5;
    private String bakColumn6;
    private String bakColumn7;
    private String bakColumn8;
    private String bakColumn9;
    private String bakColumn10;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long aId) {
        this.id = aId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(final String aUserId) {
        this.userId = aUserId;
    }

    public String getQueryId() {
        return this.queryId;
    }

    public void setQueryId(final String aQueryId) {
        this.queryId = aQueryId;
    }

    public String getQueryParameter() {
        return this.queryParameter;
    }

    public void setQueryParameter(final String aQueryParameter) {
        this.queryParameter = aQueryParameter;
    }

    public String getQuerySql() {
        return this.querySql;
    }

    public void setQuerySql(final String aQuerySql) {
        this.querySql = aQuerySql;
    }

    public String getQueryResult() {
        return this.queryResult;
    }

    public void setQueryResult(final String aQueryResult) {
        this.queryResult = aQueryResult;
    }

    public Integer getQueryCount() {
        return this.queryCount;
    }

    public void setQueryCount(final Integer aQueryCount) {
        this.queryCount = aQueryCount;
    }

    public Date getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(final Date aQueryTime) {
        this.queryTime = aQueryTime;
    }

    public String getBakColumn1() {
        return this.bakColumn1;
    }

    public void setBakColumn1(final String aBakColumn1) {
        this.bakColumn1 = aBakColumn1;
    }

    public String getBakColumn2() {
        return this.bakColumn2;
    }

    public void setBakColumn2(final String aBakColumn2) {
        this.bakColumn2 = aBakColumn2;
    }

    public String getBakColumn3() {
        return this.bakColumn3;
    }

    public void setBakColumn3(final String aBakColumn3) {
        this.bakColumn3 = aBakColumn3;
    }

    public String getBakColumn4() {
        return this.bakColumn4;
    }

    public void setBakColumn4(final String aBakColumn4) {
        this.bakColumn4 = aBakColumn4;
    }

    public String getBakColumn5() {
        return this.bakColumn5;
    }

    public void setBakColumn5(final String aBakColumn5) {
        this.bakColumn5 = aBakColumn5;
    }

    public String getBakColumn6() {
        return this.bakColumn6;
    }

    public void setBakColumn6(final String aBakColumn6) {
        this.bakColumn6 = aBakColumn6;
    }

    public String getBakColumn7() {
        return this.bakColumn7;
    }

    public void setBakColumn7(final String aBakColumn7) {
        this.bakColumn7 = aBakColumn7;
    }

    public String getBakColumn8() {
        return this.bakColumn8;
    }

    public void setBakColumn8(final String aBakColumn8) {
        this.bakColumn8 = aBakColumn8;
    }

    public String getBakColumn9() {
        return this.bakColumn9;
    }

    public void setBakColumn9(final String aBakColumn9) {
        this.bakColumn9 = aBakColumn9;
    }

    public String getBakColumn10() {
        return this.bakColumn10;
    }

    public void setBakColumn10(final String aBakColumn10) {
        this.bakColumn10 = aBakColumn10;
    }

}
