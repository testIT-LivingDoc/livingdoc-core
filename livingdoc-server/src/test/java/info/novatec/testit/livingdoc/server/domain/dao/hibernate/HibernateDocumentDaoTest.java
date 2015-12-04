package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.SortedSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.dao.DocumentDao;


public class HibernateDocumentDaoTest extends AbstractDBUnitHibernateMemoryTest {
    private static final String DATAS = "/dbunit/datas/HibernateDocumentDaoTest.xml";
    private DocumentDao documentDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        insertIntoDatabase(DATAS);
        documentDao = new HibernateDocumentDao(this);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testTheCRUDOfARequirement() throws LivingDocServerException {
        /* CAN Retrieve a Requirement */
        assertNotNull(documentDao.getRequirementByName("UID-1", "REQUIREMENT-1"));

        /* CAN create a NEW Requirement */
        session.getTransaction().begin();
        Requirement requirement = documentDao.createRequirement("UID-1", "REQUIREMENT-CREATED");
        session.getTransaction().commit();
        Requirement loadedReq = getById(Requirement.class, requirement.getId());
        assertNotNull(loadedReq);
        assertEquals("REQUIREMENT-CREATED", loadedReq.getName());
        assertEquals("UID-1", loadedReq.getRepository().getUid());

        /* Creating NEW Requirement requires an Existing Repository */
        try {
            session.getTransaction().begin();
            documentDao.createRequirement("REPOSITORY-NOT-FOUND", "REQUIREMENT-CREATED");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, e.getId());
        }

        /* We Can Force The Creation Of The Requirement If Not Found */
        session.getTransaction().begin();
        Requirement requirementToCreate = documentDao.getOrCreateRequirement("UID-1", "REQUIREMENT-NOT-FOUND");
        session.getTransaction().commit();

        loadedReq = getById(Requirement.class, requirementToCreate.getId());
        assertEquals("REQUIREMENT-NOT-FOUND", loadedReq.getName());
        assertEquals("UID-1", loadedReq.getRepository().getUid());
        requirement = documentDao.getOrCreateRequirement("UID-1", "REQUIREMENT-NOT-FOUND");
        assertEquals(requirementToCreate, requirement);

