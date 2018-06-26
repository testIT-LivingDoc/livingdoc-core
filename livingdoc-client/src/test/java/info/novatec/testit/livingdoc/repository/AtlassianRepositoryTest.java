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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static info.novatec.testit.livingdoc.util.CollectionUtil.toVector;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AtlassianRepositoryTest {

    @Mock
    private RestClient restClient;

    private AtlassianRepository cut;


    @Test
    public void testCannotProvideListOfSpecifications() throws Exception {
        cut = spy(new AtlassianRepository("http://localhost:19005?includeStyle=true#SPACE_KEY"));
        assertTrue(cut.listDocuments("PAGE").isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProvideAHierarchyListOfSpecifications() throws Exception {

        final List<?> expected1 = toVector("SPACE_KEY");
        final List<?> expected2 = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        cut = spy(new AtlassianRepository("http://localhost:19005?includeStyle=true#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.listDocumentsInHierarchy(expected1)).thenReturn((List) hierarchy());
        when(restClient.getRenderedSpecification(expected2)).thenReturn(specification());

        List<Object> docs = cut.listDocumentsInHierarchy();
        Hashtable<String, String> pageBranch = (Hashtable<String, String>) docs.get(2);
        cut.loadDocument(pageBranch.keySet().iterator().next());

        verify(restClient).listDocumentsInHierarchy(expected1);
        verify(restClient).getRenderedSpecification(expected2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAConfluenceServer() throws Exception {

        final List<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        cut = spy(new AtlassianRepository("http://localhost:19005?includeStyle=true#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        Document spec = cut.loadDocument("PAGE");
        verify(restClient).getRenderedSpecification(expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCanDowloadPageContentFromAJiraServer() throws Exception {

        final List<?> expected = toVector("SPACE_KEY", "ISSUE_KEY", Boolean.FALSE, Boolean.TRUE);

        cut = spy(new AtlassianRepository("http://localhost:19005?includeStyle=false#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        Document spec = cut.loadDocument("ISSUE_KEY");
        verify(restClient).getRenderedSpecification(expected);
        assertSpecification(spec);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testImplementedIsPassedInTheArgumenents() throws Exception {

        final List<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.FALSE);

        cut = spy(new AtlassianRepository("http://localhost:19005?#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        cut.loadDocument("PAGE?implemented=false");
        verify(restClient).getRenderedSpecification(expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStyleDefaultsToTrueAndImplementedDefaultsToTrue() throws Exception {

        final List<?> expected = toVector("SPACE_KEY", "PAGE", Boolean.TRUE, Boolean.TRUE);

        cut = spy(new AtlassianRepository("http://localhost:19005?#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.getRenderedSpecification(expected)).thenReturn(specification());

        cut.loadDocument("PAGE");
        verify(restClient).getRenderedSpecification(expected);
    }

    @Test(expected = RestClientException.class)
    public void testComplainsIfArgumentsAreMissing() throws Exception {
        cut = spy(new AtlassianRepository("http://localhost:19005?includeStyle=true"));
        cut.loadDocument("ISSUE KEY?");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWeCanSetASpecificationAsImplemented() throws Exception {

        cut = spy(new AtlassianRepository("http://localhost:19005?#SPACE_KEY"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<success>");

        try {
            cut.setDocumentAsImplemented("PAGE1");

            List<?> expected = Arrays.asList("SPACE_KEY", "PAGE1", Boolean.TRUE, Boolean.TRUE);
            verify(restClient).setSpecificationAsImplemented(expected);

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Test(expected = LivingDocServerException.class)
    @SuppressWarnings("unchecked")
    public void testExceptionIsThrownIfReturnedValueFromSettingAsImplementedDiffersFromSuccess() throws Exception {

        cut = spy(new AtlassianRepository("http://localhost:19005"));
        when(cut.getRestClient()).thenReturn(restClient);
        when(restClient.setSpecificationAsImplemented(anyList())).thenReturn("<error>");

        cut.setDocumentAsImplemented("PAGE");
    }

    private void assertSpecification(Document doc) {
        assertNotNull(doc);
        StringWriter buffer = new StringWriter();
        doc.print(new PrintWriter(buffer));
        assertEquals(specification(), buffer.toString());
    }

    private List<Object> hierarchy() {
        List<Object> hierachy = new ArrayList<Object>();
        hierachy.add("1");
        hierachy.add("HOME");

        Hashtable<String, List<Object>> pageBranch = new Hashtable<String, List<Object>>();
        List<Object> page = new ArrayList<Object>();
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
