package com.youthen.framework.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dbunit.Assertion;
import org.dbunit.assertion.DefaultFailureHandler;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.assertion.FailureHandler;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import com.youthen.framework.common.exception.ApplicationException;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class BaseTestWithDB extends BaseTest {

    private static IDatabaseConnection sConnection;

    private static File sBackupFile;

    @Autowired
    private DataSource dataSource;

    public BaseTestWithDB() {
        super();
    }

    public BaseTestWithDB(final String aKaishaCd) {
        super(aKaishaCd);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        try {
            restore();
        } finally {
            closeConnection();
        }
    }

    @Override
    protected void setUpOnce() throws Exception {
        super.setUpOnce();
        sBackupFile = null;
        this.openConnection();
        this.backup();
    }

    private void openConnection() throws Exception {
        final Connection conn = this.dataSource.getConnection();
        sConnection = this.prepareConnection(conn);

        sConnection.getConnection().setAutoCommit(true);
    }

    private static void closeConnection() throws Exception {
        if (sConnection != null) {
            sConnection.close();
        }
    }

    private IDatabaseConnection prepareConnection(final Connection aConnection) throws Exception {
        IDatabaseConnection connection = null;

        final String schema = aConnection.getMetaData().getUserName();
        final String dbmsProductName = aConnection.getMetaData().getDatabaseProductName();
        if ("Oracle".equals(dbmsProductName)) {
            final Connection oracleConn = ((DelegatingConnection) aConnection).getInnermostDelegate();
            if (oracleConn == null) {
                throw new ApplicationException(new FieldSupportedMessage("XFW90005"));
            }

            connection = new DatabaseConnection(oracleConn, schema);
            final DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Oracle10DataTypeFactory());

            config.setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, new String[] {"TABLE", "SYNONYM"});
            config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new OracleSynonymAwareMetaDataHandler(
                    new SimpleJdbcTemplate(this.dataSource))
                    );

        } else if ("MySQL".equals(dbmsProductName)) {
            connection = new DatabaseConnection(aConnection);
            final DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
        } else if ("Microsoft SQL Server".equals(dbmsProductName)) {
            connection = new DatabaseConnection(aConnection);
            final DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
        } else {
            throw new ApplicationException(new FieldSupportedMessage("XFW90001"));
        }

        return connection;
    }

    protected boolean isBackup() {
        return false;
    }

    protected String[] getBackupTargetTables() {
        return new String[] {};
    }

    private void backup() throws Exception {
        if (this.isBackup()) {
            IDataSet dataSet;
            if (this.getBackupTargetTables() == null || this.getBackupTargetTables().length == 0) {

                dataSet = sConnection.createDataSet();
            } else {
                dataSet = sConnection.createDataSet(this.getBackupTargetTables());
            }
            sBackupFile = File.createTempFile("backup", ".xml");
            FlatXmlDataSet.write(dataSet, new FileOutputStream(sBackupFile));
        } else {
            sBackupFile = null;
        }
    }

    private static void restore() throws Exception {
        if (sBackupFile != null && sBackupFile.exists()) {
            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            final IDataSet dataSet = builder.build(sBackupFile);
            final FilteredDataSet filteredDataSet = sequenceTables(dataSet);
            if (filteredDataSet == null) {
                return;
            }

            DatabaseOperation.CLEAN_INSERT.execute(sConnection, sequenceTables(dataSet));
            sBackupFile.deleteOnExit();
        }
    }

    private static FilteredDataSet sequenceTables(final IDataSet aDataSet) throws Exception {
        if (sConnection == null || sConnection.getConnection() == null || sConnection.getConnection().isClosed()) {
            return null;
        }
        return new FilteredDataSet(new DatabaseSequenceFilter(sConnection, aDataSet.getTableNames()), aDataSet);
    }

    protected final void loadTestData(final String[] aFileNames) throws Exception {
        final IDataSet dataSet = this.loadData(aFileNames);
        DatabaseOperation.CLEAN_INSERT.execute(sConnection, sequenceTables(dataSet));
    }

    protected void loadTestData(final Location aLocation, final String[] aFileNames) throws Exception {
        final IDataSet dataSet = this.loadData(aLocation, aFileNames);
        DatabaseOperation.CLEAN_INSERT.execute(sConnection, sequenceTables(dataSet));
    }

    private IDataSet loadData(final Location aLocation, final String[] aFileNames) throws Exception {
        if (aFileNames == null || aFileNames.length == 0) {
            throw new ApplicationException(new FieldSupportedMessage("XFW90002"));
        }
        final List<IDataSet> dataSetList = new ArrayList<IDataSet>();
        for (final String fileName : aFileNames) {
            dataSetList.add(new XlsDataSet(aLocation.getResourceAsStream(fileName, this.getClass())));
        }
        final CompositeDataSet compositeDataSet =
                new CompositeDataSet(dataSetList.toArray(new IDataSet[dataSetList.size()]));

        final IDataSet replaceDataSet = this.replaceData(compositeDataSet);
        if (replaceDataSet == null) {
            throw new ApplicationException(new FieldSupportedMessage("XFW90004"));
        }

        return replaceDataSet;
    }

    private IDataSet loadData(final String[] aFileNames) throws Exception {

        return loadData(Location.LOCAL, aFileNames);
    }

    protected final InputStream getResourceAsStreamFromCurrentPackage(final String aFileName) {
        final InputStream result = this.getClass().getResourceAsStream(aFileName);
        if (result == null) {
            throw new ApplicationException(new FieldSupportedMessage("XFW90003").format(aFileName));
        }
        return result;
    }

    public enum Location {
        LOCAL("");

        private String path;

        Location(final String aPath) {
            this.path = aPath;
        }

        public InputStream getResourceAsStream(final String aFileName, final Class<?> aTargetClass) {
            InputStream result = null;
            if (this.equals(LOCAL)) {
                result = aTargetClass.getResourceAsStream(aFileName);
            } else {
                result = aTargetClass.getClassLoader().getResourceAsStream(this.path + aFileName);
            }

            if (result != null) {
                return result;
            }
            final String currentDir = new File(".").getAbsoluteFile().getParent();
            try {
                result = new FileInputStream(currentDir + File.separator + this.path + aFileName);
            } catch (final FileNotFoundException e) {
                throw new ApplicationException(new FieldSupportedMessage("XFW90003").format(currentDir + File.separator
                        + this.path + aFileName));
            }
            return result;
        }
    }

    protected final byte[] getByteArray(final String aFileName) throws Exception {
        InputStream in = null;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            in = this.getResourceAsStreamFromCurrentPackage(aFileName);
            int b;
            while ((b = in.read()) != -1) {
                byteArrayOutputStream.write(b);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(byteArrayOutputStream);
        }
    }

    protected final String getString(final String aFileName) throws Exception {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = this.getResourceAsStreamFromCurrentPackage(aFileName);
            final StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));
            while (reader.ready()) {
                if (builder.length() != 0) {
                    builder.append(System.getProperty("line.separator"));
                }
                builder.append(reader.readLine());
            }
            return builder.toString();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(reader);
        }
    }

    protected ReplacementDataSet replaceData(final IDataSet aDataSet) throws Exception {
        final ReplacementDataSet replaceDataSet = new ReplacementDataSet(aDataSet);
        replaceDataSet.addReplacementObject("[NULL]", null);
        return replaceDataSet;
    }

    protected final void assertEqualsData(final String aTargetTable, final String[] aExpectedDataFileNames,
            final String[] aIgnoreColumns, final String[] aSortColumnNames)
            throws Exception {
        final IDatabaseConnection databaseConnection = databaseConnection();

        ITable actualTable;
        ITable expectedTable;
        if (aSortColumnNames != null && aSortColumnNames.length > 0) {
            actualTable = new SortedTable(databaseConnection.createTable(aTargetTable), aSortColumnNames);
            expectedTable =
                    new SortedTable(this.loadData(aExpectedDataFileNames).getTable(aTargetTable), aSortColumnNames);
        } else {
            actualTable = new SortedTable(databaseConnection.createTable(aTargetTable));
            expectedTable =
                    new SortedTable(this.loadData(Location.LOCAL, aExpectedDataFileNames).getTable(aTargetTable));
        }

        if (aIgnoreColumns == null || aIgnoreColumns.length == 0) {
            Assertion.assertEquals(expectedTable, actualTable);
        } else {
            Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, aIgnoreColumns);
        }
    }

    protected final void assertEqualsData(final String... aExpectedDataFileNames) throws Exception {
        assertEqualsData(aExpectedDataFileNames, new NoOption());
    }

    protected final void assertEqualsData(final String[] aExpectedDataFileNames,
            final Map<String, String[]> aExpectedColumns)
            throws Exception {
        assertEqualsData(aExpectedDataFileNames, new IncludeColumnOption(aExpectedColumns));
    }

    protected final void assertEqualsData(final String[] aExpectedDataFileNames,
            final TargetColumnOption aOption)
            throws Exception {
        final IDatabaseConnection databaseConnection = databaseConnection();
        final DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();
        final IDataSet expected = this.loadData(Location.LOCAL, aExpectedDataFileNames);
        try {
            for (final String tablename : expected.getTableNames()) {
                final ITable actualTable =
                        aOption.addOption(new SortedTable(databaseConnection.createTable(tablename)));
                final ITable expectedTable = aOption.addOption(new SortedTable(expected.getTable(tablename)));
                Assertion.assertEquals(expectedTable, actualTable, myHandler);
            }
        } catch (final AssertionError ex) {
            throw ex;
        } catch (final Exception ex) {

            final StringBuilder message = new StringBuilder();
            if (!myHandler.getDiffList().isEmpty()) {
                message.append(collectError(myHandler));
            }
            throw new AssertionError(message.append(throwableToString(ex)).toString());
        }
        if (!myHandler.getDiffList().isEmpty()) {
            throw new AssertionError(collectError(myHandler));
        }
    }

    /**
     * @param aT
     */
    private String throwableToString(final Throwable aT) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        aT.printStackTrace(pw);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    private String collectError(final DiffCollectingFailureHandler aMyHandler) {
        final StringBuilder errorMessage = new StringBuilder();
        final FailureHandler messageCollector = new DefaultFailureHandler() {

            @Override
            public void handle(final Difference aDiff) {
                final String message = buildMessage(aDiff);
                final Error originErr = this.createFailure(message,
                        String.valueOf(aDiff.getExpectedValue()), String.valueOf(aDiff.getActualValue()));
                errorMessage.append(originErr.getMessage() + "\n");
            }

        };
        for (final Difference diff : (List<Difference>) aMyHandler.getDiffList()) {
            messageCollector.handle(diff);
        }
        return errorMessage.toString();
    }

    protected final String[] backupTablesFromExpectedFile(final String... aExpectedDataFileNames) {
        IDataSet expected;
        try {
            expected = this.loadData(Location.LOCAL, aExpectedDataFileNames);
            return expected.getTableNames();
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }

    private IDatabaseConnection databaseConnection() throws Exception {
        final Connection conn = DataSourceUtils.getConnection(this.dataSource);
        final IDatabaseConnection databaseConnection = this.prepareConnection(conn);
        return databaseConnection;
    }

    protected final void defaultData(final String aTargetTable, final String[] aExpectedDataFileNames,
            final String[] aIgnoreColumns)
            throws Exception {
        this.assertEqualsData(aTargetTable, aExpectedDataFileNames, aIgnoreColumns, null);
    }

    /**
     * getter for dataSource.
     * 
     * @return dataSource
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected void export(final String[] aTargetTables, final String aExportFileName) throws Exception {
        final IDatabaseConnection databaseConnection = databaseConnection();
        final IDataSet dataset = databaseConnection.createDataSet(aTargetTables);
        XlsDataSet.write(dataset, new FileOutputStream(aExportFileName));
    }

    interface TargetColumnOption {

        ITable addOption(ITable aSource) throws DataSetException;
    }

    class IncludeColumnOption implements TargetColumnOption {

        private final Map<String, String[]> targetColumn;

        public IncludeColumnOption(final Map<String, String[]> aTargetColumn) {
            this.targetColumn = aTargetColumn;
        }

        @Override
        public ITable addOption(final ITable aSource) throws DataSetException {
            final String[] columns = this.targetColumn.get(aSource.getTableMetaData().getTableName());
            return DefaultColumnFilter.includedColumnsTable(aSource, columns);
        }

    }
    class NoOption implements TargetColumnOption {

        @Override
        public ITable addOption(final ITable aSource) throws DataSetException {
            return aSource;
        }

    }
}