        /* We Can Delete A Requirement */
        session.getTransaction().begin();
        requirement = Requirement.newInstance("REQUIREMENT-TO-REMOVE");
        requirement.setRepository(Repository.newInstance("UID-1"));
        documentDao.removeRequirement(requirement);
        session.getTransaction().commit();
        assertNull(getById(Requirement.class, 2));
    }

    @Test
    public void testTheCRUDOfASpecification() throws LivingDocServerException {
        /* CAN Retrieve a Specification */
        assertNotNull(documentDao.getSpecificationByName("UID-1", "SPECIFICATION-1"));

        /* We Can Create A New Specification */
        session.getTransaction().begin();
        Specification specification = documentDao.createSpecification("SUT-1", "UID-1", "SPECIFICATION-CREATED");
        session.getTransaction().commit();

        Specification loadedSpec = getById(Specification.class, specification.getId());
        assertNotNull(loadedSpec);
        assertEquals("SPECIFICATION-CREATED", loadedSpec.getName());
        assertEquals("UID-1", loadedSpec.getRepository().getUid());

        /* When Creating A New Specification The Repository Must Exists */
        try {
            session.getTransaction().begin();
            documentDao.createSpecification("SUT-1", "REPOSITORY-NOT-FOUND", "REQUIREMENT-CREATED");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, e.getId());
        }

        /* We Can Force The Creation Of The Specification If Not Found */
        session.getTransaction().begin();
        Specification specificationToCreate = documentDao.getOrCreateSpecification("SUT-1", "UID-1",
            "SPECIFICATION-NOT-FOUND");
        session.getTransaction().commit();

        loadedSpec = getById(Specification.class, specificationToCreate.getId());
        assertEquals("SPECIFICATION-NOT-FOUND", loadedSpec.getName());
        assertEquals("UID-1", loadedSpec.getRepository().getUid());

        specification = documentDao.getOrCreateSpecification("SUT-1", "UID-1", "SPECIFICATION-NOT-FOUND");
        assertEquals(specificationToCreate, specification);

        /* Creating A Specification Without Specifying A Sut Will Associate The
         * Default Sut */
        session.getTransaction().begin();
        Specification spec = documentDao.createSpecification(null, "UID-1", "SPECIFICATION-NEW");
        SortedSet<SystemUnderTest> suts = spec.getTargetedSystemUnderTests();
        SystemUnderTest sut = suts.iterator().next();
        assertEquals("SUT-DEFAULT", sut.getName());
        session.getTransaction().commit();

        /* Creating A Specification Without Specifying A Sut And No Default Sut
         * Is Defined Will Throw AnException */
        try {
            session.getTransaction().begin();
            documentDao.createSpecification(null, "UID-WITHOUT-DEFAULT-SUT", "SPECIFICATION-WITHOUT-DEFAULT-SUT");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_DEFAULT_SUT_NOT_FOUND, e.getId());
        }

        /* We Can Associate A Given Sut For A New Specification */
        session.getTransaction().begin();
        spec = documentDao.createSpecification("SUT-1", "UID-1", "SPECIFICATION-NEW-2");
        sut = spec.getTargetedSystemUnderTests().iterator().next();
        session.getTransaction().commit();
        assertEquals("SUT-1", sut.getName());

        /* An Error Occures If The Sut Doesnt Exists At The Specification
         * Creation */
        try {
            session.getTransaction().begin();
            documentDao.createSpecification("SUT-NOT-FOUND", "UID-1", "SPECIFICATION-1");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_NOT_FOUND, e.getId());
        }

        /* We Can Update A Specification */
        Specification oldSpec = Specification.newInstance("SPECIFICATION-TO-UPDATE");
        oldSpec.setRepository(Repository.newInstance("UID-1"));
        Specification updatedSpec = Specification.newInstance("SPECIFICATION-UPDATED");
        updatedSpec.setRepository(Repository.newInstance("UID-1"));

        session.getTransaction().begin();
        documentDao.updateSpecification(oldSpec, updatedSpec);
        session.getTransaction().commit();

        loadedSpec = getById(Specification.class, - 1l);
        assertEquals("SPECIFICATION-UPDATED", loadedSpec.getName());
        assertEquals("UID-1", loadedSpec.getRepository().getUid());

        /* An Error Occures If We Try To Update A None Existing Specification */
        oldSpec = Specification.newInstance("SPECIFICATION-NOT-FOUND-2");
        oldSpec.setRepository(Repository.newInstance("UID-1"));
        updatedSpec = Specification.newInstance("SPECIFICATION-UPDATED-2");
        updatedSpec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.updateSpecification(oldSpec, updatedSpec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCanRetrieveTheSortedSpecificationListForAGivenSutAndRepository() {
        Project project = Project.newInstance("PROJECT-1");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        sut.setProject(project);
        Repository repository = Repository.newInstance("UID-1");

        List<Specification> specifications = documentDao.getSpecifications(sut, repository);

        assertEquals(3, specifications.size());
        assertTrue(isAlphabeticallyOrdered(specifications));
    }

    @Test
    public void testTheBehaviorOfASpecificationWithinAReference() throws LivingDocServerException {
        /* We Can Remove A Specification Only If It Is Not Part Of AReference */
        Specification spec = Specification.newInstance("SPECIFICATION-TO-REMOVE");
        spec.setRepository(Repository.newInstance("UID-1"));

        session.getTransaction().begin();
        documentDao.removeSpecification(spec);
        session.getTransaction().commit();

        /* We Can Remove A Specification If It Is Part Of A Reference */
        spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));

        Specification existingSpec = documentDao.getSpecificationByName(spec.getRepository().getUid(), spec.getName());
        assertFalse(existingSpec.getReferences().isEmpty());

        session.getTransaction().begin();
        documentDao.removeSpecification(spec);
        session.getTransaction().commit();

        assertTrue(documentDao.getAllReferences(spec).isEmpty());
    }

    @Test
    public void testTheBehaviorOfASpecificationAndTheSystemUnderTest() throws LivingDocServerException {
        /* We Can Associate A Sut To A Specification */
        SystemUnderTest systemUnderTest = SystemUnderTest.newInstance("SUT-1");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        Specification spec = Specification.newInstance("SPECIFICATION-TO-ASSOCIATE");
        spec.setRepository(Repository.newInstance("UID-1"));

        session.getTransaction().begin();
        documentDao.addSystemUnderTest(systemUnderTest, spec);
        session.getTransaction().commit();

        Specification loadedSpec = getById(Specification.class, 5l);
        assertTrue(containsSut(loadedSpec.getTargetedSystemUnderTests(), systemUnderTest.getName()));

        /* An Error Occures If We Try To Associate A Sut To A None Existing
         * Specification */
        systemUnderTest = SystemUnderTest.newInstance("SUT-1");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-NOT-FOUND");
        spec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.addSystemUnderTest(systemUnderTest, spec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, e.getId());
        }

        /* An Error Occures If We Try To Associate A None Existing Sut To A
         * Specification */
        systemUnderTest = SystemUnderTest.newInstance("SUT-NOT-FOUND");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-1");
        spec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.addSystemUnderTest(systemUnderTest, spec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_NOT_FOUND, e.getId());
        }

        /* We Can Deassociate A Sut To A Specification */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-TO-DEASSOCIATE");
        spec.setRepository(Repository.newInstance("UID-1"));

        session.getTransaction().begin();
        documentDao.removeSystemUnderTest(systemUnderTest, spec);
        session.getTransaction().commit();

        loadedSpec = getById(Specification.class, 6l);
        loadedSpec.getTargetedSystemUnderTests().contains(systemUnderTest);
        assertFalse(containsSut(loadedSpec.getTargetedSystemUnderTests(), systemUnderTest.getName()));

        /* An Error Occures If We Try To Deassociate A Sut To A None Existing
         * Specification */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-NOT-FOUND");
        spec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.removeSystemUnderTest(systemUnderTest, spec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, e.getId());
        }

        /* An Error Occures If We Try To Deassociate A None Existing Sut To A
         * Specification */
        systemUnderTest = SystemUnderTest.newInstance("SUT-NOT-FOUND");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-1");
        spec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.removeSystemUnderTest(systemUnderTest, spec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_NOT_FOUND, e.getId());
        }

        /* An Error Occures If We Try To Deassociate A Sut From A Specification
         * If Both Are In The Same Reference */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));

        try {
            session.getTransaction().begin();
            documentDao.removeSystemUnderTest(systemUnderTest, spec);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_REFERENCE_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testTheCRUDOfAReference() throws LivingDocServerException {
        /* We Can Retrieve A Reference For A Given Specification And Requirement
         * And Sut */
        SystemUnderTest systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        Specification spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));
        Requirement requirement = Requirement.newInstance("REQUIREMENT-REFERENCED");
        requirement.setRepository(Repository.newInstance("UID-1"));
        Reference reference = Reference.newInstance(requirement, spec, systemUnderTest);
        reference.setSections("SECTION-1");

        assertNotNull(documentDao.get(reference));

        /* We Can Retrieve All References For A Given Requirement */
        requirement = Requirement.newInstance("REQUIREMENT-REFERENCED");
        requirement.setRepository(Repository.newInstance("UID-1"));

        List<Reference> references = documentDao.getAllReferences(requirement);
        assertEquals(2, references.size());

        /* We Can Create A New Reference */
        systemUnderTest = SystemUnderTest.newInstance("SUT-1");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-1");
        spec.setRepository(Repository.newInstance("UID-1"));
        requirement = Requirement.newInstance("REQUIREMENT-1");
        requirement.setRepository(Repository.newInstance("UID-1"));
        reference = Reference.newInstance(requirement, spec, systemUnderTest);
        reference.setSections("SECTION-1");

        session.getTransaction().begin();
        reference = documentDao.createReference(reference);
        session.getTransaction().commit();

        Reference loadedRef = getById(Reference.class, reference.getId());
        assertEquals(reference, loadedRef);

        /* We Can Remove A Reference */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));
        requirement = Requirement.newInstance("REQUIREMENT-REFERENCED");
        requirement.setRepository(Repository.newInstance("UID-1"));
        reference = Reference.newInstance(requirement, spec, systemUnderTest);
        reference.setSections("SECTION-1");

        session.getTransaction().begin();
        documentDao.removeReference(reference);
        session.getTransaction().commit();

        loadedRef = getById(Reference.class, 1l);
        assertNull(loadedRef);

        /* Updating A Reference Will Delete The Current One And Create A New One */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED-2");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-REFERENCED-3");
        spec.setRepository(Repository.newInstance("UID-1"));
        requirement = Requirement.newInstance("REQUIREMENT-REFERENCED-2");
        requirement.setRepository(Repository.newInstance("UID-1"));
        Reference oldReference = Reference.newInstance(requirement, spec, systemUnderTest);
        oldReference.setSections("SECTION-1");

        Reference newReference = Reference.newInstance(requirement, spec, systemUnderTest);
        newReference.setSections("SECTION-2");

        session.getTransaction().begin();
        documentDao.updateReference(oldReference, newReference);
        session.getTransaction().commit();

        assertNull(getById(Reference.class, 1l));
        assertNotNull(documentDao.get(newReference));
        assertNotNull(getById(Requirement.class, 1l));

        /* Removing The Only Reference Of A Requirement Will Remove The
         * Requirement Also */
        systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));
        requirement = Requirement.newInstance("REQUIREMENT-REFERENCED-ONCE");
        requirement.setRepository(Repository.newInstance("UID-1"));
        reference = Reference.newInstance(requirement, spec, systemUnderTest);
        reference.setSections("SECTION-1");

        session.getTransaction().begin();
        documentDao.removeReference(reference);
        session.getTransaction().commit();

        assertNull(getById(Requirement.class, - 1l));
    }

    @Test
    public void testTheCreateOfAnExistingReferenceMustDetectDuplicates() throws Exception {
        /* We Can Retrieve A Reference For A Given Specification And Requirement
         * And Sut */
        SystemUnderTest systemUnderTest = SystemUnderTest.newInstance("SUT-REFERENCED");
        systemUnderTest.setProject(Project.newInstance("PROJECT-1"));
        Specification spec = Specification.newInstance("SPECIFICATION-REFERENCED");
        spec.setRepository(Repository.newInstance("UID-1"));
        Requirement requirement = Requirement.newInstance("REQUIREMENT-REFERENCED");
        requirement.setRepository(Repository.newInstance("UID-1"));
        Reference reference = Reference.newInstance(requirement, spec, systemUnderTest);
        reference.setSections("SECTION-1");

        assertNotNull(documentDao.get(reference));

        try {
            session.getTransaction().begin();
            documentDao.createReference(reference);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REFERENCE_CREATE_ALREADYEXIST, e.getId());
        }

        /* If the section value not the same, creation should be ok */
        session.getTransaction().begin();
        reference.setSections("SECTION-2");
        documentDao.createReference(reference);
        session.getTransaction().commit();
    }

    @Test
    public void testSpecificationExecutionsRetrieval() {
        Specification spec = documentDao.getSpecificationByName("UID-1", "SPECIFICATION-1");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");

        List<Execution> executions = documentDao.getSpecificationExecutions(spec, null, 100);
        assertNotNull(executions);
        assertTrue(executions.size() == 2);
        assertEquals(new Long(1), executions.get(0).getId());
        assertEquals(new Long(2), executions.get(1).getId());

        spec = documentDao.getSpecificationByName("UID-1", "SPECIFICATION-2");

        executions = documentDao.getSpecificationExecutions(spec, sut, 0);
        assertNotNull(executions);
        assertTrue(executions.size() == 0);

        executions = documentDao.getSpecificationExecutions(spec, sut, 100);
        assertNotNull(executions);
        assertTrue(executions.size() == 1);

        Execution execution = documentDao.getSpecificationExecution(executions.get(0).getId());
        assertNotNull(execution);
        assertEquals(new Long(3), execution.getId());
    }

    @Test
    public void testWeCanRetrieveSpecificationById() {
        Specification spec = documentDao.getSpecificationById(1L);
        assertNotNull(spec);
        assertEquals("SPECIFICATION-1", spec.getName());

        spec = documentDao.getSpecificationById( - 999L);
        assertNull(spec);
    }

    private boolean isAlphabeticallyOrdered(List<Specification> specifications) {
        Object[] specs = specifications.toArray();
        for (int i = 0; i < specs.length; i ++ ) {
            for (int j = i; j < specs.length; j ++ ) {
                if ( ( ( Specification ) specs[j] ).getName().compareTo( ( ( Specification ) specs[i] ).getName()) < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean containsSut(SortedSet<SystemUnderTest> suts, String sutName) {
        for (SystemUnderTest sut : suts) {
            if (sut.getName().equals(sutName)) {
                return true;
            }
        }

        return false;
    }
}
