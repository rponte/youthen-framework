// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.service;

import java.util.List;
import java.util.Map;

/**
 * 基础数据service类。
 * 
 * @author LiXin
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
public interface SQLService {

    public boolean insert(final String tableName, List<String[]> colName_value);

    public boolean update(final String tableName, List<String[]> colName_value, List<String[]> colWhere_value);

    public List<Map<String, String>> selectById(final String tableName, final String... ids);

    public List<Map<String, String>> selectFromTable(final String tableName, final List<String[]> colName_value);

    public boolean createTable(final String tableName, final String... columnNames);

    public List<Map<String, Object>> queryBySql(final String sql);

    public int excuteBySql(final String sql);

    public List<Map<String, Object>> getList(String tableName, String condition, String orderBy);

}
