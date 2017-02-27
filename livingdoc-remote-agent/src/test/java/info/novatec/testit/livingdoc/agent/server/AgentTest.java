package info.novatec.testit.livingdoc.agent.server;

import info.novatec.testit.livingdoc.repository.AtlassianRepository;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.URIUtil;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({Agent.class, ServiceImpl.class})
public class AgentTest {

    private int CURRENT_PORT = 18887;

    @Mock
    private Handler handler;
    private WebServer fakeServer;

    @Mock
    private ServiceImpl service;


    @Before
    public void setUp() throws Exception {

        Execution execution = new Execution();
        execution.setSuccess(2);
        execution.setFailures(1);
        List<Object> execParams = execution.marshallize();
        PowerMockito.whenNew(ServiceImpl.class).withAnyArguments().thenReturn(service);
        when(service.execute((Vector<Object>) anyVararg(), (Vector<Object>) anyVararg(), (Vector<Object>) anyVararg(), anyBoolean(), anyString(), anyString())).thenReturn(execParams);

        // XmlRpc.setDebug(true);
        CURRENT_PORT++;
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

        XmlRpcClient xmlrpc = new XmlRpcClient("http://localhost:7777");
        Vector<Serializable> params = CollectionUtil.toVector(getRunner().marshallize(), getSystemUnderTest().marshallize(),
                getSpecification().marshallize(), true, "", "en");
        Vector<Object> execParams = (Vector<Object>) xmlrpc.execute("livingdoc-agent1.execute", params);
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

    public interface Handler {
    }

}
