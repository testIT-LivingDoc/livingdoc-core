package info.novatec.testit.livingdoc.server.database.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.repository.AtlassianRepository;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.server.LivingDocServer;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateRepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class InitialDatasTest extends AbstractDBUnitHibernateMemoryTest {
    private SystemInfoDao systDao;
    private RepositoryDao repoDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        systDao = new HibernateSystemInfoDao(this);
        repoDao = new HibernateRepositoryDao(this);
    }

    @Test
    public void testTheSystemInfoIsInitializedIfNotEntry() throws Exception {
        new InitialDatas(this).insert();
        SystemInfo systemInfo = systDao.getSystemInfo();
        assertEquals(LivingDocServer.VERSION, systemInfo.getServerVersion());
    }

    @Test
    public void testTheSystemInfoInsertIsNotTriggeredIfWeAlreadyHaveSomeEntriesButTheVersionIsSetToDefault()
        throws Exception {
        insertIntoDatabase("/dbunit/datas/InitializedDataBase-latest.xml");
        new InitialDatas(this).insert();

        SystemInfo systemInfo = systDao.getSystemInfo();
        assertEquals(InitialDatas.DEFAULT_VERSION, systemInfo.getServerVersion());
    }

    @Test
    public void testAllSupportedRepositoryTypesAreRegisteredAtInitialization() throws Exception {
        new InitialDatas(this).insert();
        RepositoryType type = repoDao.getTypeByName("FILE");
        RepositoryType typeJava = repoDao.getTypeByName("CONFLUENCE");

        assertNotNull(type);
        assertEquals(type.getClassName(), FileSystemRepository.class.getName());
        assertEquals("%s%s", type.getDocumentUrlFormat());
        assertEquals("%s%s", type.getTestUrlFormat());

        assertNotNull(typeJava);
        assertEquals(typeJava.getClassName(), AtlassianRepository.class.getName());
        assertEquals("%s/%s", typeJava.getDocumentUrlFormat());
        assertEquals(null, typeJava.getTestUrlFormat());

    }

    @Test
    public void testAllSupportedRepositoryTypesInsertIsNotTriggeredIfWeAlreadyHaveSomeEntries() throws Exception {
        new InitialDatas(this).insert();

        List<RepositoryType> types = repoDao.getAllTypes();
        for (RepositoryType type : types) {
            assertEquals(new Integer(0), type.getVersion());
        }
    }

}
