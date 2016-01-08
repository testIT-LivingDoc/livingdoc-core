package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_REPOSITORY_UID_INDEX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_SECTION_INDEX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_SUT_NAME_INDEX;

import java.util.Vector;

import org.apache.commons.lang3.StringUtils;


public class ReferenceNode extends DocumentNode implements Marshalizable {
    private String repositoryUID;
    private String sutName;
    private String section;

    public ReferenceNode(String title, String repositoryUID, String sutName, String section) {
        super(title);
        this.repositoryUID = repositoryUID;
        this.sutName = sutName;
        this.section = section;
    }

    public String getRepositoryUID() {
        return repositoryUID;
    }

    public String getSutName() {
        return sutName;
    }

    public String getSection() {
        return section;
    }

    @Override
    public void addChildren(DocumentNode child) {
        throw new RuntimeException("Reference node should not have children");
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> vector = super.marshallize();
        vector.add(NODE_REPOSITORY_UID_INDEX, repositoryUID);
        vector.add(NODE_SUT_NAME_INDEX, sutName);
        vector.add(NODE_SECTION_INDEX,StringUtils.stripToEmpty(section));
        return vector;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof ReferenceNode )) {
            return false;
        }

        ReferenceNode nodeCompared = ( ReferenceNode ) o;
        if (super.equals(o)) {
            return getRepositoryUID().equals(nodeCompared.getRepositoryUID()) && getSutName().equals(nodeCompared
                .getSutName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + getRepositoryUID().hashCode() + getSutName().hashCode();
    }
}
