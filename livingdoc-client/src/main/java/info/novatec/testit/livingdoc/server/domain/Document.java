package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.DOCUMENT_NAME_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.DOCUMENT_REPOSITORY_IDX;



import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


@MappedSuperclass
public abstract class Document extends AbstractUniqueEntity implements Comparable<Document> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String resolvedName;
    private Repository repository;

    @Basic
    @Column(name = "NAME", nullable = false, length = 255)
    public String getName() {
        return this.name;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "REPOSITORY_ID")
    public Repository getRepository() {
        return this.repository;
    }

    public void setResolvedName(String resolvedName) {
        this.resolvedName = resolvedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Transient
    public String getResolvedName() {
        if (resolvedName != null) {
            return resolvedName;
        }
        return repository.resolveName(this);
    }

    @Override
    public List<Object> marshallize() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(DOCUMENT_NAME_IDX, name);
        parameters.add(DOCUMENT_REPOSITORY_IDX, repository.marshallize());
        return parameters;
    }

    private boolean isNameEqualsTo(String nameToCompare) {
        return getName() != null && getName().equals(nameToCompare);
    }

    private boolean isRepositoryEqualsTo(Repository repositoryToCompare) {
        return getRepository() != null && getRepository().equals(repositoryToCompare);
    }

    public boolean equalsTo(Object o) {
        if (o == null || ! ( o instanceof Document )) {
            return false;
        }

        Document docCompared = ( Document ) o;

        return isNameEqualsTo(docCompared.getName()) && isRepositoryEqualsTo(docCompared.getRepository());
    }

    @Override
    public int compareTo(Document o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof Document )) {
            return false;
        }

        return super.equals(o);
    }
}
