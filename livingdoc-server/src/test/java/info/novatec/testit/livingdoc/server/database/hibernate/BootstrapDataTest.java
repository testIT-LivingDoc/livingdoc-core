package info.novatec.testit.livingdoc.server.database.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.configuration.ServerConfiguration;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class BootstrapDataTest extends AbstractDBUnitHibernateMemoryTest {
    private URL configURL = BootstrapDataTest.class.getResource("configuration-test.xml");
    private BootstrapData boot;
    private SystemInfoDao systemDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        ServerConfiguration config = ServerConfiguration.load(configURL);
        config.getProperties().setProperty("baseUrl", "no directories");

        boot = new BootstrapData(this, config.getProperties());
        systemDao = new HibernateSystemInfoDao(this);
    }

    @Override
    @After
    public void tearDown() {
        // overrides tearDown from extended class
    }

    @Test
    public void testWhileUpgradingIfAnErrorOccuresTheBootstrapProcessWillAbortAndProvoqueARollBack() throws Exception {
        try {
            setupSystemInfo("VERSION.THAT.WILL.CAUSE.A.FAILURE");
            boot.execute();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals("Boostrap Failure", e.getMessage());
        }

        assertNull(systemDao.getSystemInfo());
    }

    @Test
    public void testWhileRegistratingTheRunnersIfAnErrorOccuresTheBootstrapProcessWillContinue() throws Exception {
        boot.execute();
        assertNotNull(systemDao.getSystemInfo());
    }

    private void setupSystemInfo(String serverVersion) {
        SystemInfo sysForServerOfCurrentVersion = new SystemInfo();
        sysForServerOfCurrentVersion.setVersion(0);
        sysForServerOfCurrentVersion.setServerVersion(serverVersion);
        systemDao.store(sysForServerOfCurrentVersion);
    }
}
