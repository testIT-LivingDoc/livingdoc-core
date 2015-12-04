package info.novatec.testit.livingdoc.server.database.hibernate.hsqldb;

import org.junit.After;
import org.junit.Before;


public abstract class AbstractHibernateMemoryTest extends HibernateMemoryTestCase {
    private String dataFile;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        startSession();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        rollbackIfNecessary();
        closeSession();
        super.tearDown();
    }

    private void rollbackIfNecessary() {
        if (transaction == null) {
            return;
        }
        if ( ! transaction.wasCommitted()) {
            transaction.rollback();
        }
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }
}
