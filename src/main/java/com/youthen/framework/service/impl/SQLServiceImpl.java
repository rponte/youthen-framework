// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.persistence.dao.SQLDao;
import com.youthen.framework.service.SQLService;

/**
 * 基础数据service类。
 * 
 * @author LiXin
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
@Service("sqlService")
public class SQLServiceImpl implements SQLService {

    @Resource
    public SQLDao sqlDao;

    /**
     * @see com.youthen.sql.service.SQLService#insert()
     */

    @Override
    public boolean insert(final String tableName, final List<String[]> colName_value) {
        return this.sqlDao.insert(tableName, colName_value);
    }

    /**
     * @see com.youthen.sql.service.SQLService#getSQL(java.lang.String, java.lang.String[])
     */

    @Override
    public List<Map<String, String>> selectById(final String aTableName, final String... aIds) {
        return this.sqlDao.selectById(aTableName, aIds);
    }

    /**
     * @see com.youthen.sql.service.SQLService#createSQL(java.lang.String, java.lang.String[])
     */

    @Override
    public boolean createTable(final String aTableName, final String... aColumnNames) {
        return this.sqlDao.createTable(aTableName, aColumnNames);
    }

    /**
     * @see com.youthen.sql.service.SQLService#update(java.lang.String, java.lang.String[][],
     *      java.lang.String[][])
     */

    @Override
    public boolean update(final String aTableName, final List<String[]> aColName_value,
            final List<String[]> aColWhere_value) {
        return this.sqlDao.update(aTableName, aColName_value, aColWhere_value);
    }

    /**
     * @see com.youthen.framework.service.SQLService#selectFromTable(java.lang.String, java.util.List)
     */

    @Override
    public List<Map<String, String>> selectFromTable(final String aTableName, final List<String[]> aColName_value) {
        return this.sqlDao.selectFromTable(aTableName, aColName_value);
    }

    /**
     * @see com.youthen.framework.service.SQLService#queryBySql(java.lang.String)
     */

    @Override
    public List<Map<String, Object>> queryBySql(final String aSql) {
        return this.sqlDao.queryBySql(aSql);
    }

    @Override
    public int excuteBySql(final String sql) {
        return this.sqlDao.excuteBySql(sql);
    }

    /**
     * @see com.youthen.framework.service.SQLService#getList(java.lang.String, java.lang.String,
     *      java.lang.String)
     */

    @Override
    public List<Map<String, Object>> getList(final String aTableName, final String aCondition, final String aOrderBy) {

        String sql = " select * from " + aTableName + " where 1=1 ";
        if (StringUtils.isNotEmpty(aCondition)) {
            sql += " AND " + aCondition;
        }

        if (StringUtils.isNotEmpty(aOrderBy)) {
            sql += " order by " + aOrderBy;
        }

        return this.queryBySql(sql);
    }
}
