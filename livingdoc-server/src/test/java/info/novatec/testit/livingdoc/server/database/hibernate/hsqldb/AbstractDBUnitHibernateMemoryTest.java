package info.novatec.testit.livingdoc.server.database.hibernate.hsqldb;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

import junit.framework.AssertionFailedError;


public abstract class AbstractDBUnitHibernateMemoryTest extends AbstractHibernateMemoryTest {

    protected void insertIntoDatabase(String flatXmlDataSetFileName) throws Exception {
        insertIntoDatabase(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(flatXmlDataSetFileName)));
    }

    /**
     * This operation inserts the dataset contents into the database. This
     * operation assumes that table data does not exist in the target database
     * and fails if this is not the case. To prevent problems with foreign keys,
     * tables must be sequenced appropriately in the dataset.
     * 
     * @param dataSet
     * @throws Exception
     */
    protected void insertIntoDatabase(IDataSet dataSet) throws Exception {
        IDatabaseConnection connection = null;

        try {
            connection = getConnection();
            DatabaseOperation.INSERT.execute(connection, dataSet);
        } finally {
            closeConnection(connection);
        }
    }

    protected void deleteFromDatabase(String flatXMLDataSetFileName) throws Exception {
        deleteFromDatabase(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(flatXMLDataSetFileName)));
    }

    /**
     * Deletes all rows of tables present in the specified dataset. If the
     * dataset does not contains a particular table, but that table exists in
     * the database, the database table is not affected. Table are truncated in
     * reverse sequence.
     * 
     * @param dataSet
     * @throws Exception
     */
    protected void deleteFromDatabase(IDataSet dataSet) throws Exception {
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
            DatabaseOperation.DELETE.execute(connection, dataSet);
        } finally {
            closeConnection(connection);
        }
    }

    protected void deleteAllFromDatabase(Class< ? > persistentClass) throws Exception {
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
            IDataSet actualTable = getConnection().createDataSet(new String[] { getTableName(persistentClass) });
            deleteFromDatabase(actualTable);
        } finally {
            closeConnection(connection);
        }
    }

    protected void cleanInsertIntoDatabase(String flatXMLDataSetFileName) throws Exception {
        cleanInsertIntoDatabase(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(flatXMLDataSetFileName)));
    }

    /**
     * This composite operation performs a deleteAllFromDatabase operation
     * followed by an insertIntoDatabase operation. This is the safest approach
     * to ensure that the database is in a known state. This is appropriate for
     * tests that require the database to only contain a specific set of data.
     * 
     * @param dataSet
     * @throws Exception
     */
    protected void cleanInsertIntoDatabase(IDataSet dataSet) throws Exception {
        IDatabaseConnection connection = null;

        try {
            connection = getConnection();
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            closeConnection(connection);
        }
    }

    protected void assertTableEquals(Class< ? > peristentClass, String[] includedFields, String flatXmlDataSetFileName)
        throws Exception {
        assertTableEquals(peristentClass, includedFields, getFlatXmlDataSet(flatXmlDataSetFileName));
    }

    protected void assertTableEquals(Class< ? > peristentClass, String[] includedFields, IDataSet dataset) throws Exception {
        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(getTableName(peristentClass));
        ITable actualData = DefaultColumnFilter.includedColumnsTable(actualTable, getColumnNames(peristentClass,
            includedFields));

        // Load expected data from an XML dataset
        ITable expectedTable = dataset.getTable(getTableName(peristentClass));
        ITable expectedData = DefaultColumnFilter.includedColumnsTable(expectedTable, getColumnNames(peristentClass,
            includedFields));

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedData, actualData);
    }

    protected void assertTableEmpty(Class< ? > peristentClass) throws Exception {
        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(getTableName(peristentClass));

        assertTrue(actualTable.getRowCount() == 0);
    }

    protected String getTableName(Class< ? > peristentClass) {
        Table table = getMapping(peristentClass).getTable();
        return table.getName();
    }

    private PersistentClass getMapping(Class< ? > peristentClass) {
        PersistentClass mapping = getHibernateConfiguration().getClassMapping(peristentClass.getName());
        if (mapping == null) {
            throw new AssertionFailedError("Unable to resolve table name for class: " + peristentClass.getName());
        }
        return mapping;
    }

    @SuppressWarnings("unchecked")
    protected String[] getColumnNames(Class< ? > peristentClass, String[] includedFields) throws MappingException {
        Collection<String> columns = new ArrayList<String>();
        for (int i = 0; i < includedFields.length; i ++ ) {
            String propertyName = includedFields[i];
            Property property = getMapping(peristentClass).getProperty(propertyName);

            for (Iterator<Column> it = property.getColumnIterator(); it.hasNext();) {
                Column col = it.next();
                columns.add(col.getName());
            }
        }
        return columns.toArray(new String[columns.size()]);
    }

    protected IDataSet getFlatXmlDataSet(String resourceName) throws Exception {
        return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(resourceName));
    }

    protected IDatabaseConnection getConnection() throws Exception {
        Configuration cfg = getHibernateConfiguration();
        String username = cfg.getProperty("hibernate.connection.username");
        String password = cfg.getProperty("hibernate.connection.password");
        String driver = cfg.getProperty("hibernate.connection.driver_class");
        String url = cfg.getProperty("hibernate.connection.url");

        Class.forName(driver);

        Connection jdbcConnection = DriverManager.getConnection(url, username, password);

        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        DatabaseConfig config = connection.getConfig();
        // config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new
        // H2DataTypeFactory());
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

        return connection;
    }

    private void closeConnection(IDatabaseConnection connection) throws SQLException {
        if (null != connection) {
            connection.close();
        }
    }
}
