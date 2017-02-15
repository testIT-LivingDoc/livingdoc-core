package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_CAN_BE_IMPLEMENTED_INDEX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_CHILDREN_INDEX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_EXECUTABLE_INDEX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_TITLE_INDEX;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


public class DocumentNode implements Comparable<DocumentNode>, Marshalizable {

    private final String title;
    private boolean executable;
    private boolean canBeImplemented;

    private final List<DocumentNode> children = new ArrayList<DocumentNode>();

    public DocumentNode(final String title) {
        this.title = title;
    }

    public List<DocumentNode> getChildren() {
        return children;
    }

    public String getTitle() {
        return title;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setIsExecutable(boolean executable) {
        this.executable = executable;
    }

    public boolean isCanBeImplemented() {
        return canBeImplemented;
    }

    public void setCanBeImplemented(boolean canBeImplemented) {
        this.canBeImplemented = canBeImplemented;
    }

    public void addChildren(DocumentNode child) {
        children.add(child);
    }

    public boolean hasChildren() {
        return !children.isEmpty() ;
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> vector = new Vector<Object>();
        vector.add(NODE_TITLE_INDEX, title);
        vector.add(NODE_EXECUTABLE_INDEX, executable);
        vector.add(NODE_CAN_BE_IMPLEMENTED_INDEX, canBeImplemented);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        for (DocumentNode node : children) {
            hashtable.put(node.getTitle(), node.marshallize());
        }

        vector.add(NODE_CHILDREN_INDEX, hashtable);

        return vector;
    }

    @Override
    public int compareTo(DocumentNode node) {
        return title.compareTo(node.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof DocumentNode )) {
            return false;
        }

        DocumentNode nodeCompared = ( DocumentNode ) o;

        return getTitle().equals(nodeCompared.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
