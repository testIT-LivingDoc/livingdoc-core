package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ReferenceTest {
    @Test
    public void testThatAReferenceCanBeCreated() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        Specification specification = Specification.newInstance("SPEC-1");
        Requirement requirement = Requirement.newInstance("REQ-1");

        Reference reference = Reference.newInstance(requirement, specification, sut);

        assertEquals(sut, reference.getSystemUnderTest());

        assertEquals(specification, reference.getSpecification());
        assertTrue(specification.getReferences().contains(reference));

        assertEquals(requirement, reference.getRequirement());
        assertTrue(requirement.getReferences().contains(reference));
    }

    @Test
    public void testReferenceWithSectionIsGreater() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        Specification specification = Specification.newInstance("SPEC-1");
        Requirement requirement = Requirement.newInstance("REQ-1");

        Reference reference = Reference.newInstance(requirement, specification, sut);
        Reference referenceSection = Reference.newInstance(requirement, specification, sut, "SECTION-A");

        assertTrue(referenceSection.compareTo(reference) > 0);
    }

}
