package info.novatec.testit.livingdoc.repository;

import info.novatec.testit.livingdoc.*;
import info.novatec.testit.livingdoc.document.*;
import info.novatec.testit.livingdoc.report.*;
import info.novatec.testit.livingdoc.server.*;
import info.novatec.testit.livingdoc.server.rest.*;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.slf4j.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import static info.novatec.testit.livingdoc.util.LoggerConstants.*;

public class LivingDocRepository implements DocumentRepository {
    private static final Logger LOG = LoggerFactory.getLogger(LivingDocRepository.class);

    @Deprecated
    private static final String XML_RPC = "/rpc/xmlrpc";

    private URI root;
    private String handler;
    private String sut;
    private boolean includeStyle;
    private Boolean implemented;
    private boolean postExecutionResult;
    private String username = "";
    private String password = "";
    private RestClient restClient = null;

    public LivingDocRepository(String... args) throws IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No root specified");
        }
        this.root = URI.create(URIUtil.raw(args[0]));

        String includeAtt = URIUtil.getAttribute(root, "includeStyle");
        includeStyle = includeAtt == null || Boolean.valueOf(includeAtt);

        String implementedAtt = URIUtil.getAttribute(root, "implemented");
        if (implementedAtt != null) {
            implemented = Boolean.parseBoolean(implementedAtt);
        }

        handler = URIUtil.getAttribute(root, "handler");
        if (handler == null) {
            throw new IllegalArgumentException("No handler specified");
        }

        sut = URIUtil.getAttribute(root, "sut");
        if (sut == null) {
            throw new IllegalArgumentException("No sut specified");
        }

        String postExecutionResultAtt = URIUtil.getAttribute(root, "postExecutionResult");
        if (postExecutionResultAtt != null) {
            postExecutionResult = Boolean.parseBoolean(postExecutionResultAtt);
        }

        if (args.length == 3) {
            username = args[1];
            password = args[2];
        }
    }

    @Override
    public void setDocumentAsImplemented(String location) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public List<String> listDocuments(String uri) throws LivingDocServerException {

        String repoUID = getPath(uri);
        if (repoUID == null) {
            throw new LivingDocServerException(new UnsupportedDocumentException("Missing repo UID"));
        }

        List<List<String>> definitions = downloadSpecificationsDefinitions(repoUID);
        List<String> documentsURI = new ArrayList<String>();
        for (List<String> definition : definitions) {
            String docName = repoUID + "/" + definition.get(4);
            documentsURI.add(docName);
        }

        return documentsURI;
    }

    private String getPath(String uri) {
        return URI.create(URIUtil.raw(uri)).getPath();
    }

    @SuppressWarnings("unchecked")
    private List<List<String>> downloadSpecificationsDefinitions(String repoUID) throws LivingDocServerException {
        try {
            List<List<String>> definitions = getRestClient().getListOfSpecificationLocations(repoUID, sut);
            LOG.debug(ToStringBuilder.reflectionToString(definitions));
            checkForErrors(definitions);
            return definitions;
        } catch (LivingDocServerException e) {
            LOG.error(LOG_ERROR, e);
            throw new LivingDocServerException(e);
        }
    }

    @Override
    public List<Object> listDocumentsInHierarchy() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Hierarchy not supported");
    }

    @Override
    public Document loadDocument(String location) throws LivingDocServerException {
        try {
            List<String> definition = getDefinition(location);

            DocumentRepository repository = getRepository(definition);

            String specLocation = definition.get(4) + (implemented != null ? "?implemented=" + implemented : "");

            Document document = repository.loadDocument(specLocation);

            if (postExecutionResult) {
                document.setSpecificationListener(new PostExecutionResultSpecificationListener(definition, document));
            }

            return document;
        } catch (Exception e) {
            throw new LivingDocServerException(e);
        }
    }

    private DocumentRepository getRepository(List<String> definition) throws SecurityException, IllegalArgumentException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> klass = ClassUtils.loadClass(definition.get(0));
        Constructor<?> constructor = klass.getConstructor(String[].class);
        return (DocumentRepository) constructor.newInstance(new Object[]{args(definition)});
    }

    private List<String> getDefinitionFor(List<List<String>> definitions, String location) throws DocumentNotFoundException {
        for (List<String> def : definitions) {
            if (def.get(4).equals(location)) {
                return def;
            }
        }
        throw new DocumentNotFoundException(location);
    }

    private List<String> getDefinition(String location) throws DocumentNotFoundException, LivingDocServerException {
        String path = getPath(location);
        String[] parts = path.split("/", 2);
        if (parts.length == 1) {
            DocumentNotFoundException e = new DocumentNotFoundException(location);
            LOG.error(LOG_ERROR, e);
            throw e;
        }

        String repoUID = parts[0];
        List<List<String>> definitions = downloadSpecificationsDefinitions(repoUID);
        return getDefinitionFor(definitions, parts[1]);
    }

    protected RestClient getRestClient() {
        if (this.restClient == null) {

            String serverBaseURL = root.getScheme() +
                    "://" +
                    root.getAuthority() +                               // TODO confluence/rpc/xmlrpc (view Repository.getBaseTestUrl)
                    root.getPath().replace(XML_RPC, StringUtils.EMPTY); // TODO It's a patch while we are migrating to REST

            this.restClient = new LivingDocRestClient(serverBaseURL, username, password);
        }
        return restClient;
    }


    private String[] args(List<String> definition) {
        String[] args = new String[3];
        args[0] = includeStyle ? definition.get(1) : withNoStyle(definition.get(1));
        args[1] = StringUtils.isEmpty(username) ? definition.get(2) : username;
        args[2] = StringUtils.isEmpty(password) ? definition.get(3) : password;
        return args;
    }

    private String withNoStyle(String location) {
        URI uri = URI.create(URIUtil.raw(location));
        StringBuilder sb = new StringBuilder(22);
        if (uri.getScheme() != null) {
            sb.append(uri.getScheme()).append("://");
        }
        if (uri.getAuthority() != null) {
            sb.append(uri.getAuthority());
        }
        if (uri.getPath() != null) {
            sb.append(uri.getPath());
        }

        String query = uri.getQuery();
        if (query == null) {
            sb.append("??includeStyle=false");
        } else {
            sb.append('?').append(query).append("&includeStyle=false");
        }

        if (uri.getFragment() != null) {
            sb.append('#').append(uri.getFragment());
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private void checkForErrors(Object xmlRpcResponse) throws LivingDocServerException {
        if (xmlRpcResponse instanceof Vector) {
            List<?> temp = (List<?>) xmlRpcResponse;
            if (!temp.isEmpty()) {
                checkErrors(temp.get(0));
            }
        } else if (xmlRpcResponse instanceof Hashtable) {
            Hashtable<String, ?> table = (Hashtable<String, ?>) xmlRpcResponse;
            if (!table.isEmpty()) {
                checkForErrors(table.get("<exception>"));
            }
        } else {
            checkErrors(xmlRpcResponse);
        }
    }

    private void checkErrors(Object object) throws LivingDocServerException {
        if (object instanceof Exception) {
            throw new LivingDocServerException((Exception) object);
        }

        if (object instanceof String) {
            String msg = (String) object;
            if (!StringUtils.isEmpty(msg) && msg.indexOf("<exception>") > -1) {
                throw new LivingDocServerException("XmlRpcResponse contains error ", msg.replace("<exception>", ""));
            }
        }
    }

    private final class PostExecutionResultSpecificationListener implements SpecificationListener {
        private final List<String> definitionRef;
        private final Document documentRef;

        private PostExecutionResultSpecificationListener(List<String> definitionRef, Document documentRef) {
            this.definitionRef = definitionRef;
            this.documentRef = documentRef;
        }

        @Override
        public void exampleDone(Example table, Statistics statistics) {
            // No implementation needed.
        }

        @Override
        public void specificationDone(Example spec, Statistics statistics) {
            try {
                String[] args1 = args(definitionRef);
                URI location = URI.create(URIUtil.raw(definitionRef.get(1)));
                List<Serializable> args = CollectionUtil.toList(args1[1], args1[2], (ArrayList<String>) CollectionUtil
                        .toList(location.getFragment(), definitionRef.get(4), sut, XmlReport.toXml(documentRef)));

                String msg = getRestClient().saveExecutionResult(args);

                if (!("<success>".equals(msg))) {
                    throw new IllegalStateException(msg);
                }
            } catch (LivingDocServerException e) {
                // Old server / incompatible method ?
                if (e.getMessage().indexOf(NoSuchMethodException.class.getName()) == -1) {
                    // @todo : Log ? Critical ? Do we want the test execution to
                    // fail if we can't post the result back ?
                    throw ExceptionImposter.imposterize(e);
                }
            } catch (IllegalStateException e) {
                // @todo : Log ? Critical ? Do we want the test execution to
                // fail if we can't post the result back ?
                throw ExceptionImposter.imposterize(e);
            } catch (IOException e) {
                // @todo : Log ? Critical ? Do we want the test execution to
                // fail if we can't post the result back ?
                throw ExceptionImposter.imposterize(e);
            }
        }
    }
}
