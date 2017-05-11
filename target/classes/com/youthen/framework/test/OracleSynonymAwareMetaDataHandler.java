// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.test;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DefaultMetadataHandler;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class OracleSynonymAwareMetaDataHandler extends DefaultMetadataHandler {

    public static final Log LOG = LogFactory.getLog(OracleSynonymAwareMetaDataHandler.class);

    private static final String SQL_SELECT_USER_SYNONYM =
            "\n with filter1 as (  " +
                    "\n select connect_by_root synonym_name root_synonym_name,  " +
                    "\n        connect_by_root table_owner root_table_owner,  " +
                    "\n        level lvl,  " +
                    "\n        syn.*  " +
                    "\n from user_synonyms syn  " +
                    "\n connect by synonym_name = prior table_name and (table_owner != prior table_owner)  " +
                    "\n start with synonym_name = :p_synonym_name " +
                    "\n ), filter2 as (  " +
                    "\n select filter1.*,  " +
                    "\n        max(lvl) over (PARTITION BY root_synonym_name, root_table_owner) max_lvl  " +
                    "\n from filter1  " +
                    "\n )  " +
                    "\n select table_owner,  " +
                    "\n        table_name  " +
                    "\n from filter2  " +
                    "\n where lvl = max_lvl  ";
    private final Map<TableInfo, TableInfo> synonymMap;

    private final SimpleJdbcOperations simpleJdbcOperations;

    public OracleSynonymAwareMetaDataHandler(final SimpleJdbcOperations aSimpleJdbcOperations) {
        this.synonymMap = new HashMap<TableInfo, TableInfo>();
        this.simpleJdbcOperations = aSimpleJdbcOperations;
    }

    /**
     * @see org.dbunit.database.DefaultMetadataHandler#getColumns(java.sql.DatabaseMetaData, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ResultSet getColumns(final DatabaseMetaData aDatabaseMetaData,
                                final String aSchemaName,
                                final String aTableName)
            throws SQLException {

        final TableInfo tableInfo = getTableInfo(aSchemaName, aTableName);

        return super.getColumns(aDatabaseMetaData, tableInfo.getSchemaName(),
                                tableInfo.getTableName());
    }

    /**
     * @see org.dbunit.database.DefaultMetadataHandler#matches(java.sql.ResultSet, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @Override
    public boolean matches(final ResultSet aColumnsResultSet,
                           final String aCatalog,
                           final String aSchemaName,
                           final String aTableName,
                           final String aColumn,
                           final boolean aCaseSensitive)
            throws SQLException {

        final TableInfo tableInfo = getTableInfo(aSchemaName, aTableName);

        return super.matches(aColumnsResultSet, aCatalog, tableInfo.getSchemaName(),
                             tableInfo.getTableName(), aColumn, aCaseSensitive);
    }

    /**
     * @see org.dbunit.database.DefaultMetadataHandler#getPrimaryKeys(java.sql.DatabaseMetaData, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ResultSet getPrimaryKeys(final DatabaseMetaData aMetaData,
                                    final String aSchemaName,
                                    final String aTableName)
            throws SQLException {

        final TableInfo tableInfo = getTableInfo(aSchemaName, aTableName);

        return super.getPrimaryKeys(aMetaData, tableInfo.getSchemaName(),
                                    tableInfo.getTableName());
    }

    @SuppressWarnings("serial")
    private TableInfo getTableInfo(final String aSchemaName, final String aTableName) {

        LOG.debug("getTableInfo - schemaName: {" + aSchemaName + "}, tableName: {" + aTableName + "}");

        final TableInfo requestTableInfo = new TableInfo(aSchemaName, aTableName);

        if (this.synonymMap.containsKey(requestTableInfo)) {
            return this.synonymMap.get(requestTableInfo);
        }
        TableInfo actualTableInfo = DataAccessUtils.singleResult(
                    this.simpleJdbcOperations.getNamedParameterJdbcOperations().query(
                            SQL_SELECT_USER_SYNONYM,
                            new HashMap<String, Object>() {

                                {
                                    put("p_synonym_name", aTableName);

                                }
                            },
                            new ParameterizedRowMapper<TableInfo>() {

                                @Override
                                public TableInfo mapRow(final ResultSet aRs, final int aRowNum) throws SQLException {
                                    int ndx = 0;
                                    return new TableInfo(
                                            aRs.getString(++ndx)
                                            , aRs.getString(++ndx)
                                            );
                                        }
                            }
                            ));

        if (actualTableInfo == null) {
            actualTableInfo = requestTableInfo;
        }

        this.synonymMap.put(requestTableInfo, actualTableInfo);

        return actualTableInfo;

    }

    public static class TableInfo {

        private final String schemaName;
        private final String tableName;

        TableInfo(final String aSchemaName, final String aTableName) {
            this.schemaName = aSchemaName;
            this.tableName = aTableName;
        }

        public String getSchemaName() {
            return this.schemaName;
        }

        public String getTableName() {
            return this.tableName;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object aO) {
            if (this == aO) {
                return true;
            }
            if (!(aO instanceof TableInfo)) {
                return false;
            }

            final TableInfo that = (TableInfo) aO;

            return EqualsBuilder.reflectionEquals(this, that);
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
