package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_LAST_EXECUTION_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_REQUIREMENT_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SECTIONS_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SPECIFICATION_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REFERENCE_SUT_IDX;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * TestCase Class. Main association class between a requirement, a test
 * docuement and a system under test.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */

@Entity
@Table(name = "REFERENCE",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "REQUIREMENT_ID", "SPECIFICATION_ID", "SUT_ID", "SECTIONS" }) })
@SuppressWarnings("serial")
public class Reference extends AbstractUniqueEntity implements Comparable<Reference> {
    private String sections;
    private Requirement requirement;
    private Specification specification;
    private SystemUnderTest systemUnderTest;
    private Execution lastExecution;

    public static Reference newInstance(Requirement requirement, Specification specification, SystemUnderTest sut) {
        return newInstance(requirement, specification, sut, null);
    }

    public static Reference newInstance(Requirement requirement, Specification specification, SystemUnderTest sut,
        String sections) {
        Reference reference = new Reference();
        reference.setSections(sections);

        reference.setRequirement(requirement);
        reference.setSpecification(specification);
        reference.setSystemUnderTest(sut);
        requirement.getReferences().add(reference);
        specification.getReferences().add(reference);

        return reference;
    }

    @Basic
    @Column(name = "SECTIONS", nullable = true, length = 50)
    public String getSections() {
        return sections;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.ALL })
    @JoinColumn(name = "REQUIREMENT_ID")
    public Requirement getRequirement() {
        return requirement;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "SPECIFICATION_ID")
    public Specification getSpecification() {
        return specification;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "SUT_ID")
    public SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "LAST_EXECUTION_ID")
    public Execution getLastExecution() {
        return lastExecution;
    }

    public void setSections(String sections) {
        this.sections = StringUtils.stripToNull(sections);
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void setSystemUnderTest(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public void setLastExecution(Execution lastExecution) {
        this.lastExecution = lastExecution;
    }

    @Transient
    public String getStatus() {
        return lastExecution != null ? lastExecution.getStatus() : Execution.IGNORED;
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REFERENCE_REQUIREMENT_IDX, requirement.marshallize());
        parameters.add(REFERENCE_SPECIFICATION_IDX, specification.marshallize());
        parameters.add(REFERENCE_SUT_IDX, systemUnderTest.marshallize());
        parameters.add(REFERENCE_SECTIONS_IDX,StringUtils.stripToEmpty(sections));

        parameters.add(REFERENCE_LAST_EXECUTION_IDX, lastExecution != null ? lastExecution.marshallize() : Execution.none()
            .marshallize());
        return parameters;
    }

    public Execution execute(boolean implementedVersion, String locale) {
        return systemUnderTest.execute(specification, implementedVersion, sections, locale);
    }

    @Override
    public int compareTo(Reference referenceCompared) {
        int compare = specification.compareTo(referenceCompared.specification);
        if (compare != 0) {
            return compare;
        }

        compare = requirement.compareTo(referenceCompared.requirement);
        if (compare != 0) {
            return compare;
        }

        compare = systemUnderTest.compareTo(referenceCompared.systemUnderTest);
        if (compare != 0) {
            return compare;
        }

        return ObjectUtils.compare(sections, referenceCompared.sections);
    }

    private boolean isSystemUnderTestEqualsTo(SystemUnderTest systemUnderTestToCompare) {
        return systemUnderTest != null && systemUnderTest.equalsTo(systemUnderTestToCompare);
    }

    private boolean isRequirementEqualsTo(Requirement requirementToCompare) {
        return requirement != null && requirement.equalsTo(requirementToCompare);
    }

    private boolean isSpecificationEqualsTo(Specification specificationToCompare) {
        return specification != null && specification.equalsTo(specificationToCompare);
    }

    public boolean equalsTo(Object o) {
        if (o == null || ! ( o instanceof Reference )) {
            return false;
        }

        Reference refCompared = ( Reference ) o;

        return StringUtils.equals(sections, refCompared.sections)
                && isSystemUnderTestEqualsTo(refCompared.systemUnderTest)
                && isRequirementEqualsTo(refCompared.requirement)
                && isSpecificationEqualsTo(refCompared.specification);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof Reference )) {
            return false;
        }

        return super.equals(o);
    }
}
