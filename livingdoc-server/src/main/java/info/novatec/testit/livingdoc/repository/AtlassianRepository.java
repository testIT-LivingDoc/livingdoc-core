package info.novatec.testit.livingdoc.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.URIUtil;


public class AtlassianRepository implements DocumentRepository {
    private final URI root;
    private String handler;
    private boolean includeStyle;
    private String username = "";
    private String password = "";

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
    public Document loadDocument(String location) throws XmlRpcException, IOException {
        String spec = retrieveSpecification(URI.create(URIUtil.raw(location)));
        return loadHtmlDocument(spec);
    }

    @Override
    // TODO exc: find fitting Exception for method
    // 'setDocumentAsImplemented(String location)'
    public void setDocumentAsImplemented(String location) throws MalformedURLException, XmlRpcException, IOException,
        Exception {
        Vector< ? > args = CollectionUtil.toVector(username, password, args(URI.create(URIUtil.raw(location))));
        XmlRpcClient xmlrpc = new XmlRpcClient(root.getScheme() + "://" + root.getAuthority() + root.getPath());
        String msg = ( String ) xmlrpc.execute(new XmlRpcRequest(handler + ".setSpecificationAsImplemented", args));

        if ( ! ( "<success>".equals(msg) )) {
            throw new Exception(msg);
        }

    }

    @Override
    public List<String> listDocuments(String uri) {
        return new ArrayList<String>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> listDocumentsInHierarchy() throws XmlRpcException, IOException {
        Vector< ? > args = CollectionUtil.toVector(username, password, CollectionUtil.toVector(root.getFragment()));
        XmlRpcClient xmlrpc = new XmlRpcClient(root.getScheme() + "://" + root.getAuthority() + root.getPath());
        XmlRpcRequest request = new XmlRpcRequest(handler + ".getSpecificationHierarchy", args);
        Vector<Object> response = ( Vector<Object> ) xmlrpc.execute(request);
        return response;
    }

    private String retrieveSpecification(URI location) throws XmlRpcException, IOException {
        Vector< ? > args = CollectionUtil.toVector(username, password, args(location));
        XmlRpcClient xmlrpc = new XmlRpcClient(root.getScheme() + "://" + root.getAuthority() + root.getPath());
        XmlRpcRequest request = new XmlRpcRequest(handler + ".getRenderedSpecification", args);
        String response = ( String ) xmlrpc.execute(request);
        return response;
    }

    private Document loadHtmlDocument(String content) throws IOException {
        Reader reader = new StringReader(content);
        try {
            return HtmlDocumentBuilder.tablesAndLists().build(reader);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private Vector<Object> args(URI location) {
        String[] locationArgs = location.getPath().split("/");
        Vector<Object> args = new Vector<Object>();
        args.add(root.getFragment());

        for (int i = 0; i < locationArgs.length; i ++ ) {
            args.add(locationArgs[i]);
        }

        args.add(includeStyle);

        String implemented = URIUtil.getAttribute(location, "implemented");
        args.add(implemented == null ? true : Boolean.valueOf(implemented));

        return args;
    }
}
