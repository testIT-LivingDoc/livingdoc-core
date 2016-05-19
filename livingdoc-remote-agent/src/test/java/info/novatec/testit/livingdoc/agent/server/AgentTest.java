package info.novatec.testit.livingdoc.agent.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.repository.AtlassianRepository;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.ClasspathSet;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.URIUtil;


@RunWith(MockitoJUnitRunner.class)
public class AgentTest {
    private int CURRENT_PORT = 18887;

    @Mock
    private Handler handler;
    private WebServer fakeServer;

    @Before
    public void setUp() {
        // XmlRpc.setDebug(true);
        CURRENT_PORT ++ ;
        fakeServer = new WebServer(CURRENT_PORT);
        fakeServer.addHandler("livingdoc1", handler);
        fakeServer.start();

        Agent.main(new String[0]);
    }

    @After
    public void tearDown() {
        Agent.shutdown();
        fakeServer.shutdown();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCanExecuteASpecification() throws Exception {
        List< ? > expected = CollectionUtil.toVector("SPACE", "MyTest", Boolean.FALSE, Boolean.TRUE);
        String testSpecification = testContent();

        doReturn(testSpecification).when(handler).getRenderedSpecification("", "", ( Vector< ? > ) expected);

        XmlRpcClient xmlrpc = new XmlRpcClient("http://localhost:7777");
        List<Serializable> params = CollectionUtil.toVector(getRunner().marshallize(), getSystemUnderTest().marshallize(),
            getSpecification().marshallize(), true, "", "en");
        List<Object> execParams = ( List<Object> ) xmlrpc.execute("livingdoc-agent1.execute", ( Vector< ? > ) params);
        Execution execution = XmlRpcDataMarshaller.toExecution(execParams);
        assertEquals(2, execution.getSuccess());
        assertEquals(0, execution.getErrors());
        assertEquals(0, execution.getIgnored());
        assertEquals(1, execution.getFailures());
        assertNull(execution.getExecutionErrorId());
    }

    private Runner getRunner() {
        Runner runner = Runner.newInstance("My Runner");
        ClasspathSet classPaths = new ClasspathSet();
        classPaths.add(getPath("livingdoc-core.jar"));
        classPaths.add(getPath("commons-codec.jar"));
        classPaths.add(getPath("xmlrpc.jar"));
        runner.setClasspaths(classPaths);
        return runner;
    }

    private Specification getSpecification() throws LivingDocServerException {
        Repository repository = Repository.newInstance("My Repository");
        repository.setBaseTestUrl("http://localhost:" + CURRENT_PORT + "/rpc/xmlrpc?handler=livingdoc1#SPACE");
        RepositoryType type = RepositoryType.newInstance("JIRA");
        type.setTestUrlFormat("%s/%s");
        type.setClassName(AtlassianRepository.class.getName());
        repository.setType(type);
        repository.setProject(Project.newInstance("My Project"));

        Specification specification = Specification.newInstance("MyTest");
        repository.addSpecification(specification);
        return specification;
    }

    private SystemUnderTest getSystemUnderTest() {
        SystemUnderTest sut = SystemUnderTest.newInstance("My Sut");
        ClasspathSet classPaths = new ClasspathSet();
        classPaths.add(getPath("livingdoc-core-tests.jar"));
        sut.setProject(Project.newInstance("My Project"));
        sut.setFixtureClasspaths(classPaths);
        return sut;
    }

    private String getPath(String fileName) {
        return URIUtil.decoded(new File(AgentTest.class.getResource("/runners/java/" + fileName).getPath())
            .getAbsolutePath());
    }

    private String testContent() {
        return "<html><table border='1' cellspacing='0'>"
            + "<tr><td>Rule for</td><td colspan='3'>info.novatec.testit.livingdoc.fixture.interpreter.CellAnnotationFixture</td></tr>"
            + "<tr><td>comparisonValue</td><td>returnedValue</td><td>annotation?</td></tr>"
            + "<tr><td>1</td><td>2</td><td>wrong</td></tr>" + "<tr><td>2</td><td>2</td><td>right</td></tr>"
            + "<tr><td>2</td><td>3</td><td>right</td></tr>" + "</table></html>";
    }

    public static class MyFixture {
        public int a;
        public int b;

        public MyFixture() {
        }

        public int sum() {
            return a + b;
        }

    }

    public static interface Handler {
        String getRenderedSpecification(String username, String password, Vector< ? > args);
    }
}
