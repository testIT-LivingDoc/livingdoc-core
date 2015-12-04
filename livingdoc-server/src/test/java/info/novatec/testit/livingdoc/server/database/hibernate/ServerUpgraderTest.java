package info.novatec.testit.livingdoc.server.database.hibernate;

import static org.junit.Assert.assertEquals;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class ServerUpgraderTest extends AbstractDBUnitHibernateMemoryTest {
    private SystemInfoDao systemDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        systemDao = new HibernateSystemInfoDao(this);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testNoUpgradesShouldBeTriggeredIfTheInstalledVersionMatchesTheCurrentVersion() throws Exception {
        setupSystemInfo("VERSION");
        new ServerUpgrader(this).upgradeTo("VERSION");

        assertEquals(new Integer(0), systemDao.getSystemInfo().getVersion());
        assertEquals("VERSION", systemDao.getSystemInfo().getServerVersion());
    }

    @Test
    public void testIfNoUpgradesNeededTheInstalledVersionIsAutomaticallyUpdatedToTheCurrentVersion() throws Exception {
        setupSystemInfo("SOME.VERSION");
        new ServerUpgrader(this).upgradeTo("VERSION.WITH.NO.UPGRADES");

        assertEquals("VERSION.WITH.NO.UPGRADES", systemDao.getSystemInfo().getServerVersion());
    }

    @Test
    public void testTheUpgradesAreCalledBasedOnTheNewlyUpgradedVersion() throws Exception {
        setupSystemInfo("VERSION.THAT.NEEDS.UPGRADES");

        Transaction startedTransaction = session.beginTransaction();
        new ServerUpgrader(this).upgradeTo("VERSION.UPGRADED");
        startedTransaction.commit();

        SystemInfo systemInfo = systemDao.getSystemInfo();
        assertEquals(new Integer(2), systemInfo.getVersion());
        assertEquals("VERSION.UPGRADED", systemInfo.getServerVersion());
    }

    @Test
    public void testAnUpgradedThatReturnsNoUpgradedVersionToIsConsideredAsANoUpgradeVersion() throws Exception {
        setupSystemInfo("VERSION.THAT.DOESNT.RETURN.AN.UPGRADED.TO.VERSION");
        Transaction startedTransaction = session.beginTransaction();
        new ServerUpgrader(this).upgradeTo("VERSION.UPGRADED");
        startedTransaction.commit();

        SystemInfo systemInfo = systemDao.getSystemInfo();
        assertEquals(new Integer(1), systemInfo.getVersion());
        assertEquals("VERSION.UPGRADED", systemInfo.getServerVersion());
    }

    private void setupSystemInfo(String serverVersion) {
        SystemInfo sysForServerOfCurrentVersion = new SystemInfo();
        // sysForServerOfCurrentVersion.setId(1L);
        sysForServerOfCurrentVersion.setVersion(0);
        sysForServerOfCurrentVersion.setServerVersion(serverVersion);
        systemDao.store(sysForServerOfCurrentVersion);
    }
}
