package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rest.RestClient;
import org.apache.xmlrpc.WebServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static info.novatec.testit.livingdoc.util.CollectionUtil.toVector;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AtlassianRepositoryTest {

    private static WebServer ws;
    private static RestClient restClient;
    private static AtlassianRepository atlassianRepository;
    @Mock
    @Deprecated
    private Handler handler;
    private DocumentRepository repo;

    @BeforeClass
    public static void beforeClass() {
        ws = new WebServer(19005);
        ws.start();

        restClient = mock(RestClient.class);
        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE_KEY"));
        Mockito.when(atlassianRepository.getRestClient()).thenReturn(restClient);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        ws.shutdown();
    }

    @Before
    public void setUp() {
        ws.removeHandler("livingdoc1");
        ws.addHandler("livingdoc1", handler);
    }

    @Test
    public void testCannotProvideListOfSpecifications() throws Exception {
        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE KEY");
        assertTrue(repo.listDocuments("PAGE").isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProvideAHierarchyListOfSpecifications() throws Exception {
        final Vector<?> expected1 = (Vector<?>) toVector("SPACE KEY");
        final Vector<?> expected2 = (Vector<?>) toVector("SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);
        doReturn(hierarchy()).when(handler).getSpecificationHierarchy("", "", expected1);
        doReturn(specification()).when(handler).getRenderedSpecification("", "", expected2);

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE KEY");
        List<Object> docs = repo.listDocumentsInHierarchy();
        Hashtable<String, String> pageBranch = (Hashtable<String, String>) docs.get(2);
        repo.loadDocument(pageBranch.keySet().iterator().next());

        verify(handler).getSpecificationHierarchy("", "", expected1);
        verify(handler).getRenderedSpecification("", "", expected2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAConfluenceServer() throws Exception {
        final Vector<?> expected = (Vector<?>) toVector("SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);
        doReturn(specification()).when(handler).getRenderedSpecification("", "", expected);

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE KEY");
        Document spec = repo.loadDocument("PAGE");

        verify(handler).getRenderedSpecification("", "", expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAJiraServer() throws Exception {
        final Vector<?> expected = (Vector<?>) toVector("PROJECT ID", "ISSUE KEY", Boolean.FALSE, Boolean.TRUE);
        doReturn(specification()).when(handler).getRenderedSpecification("", "", expected);

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=false#PROJECT ID");
        Document spec = repo.loadDocument("ISSUE KEY");

        verify(handler).getRenderedSpecification("", "", expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testImplementedIsPassedInTheArgumenents() throws Exception {
        final Vector<?> expected = (Vector<?>) toVector("SPACE KEY", "PAGE", Boolean.TRUE, Boolean.FALSE);
        doReturn(specification()).when(handler).getRenderedSpecification("", "", expected);

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE KEY");
        repo.loadDocument("PAGE?implemented=false");

        verify(handler).getRenderedSpecification("", "", expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStyleDefaultsToTrueAndImplementedDefaultsToTrue() throws Exception {
        final Vector<?> expected = (Vector<?>) toVector("SPACE KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);
        doReturn(specification()).when(handler).getRenderedSpecification("", "", expected);

        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE KEY");
        repo.loadDocument("PAGE");

        verify(handler).getRenderedSpecification("", "", expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComplainsIfArgumentsAreMissing() throws Exception {
        repo = new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?includeStyle=true");
        repo.loadDocument("ISSUE KEY?");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWeCanSetASpecificationAsImplemented() throws Exception {

        Mockito.when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<success>");
        try {
            atlassianRepository.setDocumentAsImplemented("PAGE1");

            List<?> expected = Arrays.asList("SPACE_KEY", "PAGE1", Boolean.TRUE, Boolean.TRUE);
            verify(restClient).setSpecificationAsImplemented(expected);

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Test(expected = LivingDocServerException.class)
    @SuppressWarnings("unchecked")
    public void testExceptionIsThrownIfReturnedValueFromSettingAsImplementedDiffersFromSuccess() throws Exception {

        Mockito.when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<error>");
        atlassianRepository.setDocumentAsImplemented("PAGE");
    }

    private void assertSpecification(Document doc) {
        assertNotNull(doc);
        StringWriter buffer = new StringWriter();
        doc.print(new PrintWriter(buffer));
        assertEquals(specification(), buffer.toString());
    }

    private List<Object> hierarchy() {
        List<Object> hierachy = new Vector<Object>();
        hierachy.add("1");
        hierachy.add("HOME");

        Hashtable<String, Vector<Object>> pageBranch = new Hashtable<String, Vector<Object>>();
        Vector<Object> page = new Vector<Object>();
        page.add("PAGE");
        page.add(new Hashtable<Object, Object>());
        pageBranch.put("PAGE", page);

        hierachy.add(pageBranch);

        return hierachy;
    }

    private String specification() {
        return "<html><table border='1' cellspacing='0'>" + "<tr><td colspan='3'>My Fixture</td></tr>"
                + "<tr><td>a</td><td>b</td><td>sum()</td></tr>" + "<tr><td>1</td><td>2</td><td>3</td></tr>"
                + "<tr><td>2</td><td>3</td><td>15</td></tr>" + "<tr><td>2</td><td>3</td><td>a</td></tr>" + "</table></html>";
    }

    public interface Handler {
        String getRenderedSpecification(String username, String password, Vector<?> args);

        Vector<?> getSpecificationHierarchy(String username, String password, Vector<?> args);

        String setSpecificationAsImplemented(String username, String password, Vector<?> args);
    }
}
