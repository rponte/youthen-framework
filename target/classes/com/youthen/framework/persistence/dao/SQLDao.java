// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;
import com.youthen.framework.common.DateFormatUtils;
import com.youthen.framework.util.CommonEntityUtils;

/**
 * 。
 * 
 * @author LiXin
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
@Repository("sqlDao")
public class SQLDao {

    @Resource
    private SessionFactory sessionFactory;

    /**
     * UPDATE一条基础数据。
     * 
     * @param tableName
     * @param colName_value
     * @param colWhere_value
     * @return 成功返回true
     */
    public boolean update(final String tableName, final List<String[]> colName_value,
            final List<String[]> colWhere_value) {
        if (colName_value == null || colName_value.size() == 0) {
            return false;
        }

        String sql = "UPDATE  " + tableName + "  SET ";
        sql +=
                " VERSION_NO=VERSION_NO+1 , UPDATER_ID ='" + CommonEntityUtils.getUpdId() + "' , UPDATE_TIME='"
                        + CommonEntityUtils.getSystime() + "'";

        if (colName_value.size() > 0) {
            sql += ",";
        }

        for (final String[] key_value : colName_value) {
            sql += key_value[0] + " = '" + key_value[1] + "',";
        }
        if (colName_value.size() > 0) {
            sql = sql.substring(0, sql.length() - 1);
        }

        if (colWhere_value != null) {

            sql += " WHERE 1=1 ";

            for (final String[] where_value : colWhere_value) {
                sql += " AND " + where_value[0] + "='" + where_value[1] + "' ";
            }
        }

        final int result = excuteBySql(sql);
        if (result >= 0) {
            return true;
        }
        return false;

    }

    /**
     * 插入一条基础数据。
     * 
     * @param tableName
     * @param colName_value
     * @return 成功返回true
     */
    public boolean insert(final String tableName, final List<String[]> colName_value) {
        if (colName_value == null || colName_value.size() == 0) {
            return false;
        }

        String sql = "INSERT INTO " + tableName + "";
        String col = "ID,";
        String val = tableName + "_SEQ.NEXTVAL,";

        for (final String[] key_value : colName_value) {
            if (key_value[0].endsWith("_NUMBER")) {
                final String colName = key_value[0].substring(0, key_value[0].lastIndexOf('_'));
                col += colName + ",";
            } else {
                col += key_value[0] + ",";
            }
            val += "?,";
        }

        col += " VERSION_NO, UPDATER_ID, UPDATE_TIME";
        val += "0,'" + CommonEntityUtils.getUpdId() + "','" + CommonEntityUtils.getSystime() + "'";

        sql += " (" + col + ") VALUES (" + val + ")";

        // final Transaction tx = this.getSession().beginTransaction();

        final SQLQuery query = this.getSession().createSQLQuery(sql);

        for (int i = 0; i < colName_value.size(); i++) {

            final String[] key_value = colName_value.get(i);

            if (key_value[0].endsWith("_NUMBER")) {
                query.setBigDecimal(i, new BigDecimal(key_value[1]));
            } else if (key_value[0].endsWith("_DATE")) {
                final Date date = DateFormatUtils.parse("yyyy-MM-dd", key_value[1]);
                query.setDate(i, date);
            } else if (key_value[0].endsWith("_DATETIME")) {

                final Date date = DateFormatUtils.parse("yyyy-MM-dd hh:mm:ss", key_value[1]);
                query.setDate(i, date);
            } else {

                query.setParameter(i, key_value[1]);
            }
        }

        final int result = query.executeUpdate();

        // tx.commit();

        if (result >= 0) {
            return true;
        }
        return false;

    }

    /**
     * 根据ID查询基础数据。
     * 
     * @param tableName 表名
     * @param ids 列名
     * @return List
     */
    public List<Map<String, String>> selectById(final String tableName, final String... ids) {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=1 ";
        if (ids != null && ids.length > 0) {
            sql += " AND ID IN ( :valueList )";
        }

        final Query query = getSession().createSQLQuery(sql);

        if (ids != null && ids.length > 0) {
            query.setParameterList("valueList", ids);
        }

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        final List<Map<String, String>> result = query.list();
        return result;
    }

    public int count(final String cntSql) {
        final Query query = getSession().createSQLQuery(cntSql);
        return count(query);
    }

    public int count(final Query query) {
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        final List<Map<String, Integer>> result = query.list();
        if (result != null) {
            if (result.get(0) == null) return 0;
            final Object cnt = result.get(0).get("CNT");
            if (cnt != null) {
                return Integer.parseInt(cnt.toString());
            }
        }
        return 0;
    }

    /**
     * 根据条件查询数据。
     * 
     * @param tableName 表名
     * @param colName_value 列名_值
     * @return List
     */
    public List<Map<String, String>> selectFromTable(final String tableName, final List<String[]> colName_value) {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=1 ";

        if (colName_value != null && !colName_value.isEmpty()) {
            for (final String[] key_value : colName_value) {
                sql += " AND " + key_value[0] + " = ? ";
            }
        }

        final Query query = getSession().createSQLQuery(sql);

        if (colName_value != null && !colName_value.isEmpty()) {

            for (int i = 0; i < colName_value.size(); i++) {
                final String[] key_value = colName_value.get(i);
                query.setParameter(i, key_value[1]);
            }
        }

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        final List<Map<String, String>> result = query.list();
        return result;
    }

    /**
     * SQL查询。
     * 
     * @param sql
     * @return 成功返回true
     */
    public List<Map<String, Object>> queryBySql(final String sql) {
        final Query query = getSession().createSQLQuery(sql);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        final List<Map<String, Object>> list = query.list();
        return list;
    }

    /**
     * SQL查询。
     * 
     * @param sql
     * @return 成功返回true
     */
    public List<Map<String, Object>> queryBySqlAndParam(final String sql, final Object[] param) {
        final Query query = getSession().createSQLQuery(sql);
        if (param != null) {
            for (int i = 0; i < param.length; i++) {
                query.setParameter(i, param[i]);
            }
        }
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        final List<Map<String, Object>> list = query.list();
        return list;
    }

    /**
     * 创建一个基础数据表。
     * 
     * @param tableName 表名
     * @param columnNames 列名
     * @return 成功返回true
     */
    public boolean createTable(final String tableName, final String... columnNames) {

        String sql = " CREATE TABLE \"" + tableName + "\"( ID NUMBER(*,0) NOT NULL ENABLE, T_COMPANY NUMBER(*,0), ";

        for (final String colName : columnNames) {
            sql += "\"" + colName + "\" VARCHAR2(500 BYTE),";
        }

        sql += "\"VERSION_NO\" NUMBER DEFAULT 0,";
        sql += "\"UPDATER_ID\" VARCHAR2(20 BYTE),";
        sql += "\"UPDATE_TIME\" VARCHAR2(20 BYTE),";

        sql += "CONSTRAINT \"" + tableName + "_PK\" PRIMARY KEY (\"ID\"))";

        final int result = excuteBySql(sql);
        if (result >= 0) {
            createSequence(tableName);
            return true;
        }
        return false;
    }

    /**
     * 创建基础数据表的SEQUENCE。
     * 
     * @param tableName 表名
     * @return 创建成功返回true
     */
    public boolean createSequence(final String tableName) {
        final String sql = "CREATE SEQUENCE " + tableName + "_SEQ START WITH 1 increment by 1 maxvalue 999999999";
        final int result = excuteBySql(sql);
        if (result >= 0) {
            return true;
        }
        return false;
    }

    public String[] getColName(final String tableName) {

        final String sql = "select * from " + tableName;
        final List<Map<String, Object>> list = queryBySql(sql);
        return (String[]) list.get(0).keySet().toArray();
    }

    /**
     * UPDATE,DELEATE,CREATE等SQL执行
     * 
     * @param sql
     * @return 成功返回true
     */
    public int excuteBySql(final String sql) {
        int result;
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        result = query.executeUpdate();
        return result;
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

}
