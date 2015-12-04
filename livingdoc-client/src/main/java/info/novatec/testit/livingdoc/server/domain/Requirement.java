package info.novatec.testit.livingdoc.server.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;


/**
 * Requirement Class.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */

@Entity
@Table(name = "REQUIREMENT", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "REPOSITORY_ID" }) })
@SuppressWarnings("serial")
public class Requirement extends Document {
    protected Set<Reference> references = new HashSet<Reference>();

    public static Requirement newInstance(String name) {
        Requirement requirement = new Requirement();
        requirement.setName(name);
        return requirement;
    }

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    public Set<Reference> getReferences() {
        return references;
    }

    public void setReferences(Set<Reference> references) {
        this.references = references;
    }

    public void removeReference(Reference reference) throws LivingDocServerException {
        if ( ! references.contains(reference)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REFERENCE_NOT_FOUND, "Reference not found");
        }

        references.remove(reference);
        reference.setRequirement(null);
    }

    @Transient
    public RequirementSummary getSummary() {
        RequirementSummary summary = new RequirementSummary();
        for (Reference ref : references) {
            if (ref.getLastExecution() != null) {
                summary.addErrors(ref.getLastExecution().getErrors());
                summary.addException(ref.getLastExecution().hasException());
                summary.addFailures(ref.getLastExecution().getFailures());
                summary.addSuccess(ref.getLastExecution().getSuccess());
                summary.addErrors(ref.getLastExecution().getErrors());
            }
        }

        summary.setReferencesSize(references.size());
        return summary;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return o instanceof Requirement;
        }

        return false;
    }
}
