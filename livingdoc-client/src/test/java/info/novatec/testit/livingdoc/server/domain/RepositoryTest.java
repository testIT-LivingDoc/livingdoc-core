package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;


public class RepositoryTest {
    private static Repository repository1, repository2;

    @BeforeClass
    public static void beforeClass() {
        repository1 = Repository.newInstance("UID-1");
        repository2 = Repository.newInstance("UID-2");
    }

    @Test
    public void testBasicEqualsBehavior() {
        assertFalse(repository1.equals(null));
        assertFalse(repository1.equals(new Integer(0)));
        assertTrue(repository1.equals(repository1));
        assertFalse(repository1.equals(repository2));
    }

    @Test
    public void testAddingARequirementItWillBeInTheRequirementsList() throws LivingDocServerException {
        Repository repository = Repository.newInstance("UID");
        Requirement requirement = Requirement.newInstance("REQ");
        repository.addRequirement(requirement);

        assertTrue(repository.getRequirements().contains(requirement));
        assertEquals(repository, requirement.getRepository());
    }

    @Test
    public void testAddingARequirementIsAlreadyInTheRequirementsListWillTriggerAnAxception() {
        Set<Requirement> requirements = new HashSet<Requirement>();
        Repository repository = Repository.newInstance("UID");
        Requirement requirement = Requirement.newInstance("REQ");
        requirements.add(requirement);
        repository.setRequirements(requirements);

        try {
            repository.addRequirement(requirement);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REQUIREMENT_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testAddingARequirementWithTheSameNameWillTriggerAnAxception() {
        Set<Requirement> requirements = new HashSet<Requirement>();
        Repository repository = Repository.newInstance("UID");

        Requirement requirement = Requirement.newInstance("REQ");
        requirement.setName("NAME");
        requirements.add(requirement);
        repository.setRequirements(requirements);

        requirement = Requirement.newInstance("REQ2");
        requirement.setName("name");

        try {
            repository.addRequirement(requirement);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REQUIREMENT_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testRemovingARequirementItWillBeNoLongerInTheRequirementsList() throws LivingDocServerException {
        Set<Requirement> requirements = new HashSet<Requirement>();
        Repository repository = Repository.newInstance("UID");
        Requirement requirement = Requirement.newInstance("REQ");
        requirements.add(requirement);
        repository.setRequirements(requirements);

        repository.removeRequirement(requirement);
        assertFalse(repository.getRequirements().contains(requirement));
        assertNull(requirement.getRepository());
    }

    @Test
    public void testRemovingARequirementIsNotInTheRequirementsListWillTriggerAnAxception() {
        Repository repository = Repository.newInstance("UID");
        Requirement requirement = Requirement.newInstance("REQ");

        try {
            repository.removeRequirement(requirement);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REQUIREMENT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testAddingASpecificationItWillBeInTheSpecificationsList() throws LivingDocServerException {
        Repository repository = Repository.newInstance("UID");
        Specification specification = Specification.newInstance("SPEC");
        repository.addSpecification(specification);

        assertTrue(repository.getSpecifications().contains(specification));
        assertEquals(repository, specification.getRepository());
    }

    @Test
    public void testAddingASpecificationIsAlreadyInTheSpecificationsListWillTriggerAnAxception() {
        Set<Specification> specifications = new HashSet<Specification>();
        Repository repository = Repository.newInstance("UID");
        Specification specification = Specification.newInstance("SPEC");
        specifications.add(specification);
        repository.setSpecifications(specifications);

        try {
            repository.addSpecification(specification);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testAddingASpecificationWithTheSameNameWillTriggerAnAxception() {
        Set<Specification> specifications = new HashSet<Specification>();
        Repository repository = Repository.newInstance("UID");
        Specification specification = Specification.newInstance("SPEC");
        specification.setName("NAME");
        specifications.add(specification);
        repository.setSpecifications(specifications);

        specification = Specification.newInstance("SPEC2");
        specification.setName("name");
        try {
            repository.addSpecification(specification);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testRemovingASpecificationItWillBeNoLongerInTheSpecificationsList() throws LivingDocServerException {
        Set<Specification> specifications = new HashSet<Specification>();
        Repository repository = Repository.newInstance("UID");
        Specification specification = Specification.newInstance("SPEC");
        specifications.add(specification);
        repository.setSpecifications(specifications);

        repository.removeSpecification(specification);
        assertFalse(repository.getSpecifications().contains(specification));
        assertNull(specification.getRepository());
    }

    @Test
    public void testRemovingASpecificationIsNotInTheSpecificationsListWillTriggerAnAxception() {
        Repository repository = Repository.newInstance("UID");
        Specification specification = Specification.newInstance("SPEC");

        try {
            repository.removeSpecification(specification);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, e.getId());
        }
    }

}
