package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.document.Document;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Vector;

import static info.novatec.testit.livingdoc.util.CollectionUtil.toVector;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class LivingDocRepositoryTest {
    private static WebServer ws;
    @Mock
    private Handler handler;
    private String dummySpec;

    @BeforeClass
    public static void beforeClass() {
        ws = new WebServer(9005);
        ws.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        ws.shutdown();
    }

    @Before
    public void setUp() {
        ws.removeHandler("livingdoc1");
        ws.addHandler("livingdoc1", handler);

        dummySpec = TestStringSpecifications.SimpleAlternateCalculatorTest;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanProvideAListOfSpecificationsGivenASutAndARepository() throws Exception {
        final Vector<String> page1 = pageDefinition(1);
        final Vector<String> page2 = pageDefinition(2);
        final Vector<String> page3 = pageDefinition(3);
        doReturn(toVector(page1, page2, page3)).when(handler).getListOfSpecificationLocations("REPO", "SUT");

        DocumentRepository repo = new LivingDocRepository("http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT");
        List<String> actual = repo.listDocuments("REPO");

        verify(handler).getListOfSpecificationLocations("REPO", "SUT");
        List<String> expected = toVector("REPO/PAGE 1", "REPO/PAGE 2", "REPO/PAGE 3");
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDownloadPageContentFromConfluence() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE);
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected);

        DocumentRepository repo = new LivingDocRepository("http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT");
        Document document = repo.loadDocument("REPO/PAGE TITLE");

        verify(handler).getListOfSpecificationLocations("REPO", "SUT");
        verify(handler).getRenderedSpecification("user", "pwd", expected);
        assertEquals("html", document.getType());
        assertSpecification(dummySpec, document);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testhandlesImplementedVersionAttribute() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.FALSE);
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected);

        DocumentRepository repo = new LivingDocRepository(
                "http://localhost:9005/rpc/xmlrpc?implemented=false&handler=livingdoc1&sut=SUT");
        Document document = repo.loadDocument("REPO/PAGE TITLE");

        verify(handler).getListOfSpecificationLocations("REPO", "SUT");
        verify(handler).getRenderedSpecification("user", "pwd", expected);
        assertEquals("html", document.getType());
        assertSpecification(dummySpec, document);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHandlesPostBackExecutionResult() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected1 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE);
        final Vector<?> expected2 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", "SUT",
                TestStringSpecifications.SimpleAlternateCalculatorXmlReport);
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected1);
        doReturn("<success>").when(handler).saveExecutionResult("user", "pwd", expected2);

        DocumentRepository repo = new LivingDocRepository(
                "http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT&postExecutionResult=true");
        Document document = repo.loadDocument("REPO/PAGE TITLE");
        document.done();

        verify(handler).getListOfSpecificationLocations("REPO", "SUT");
        verify(handler).getRenderedSpecification("user", "pwd", expected1);
        verify(handler).saveExecutionResult("user", "pwd", expected2);
        assertEquals("html", document.getType());
        assertSpecification(dummySpec, document);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHandlesNoSuchMethodPostBackExecutionResultQuietly() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected1 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE);
        final Vector<?> expected2 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", "SUT",
                TestStringSpecifications.SimpleAlternateCalculatorXmlReport);
        final RuntimeException noSuchMethodException = new RuntimeException(new NoSuchMethodException());
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected1);
        doThrow(noSuchMethodException).when(handler).saveExecutionResult("user", "pwd", expected2);

        DocumentRepository repo = new LivingDocRepository(
                "http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT&postExecutionResult=true");
        Document document = repo.loadDocument("REPO/PAGE TITLE");
        document.done();

        verify(handler).getListOfSpecificationLocations("REPO", "SUT");
        verify(handler).getRenderedSpecification("user", "pwd", expected1);
        verify(handler).saveExecutionResult("user", "pwd", expected2);
        assertEquals("html", document.getType());
        assertSpecification(dummySpec, document);
    }

    @Test(expected = Exception.class)
    @SuppressWarnings("unchecked")
    public void testHandlesOtherXmlRpcExceptionPostBackExecutionResult() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected1 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE);
        final Vector<?> expected2 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", "SUT",
                TestStringSpecifications.SimpleAlternateCalculatorXmlReport);
        final XmlRpcException noSuchMethodException = new XmlRpcException(0, "junit");
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected1);
        doThrow(noSuchMethodException).when(handler).saveExecutionResult("user", "pwd", expected2);

        DocumentRepository repo = new LivingDocRepository(
                "http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT&postExecutionResult=true");
        Document document = repo.loadDocument("REPO/PAGE TITLE");

        try {
            document.done();
        } finally {
            verify(handler).getListOfSpecificationLocations("REPO", "SUT");
            verify(handler).getRenderedSpecification("user", "pwd", expected1);
            verify(handler).saveExecutionResult("user", "pwd", expected2);
            assertEquals("html", document.getType());
            assertSpecification(dummySpec, document);
        }
    }

    @Test(expected = Exception.class)
    @SuppressWarnings("unchecked")
    public void testHandlesUnsuccessfulPostBackExecutionResult() throws Exception {
        final Vector<String> page1 = confPageDefinition();
        final Vector<?> expected1 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", Boolean.TRUE, Boolean.TRUE);
        final Vector<?> expected2 = (Vector<?>) toVector("SPACE%20KEY", "PAGE TITLE", "SUT",
                TestStringSpecifications.SimpleAlternateCalculatorXmlReport);
        doReturn(toVector(page1)).when(handler).getListOfSpecificationLocations("REPO", "SUT");
        doReturn(dummySpec).when(handler).getRenderedSpecification("user", "pwd", expected1);
        doReturn("<failure>").when(handler).saveExecutionResult("user", "pwd", expected2);

        DocumentRepository repo = new LivingDocRepository(
                "http://localhost:9005/rpc/xmlrpc?handler=livingdoc1&sut=SUT&postExecutionResult=true");
        Document document = repo.loadDocument("REPO/PAGE TITLE");

        try {
            document.done();
        } finally {
            verify(handler).getListOfSpecificationLocations("REPO", "SUT");
            verify(handler).getRenderedSpecification("user", "pwd", expected1);
            verify(handler).saveExecutionResult("user", "pwd", expected2);
            assertEquals("html", document.getType());
            assertSpecification(dummySpec, document);
        }
    }

    @Test
    public void testComplainsIfArgumentsAreMissing() throws Exception {
        try {
            DocumentRepository repo = new LivingDocRepository("http://localhost:9005/rpc/xmlrpc?handler=livingdoc1");
            repo.listDocuments("REPO");
            fail();
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }

        try {
            DocumentRepository repo = new LivingDocRepository("http://localhost:9005/rpc/xmlrpc?handler=livingdoc1");
            repo.listDocuments("http://localhost:9005/rpc/xmlrpc?sut=SUT&includeStyle=true");
            fail();
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    private void assertSpecification(String expectedSpec, Document actualDoc) {
        assertNotNull(actualDoc);
        StringWriter buffer = new StringWriter();
        actualDoc.print(new PrintWriter(buffer));
        assertEquals(expectedSpec, buffer.toString());
    }

    private Vector<String> confPageDefinition() {
        Vector<String> def = new Vector<String>();
        def.add(AtlassianRepository.class.getName());
        def.add("http://localhost:9005/rpc/xmlrpc?handler=livingdoc1#SPACE%20KEY");
        def.add("user");
        def.add("pwd");
        def.add("PAGE TITLE");

        return def;
    }

    private Vector<String> pageDefinition(int identifier) {
        Vector<String> def = new Vector<String>();
        def.add("repoClass");
        def.add("testUrl" + identifier);
        def.add("user" + identifier);
        def.add("pwd" + identifier);
        def.add("PAGE " + identifier);

        return def;
    }

    public static interface Handler {
        Vector<Vector<String>> getListOfSpecificationLocations(String repoUID, String sutName);

        String getRenderedSpecification(String username, String password, Vector<?> args);

        String saveExecutionResult(String username, String password, Vector<?> args);
    }
}
