package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SPECIFICATION_SUTS_IDX;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;


/**
 * Specification Class.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */

@Entity
@Table(name = "SPECIFICATION", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "REPOSITORY_ID" }) })
@SuppressWarnings("serial")
public class Specification extends Document {
    private Set<SystemUnderTest> targetedSystemUnderTests = new HashSet<SystemUnderTest>();
    protected Set<Reference> references = new HashSet<Reference>();
    private Set<Execution> executions = new HashSet<Execution>();

    public static Specification newInstance(String name) {
        Specification specification = new Specification();
        specification.setName(name);
        return specification;
    }

    @ManyToMany(targetEntity = SystemUnderTest.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "SUT_SPECIFICATION",
        joinColumns = { @JoinColumn(name = "SPECIFICATION_ID") },
        inverseJoinColumns = { @JoinColumn(name = "SUT_ID") })
    public Set<SystemUnderTest> getTargetedSystemUnderTests() {
        return targetedSystemUnderTests;
    }

    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL)
    public Set<Execution> getExecutions() {
        return this.executions;
    }

    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL)
    public Set<Reference> getReferences() {
        return references;
    }

    public void setTargetedSystemUnderTests(Set<SystemUnderTest> targetedSystemUnderTests) {
        this.targetedSystemUnderTests = targetedSystemUnderTests;
    }

    public void setExecutions(Set<Execution> executions) {
        this.executions = executions;
    }

    public void setReferences(Set<Reference> references) {
        this.references = references;
    }

    public void addSystemUnderTest(SystemUnderTest systemUnderTest) {
        targetedSystemUnderTests.add(systemUnderTest);
    }

    public void addExecution(Execution execution) {
        execution.setSpecification(this);
        executions.add(execution);
    }

    public void removeSystemUnderTest(SystemUnderTest systemUnderTest) {
        targetedSystemUnderTests.remove(systemUnderTest);
        if (targetedSystemUnderTests.isEmpty()) {
            addSystemUnderTest(getRepository().getProject().getDefaultSystemUnderTest());
        }
    }

    public void removeReference(Reference reference) throws LivingDocServerException {
        if ( ! references.contains(reference)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REFERENCE_NOT_FOUND, "Reference not found");
        }

        references.remove(reference);
        reference.setSpecification(null);
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = super.marshallize();
        List<Object> suts = XmlRpcDataMarshaller.toXmlRpcSystemUnderTestsParameters(targetedSystemUnderTests);
        parameters.add(SPECIFICATION_SUTS_IDX, suts);
        return parameters;
    }

    public Specification marshallizeRest() {

        Specification returnValue = Specification.newInstance(this.getName());
        returnValue.setId(this.getId());
        returnValue.setUUID(this.getUUID());
        returnValue.setVersion(this.getVersion());
        returnValue.setRepository(this.getRepository() != null ? this.getRepository().marshallizeRest() : Repository.newInstance(""));
        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && o instanceof Specification;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        return hashCode;
    }
}
