package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rest.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;

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

    @Mock
    private RestClient restClient;
    private AtlassianRepository atlassianRepository;


    @Test
    public void testCannotProvideListOfSpecifications() throws Exception {
        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE_KEY"));
        assertTrue(atlassianRepository.listDocuments("PAGE").isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProvideAHierarchyListOfSpecifications() throws Exception {

        final Vector<?> expected1 = toVector("SPACE_KEY");
        final Vector<?> expected2 = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.listDocumentsInHierarchy(expected1)).thenReturn((List) hierarchy());
        when(restClient.getRenderedSpecification(expected2)).thenReturn(specification());

        List<Object> docs = atlassianRepository.listDocumentsInHierarchy();
        Hashtable<String, String> pageBranch = (Hashtable<String, String>) docs.get(2);
        atlassianRepository.loadDocument(pageBranch.keySet().iterator().next());

        verify(restClient).listDocumentsInHierarchy(expected1);
        verify(restClient).getRenderedSpecification(expected2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAConfluenceServer() throws Exception {

        final Vector<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=true#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        Document spec = atlassianRepository.loadDocument("PAGE");
        verify(restClient).getRenderedSpecification(expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAJiraServer() throws Exception {

        final Vector<?> expected = toVector("SPACE_KEY", "ISSUE_KEY", Boolean.FALSE, Boolean.TRUE);

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1&includeStyle=false#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        Document spec = atlassianRepository.loadDocument("ISSUE_KEY");
        verify(restClient).getRenderedSpecification(expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testImplementedIsPassedInTheArgumenents() throws Exception {

        final Vector<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.FALSE);

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        atlassianRepository.loadDocument("PAGE?implemented=false");
        verify(restClient).getRenderedSpecification(expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStyleDefaultsToTrueAndImplementedDefaultsToTrue() throws Exception {

        final Vector<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        atlassianRepository.loadDocument("PAGE");
        verify(restClient).getRenderedSpecification(expected);
    }

    @Test(expected = RestClientException.class)
    public void testComplainsIfArgumentsAreMissing() throws Exception {
        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?includeStyle=true"));
        atlassianRepository.loadDocument("ISSUE KEY?");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWeCanSetASpecificationAsImplemented() throws Exception {

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<success>");

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

        atlassianRepository = spy(new AtlassianRepository("http://localhost:19005/rpc/xmlrpc?handler=livingdoc1#SPACE_KEY"));
        when(atlassianRepository.getRestClient()).thenReturn(restClient);
        when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<error>");

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

}
