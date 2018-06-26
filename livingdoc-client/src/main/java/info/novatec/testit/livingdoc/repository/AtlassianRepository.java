package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rest.LivingDocRestClient;
import info.novatec.testit.livingdoc.server.rest.RestClient;
import info.novatec.testit.livingdoc.util.URIUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AtlassianRepository implements DocumentRepository {

    private final URI root;
    private final boolean includeStyle;
    private String username = "";
    private String password = "";
    private RestClient restClient = null;

    public AtlassianRepository(String... args) {
        this.root = URI.create(URIUtil.raw(args[0]));

        String includeAtt = URIUtil.getAttribute(root, "includeStyle");
        includeStyle = includeAtt == null ? true : Boolean.valueOf(includeAtt);

        if (args.length == 3) {
            username = args[1];
            password = args[2];
        }
    }

    @Override
    public Document loadDocument(final String location) throws LivingDocServerException, IOException {
        String spec = retrieveSpecification(URI.create(URIUtil.raw(location)));
        return loadHtmlDocument(spec);
    }

    @Override
    public void setDocumentAsImplemented(final String location) throws LivingDocServerException {

        String msg = getRestClient().setSpecificationAsImplemented(args(URI.create(URIUtil.raw(location))));

        if (!("<success>".equals(msg))) {
            throw new LivingDocServerException(null, msg);
        }
    }

    @Override
    public List<String> listDocuments(final String uri) {
        return new ArrayList<String>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> listDocumentsInHierarchy() throws LivingDocServerException {

        return (List<Object>) getRestClient().listDocumentsInHierarchy(Collections.singletonList(root.getFragment()));
    }

    protected RestClient getRestClient() {
        if (this.restClient == null) {

            String serverBaseURL = root.getScheme() +
                    "://" +
                    root.getAuthority() +
                    root.getPath();

            this.restClient = new LivingDocRestClient(serverBaseURL, username, password);
        }
        return restClient;
    }

    private String retrieveSpecification(URI location) throws LivingDocServerException {

        return getRestClient().getRenderedSpecification(args(location));
    }

    private Document loadHtmlDocument(String content) throws IOException {
        Reader reader = new StringReader(content);
        try {
            return HtmlDocumentBuilder.tablesAndLists().build(reader);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private List<Object> args(URI location) {
        String[] locationArgs = location.getPath().split("/");
        List<Object> args = new ArrayList<Object>();
        args.add(root.getFragment());

        Collections.addAll(args, locationArgs);

        args.add(includeStyle);

        String implemented = URIUtil.getAttribute(location, "implemented");
        args.add(implemented == null ? Boolean.valueOf(true) : Boolean.valueOf(implemented));


        return args;
    }
}
