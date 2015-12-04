package info.novatec.testit.livingdoc.server.database.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.configuration.ServerConfiguration;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemUnderTestDao;
import info.novatec.testit.livingdoc.util.URIUtil;


public class DefaultRunnersTest extends AbstractDBUnitHibernateMemoryTest {
    private static final String DATAS = "/dbunit/datas/InitializedDataBase-latest.xml";
    private URL configURL = DefaultRunnersTest.class.getResource("configuration-test.xml");
    private Properties properties;

    private String basePath;
    private SystemUnderTestDao sutDao;
    private File runnerDir;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        basePath = URIUtil.decoded(new File(configURL.getFile()).getParent());
        runnerDir = new File(basePath, "WEB-INF/lib");
        ServerConfiguration config = ServerConfiguration.load(configURL);
        config.getProperties().setProperty("baseUrl", basePath);
        properties = config.getProperties();

        sutDao = new HibernateSystemUnderTestDao(this);
    }

    @Test
    public void testTheJavaCurrentVersionRunnerIsProperlyRegisteredAndAllLibsAreCopiedIntoTheRunnersDirectory()
        throws Exception {
        List<String> expectedCp = new ArrayList<String>();
        expectedCp.add(URIUtil.decoded(new File(runnerDir, "livingdoc-confluence-plugin-dummy-complete.jar")
            .getAbsolutePath()).toUpperCase());

        new InitialDatas(this).insert();
        new DefaultRunners(this, properties).insertJavaRunner();

        Runner runner = sutDao.getRunnerByName("LDCore JAVA v. dummy");

        assertNotNull(runner);
        assertNull(runner.getServerName());
        assertNull(runner.getServerPort());
        List<String> runnerClasspaths = toUpperCaseList(runner.getClasspaths());
        assertTrue(runnerClasspaths.containsAll(expectedCp));
        assertTrue("expected:" + expectedCp.toString() + "but got :" + runnerClasspaths.toString(), expectedCp.containsAll(
            runnerClasspaths));
        assertTrue(expectedCp.containsAll(asPathList( ( runnerDir.listFiles() ))));
    }

    @Test
    public void testTheJavaCurrentVersionRunnerInsertIsNotTriggeredIfWeAlreadyHaveARunnerWithSameVersion() throws Exception {
        insertIntoDatabase(DATAS);
        new DefaultRunners(this, properties).insertJavaRunner();

        Runner runner = sutDao.getRunnerByName("LDCore JAVA v. dummy");

        assertNotNull(runner);
        assertEquals("1", runner.getServerName());
        assertEquals("1", runner.getServerPort());
    }

    @Test
    public void testTheJavaCurrentVersionRunnerIsProperlyRegisteredUsingTheHomeDirectory() throws Exception {
        File homeDir = new File(basePath, "home");
        runnerDir = new File(homeDir, "java/runner");
        properties.put("livingdoc.home", homeDir.getAbsolutePath());

        List<String> expectedCp = new ArrayList<String>();
        expectedCp.add(URIUtil.decoded(new File(runnerDir, "livingdoc-core-dummy.jar").getAbsolutePath()).toUpperCase());
        expectedCp.add(URIUtil.decoded(new File(runnerDir, "xmlrpc-2.0.1.jar").getAbsolutePath()).toUpperCase());
        expectedCp.add(URIUtil.decoded(new File(runnerDir, "commons-codec-1.3.jar").getAbsolutePath()).toUpperCase());

        new InitialDatas(this).insert();
        new DefaultRunners(this, properties).insertJavaRunner();

        Runner runner = sutDao.getRunnerByName("LDCore JAVA v. dummy");

        assertNotNull(runner);
        assertNull(runner.getServerName());
        assertNull(runner.getServerPort());

        List<String> runnerClasspaths = toUpperCaseList(runner.getClasspaths());
        assertTrue(runnerClasspaths.containsAll(expectedCp));
        assertTrue(expectedCp.containsAll(runnerClasspaths));
        assertTrue(expectedCp.containsAll(asPathList( ( runnerDir.listFiles() ))));
    }

    @Test
    public void testTheJavaCurrentVersionRunnerIsProperlyRegisteredUsingAJarFilePath() throws Exception {
        String jarVersion = "dummy";
        File homeDir = new File(basePath, "home");
        File runnerFile = new File(homeDir, "java/runner-plugin/livingdoc-confluence-plugin-" + jarVersion + ".jar");
        String runnerFilePath = runnerFile.getAbsolutePath();

        Properties customProperties = new Properties(properties);
        customProperties.put("livingdoc.path", runnerFilePath);
        // Make sure that only the jar from the path is loaded, so we need to
        // remove the other properties.
        customProperties.remove("livingdoc.home");
        customProperties.remove("baseUrl");

        List<String> expectedCp = new ArrayList<String>();
        expectedCp.add(URIUtil.decoded(runnerFilePath).toUpperCase());

        new InitialDatas(this).insert();
        new DefaultRunners(this, customProperties).insertJavaRunner();

        Runner runner = sutDao.getRunnerByName("LDCore JAVA v. " + jarVersion);

        assertNotNull(runner);
        assertNull(runner.getServerName());
        assertNull(runner.getServerPort());

        List<String> runnerClasspaths = toUpperCaseList(runner.getClasspaths());
        assertTrue(runnerClasspaths.containsAll(expectedCp));
        assertTrue(expectedCp.containsAll(runnerClasspaths));
    }

    private List<String> toUpperCaseList(Collection<String> classpaths) {
        List<String> paths = new ArrayList<String>();
        for (String path : classpaths) {
            paths.add(path.toUpperCase());
        }

        return paths;
    }

    private List<String> asPathList(File[] files) {
        List<String> paths = new ArrayList<String>();
        for (File file : files) {
            paths.add(URIUtil.decoded(file.getAbsolutePath()).toUpperCase());
        }

        return paths;
    }
}
