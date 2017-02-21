package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rest.LivingDocRestClient;
import info.novatec.testit.livingdoc.server.rest.RestClient;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class AtlassianRepository implements DocumentRepository {
    private final URI root;
    @Deprecated
    private String handler;
    private boolean includeStyle;
    private String username = "";
    private String password = "";
    private RestClient restClient = null;

    public AtlassianRepository(String... args) throws IllegalArgumentException {
        this.root = URI.create(URIUtil.raw(args[0]));

        String includeAtt = URIUtil.getAttribute(root, "includeStyle");
        includeStyle = includeAtt == null ? true : Boolean.valueOf(includeAtt);

        handler = URIUtil.getAttribute(root, "handler");
        if (handler == null) {
            throw new IllegalArgumentException("Missing handler");
        }

        if (args.length == 3) {
            username = args[1];
            password = args[2];
        }
    }

    @Override
    public Document loadDocument(final String location) throws XmlRpcException, IOException {
        String spec = retrieveSpecification(URI.create(URIUtil.raw(location)));
        return loadHtmlDocument(spec);
    }

    @Override
    public void setDocumentAsImplemented(final String location) throws LivingDocServerException, XmlRpcException, IOException {

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
    public List<Object> listDocumentsInHierarchy() throws XmlRpcException, IOException {
        List<?> args = CollectionUtil.toVector(username, password, CollectionUtil.toVector(root.getFragment()));
        XmlRpcClient xmlrpc = new XmlRpcClient(root.getScheme() + "://" + root.getAuthority() + root.getPath());
        XmlRpcRequest request = new XmlRpcRequest(handler + ".getSpecificationHierarchy", (Vector<?>) args);
        return (Vector<Object>) xmlrpc.execute(request);
    }

    protected RestClient getRestClient() {
        if (this.restClient == null) {

            StringBuilder url = new StringBuilder();
            url.append(root.getScheme())
                    .append("://")
                    .append(root.getAuthority())
                    // + root.path()         // TODO confluence/rpc/xmlrpc (view Repository.getBaseTestUrl)
                    .append("/confluence");  // TODO It's a patch while we are migrating to REST

            this.restClient = new LivingDocRestClient(url.toString(), username, password);
        }
        return restClient;
    }

    private String retrieveSpecification(URI location) throws XmlRpcException, IOException {
        List<?> args = CollectionUtil.toVector(username, password, args(location));
        XmlRpcClient xmlrpc = new XmlRpcClient(root.getScheme() + "://" + root.getAuthority() + root.getPath());
        XmlRpcRequest request = new XmlRpcRequest(handler + ".getRenderedSpecification", (Vector<?>) args);
        return (String) xmlrpc.execute(request);
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
        Vector<Object> args = new Vector<Object>();
        args.add(root.getFragment());

        for (int i = 0; i < locationArgs.length; i++) {
            args.add(locationArgs[i]);
        }

        args.add(includeStyle);

        String implemented = URIUtil.getAttribute(location, "implemented");
        args.add(implemented == null ? true : Boolean.valueOf(implemented));

        return args;
    }
}
