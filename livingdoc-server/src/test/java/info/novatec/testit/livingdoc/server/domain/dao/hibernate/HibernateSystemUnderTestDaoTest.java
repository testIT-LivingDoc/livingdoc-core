package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.ClasspathSet;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;


public class HibernateSystemUnderTestDaoTest extends AbstractDBUnitHibernateMemoryTest {
    private static final String DATAS = "/dbunit/datas/HibernateSystemUnderTestDaoTest.xml";
    private SystemUnderTestDao systemUnderTestDao;
    private ClasspathSet sutPaths;
    private ClasspathSet fixturePaths;
    private ClasspathSet runnerClassPaths;
    private ClasspathSet updatedClassPaths;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        insertIntoDatabase(DATAS);
        systemUnderTestDao = new HibernateSystemUnderTestDao(this);
        sutPaths = new ClasspathSet();
        sutPaths.add("SUT-PATH-1");
        sutPaths.add("SUT-PATH-2");
        fixturePaths = new ClasspathSet();
        fixturePaths.add("FIXTURE-PATH-1");
        fixturePaths.add("FIXTURE-PATH-2");
        runnerClassPaths = new ClasspathSet();
        runnerClassPaths.add("RUNNER-PATH-1");
        runnerClassPaths.add("RUNNER-PATH-2");
        updatedClassPaths = new ClasspathSet();
        updatedClassPaths.add("RUNNER-PATH-UPDATED");
        updatedClassPaths.add("RUNNER-PATH-UPDATED");
    }

    @Override
    @After
    public void tearDown() throws Exception {
        if (session != null) {
            closeSession();
        }
        super.tearDown();
    }

    @Test
    public void testARunnerCanBeSelectedbyName() {
        assertNull(systemUnderTestDao.getRunnerByName("RUNNER-NOT-FOUND"));
        assertNotNull(systemUnderTestDao.getRunnerByName("RUNNER-1"));
    }

    @Test
    public void testWeCanRetrieveAllAvailableRunners() {
        assertEquals(4, systemUnderTestDao.getAllRunners().size());
    }

    @Test
    public void testWeCanUpdateARunner() throws LivingDocServerException {
        Runner runner = Runner.newInstance("RUNNER-TO-UPDATE");
        runner.setServerName("SNAME_UPDATED");
        runner.setServerPort("SPORTUP");
        runner.setClasspaths(updatedClassPaths);

        session.getTransaction().begin();
        systemUnderTestDao.update("RUNNER-TO-UPDATE", runner);
        session.getTransaction().commit();

        Runner runnerDB = getById(Runner.class, - 1l);
        assertEquals("SNAME_UPDATED", runnerDB.getServerName());
        assertEquals("SPORTUP", runnerDB.getServerPort());
        assertTrue(updatedClassPaths.containsAll(runnerDB.getClasspaths()));
        assertTrue(runnerDB.getClasspaths().containsAll(updatedClassPaths));
    }

    @Test
    public void testWeCantUpdateANotFoundRunner() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.update("RUNNER-NOT-FOUND", Runner.newInstance("TO-SOME-RUNNER"));
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCantUpdateARunnerToAnAlreadyUsedName() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.update("RUNNER-TO-UPDATE", Runner.newInstance("RUNNER-1"));
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testARunnerCanBeCreated() throws LivingDocServerException {
        Runner runner = Runner.newInstance("RUNNER_CREATED");
        runner.setServerName("SERVERNAME");
        runner.setServerPort("8080");
        runner.setClasspaths(runnerClassPaths);

        session.getTransaction().begin();
        systemUnderTestDao.create(runner);
        session.getTransaction().commit();

        assertNotNull(getById(Runner.class, runner.getId()));
        assertEquals("RUNNER_CREATED", runner.getName());
        assertEquals("SERVERNAME", runner.getServerName());
        assertEquals("8080", runner.getServerPort());
        assertTrue(runnerClassPaths.containsAll(runner.getClasspaths()));
        assertTrue(runner.getClasspaths().containsAll(runnerClassPaths));
    }

    @Test
    public void testARunnerCantBeCreatedIfAnotherRunnerExistsUnderTheSameName() {
        try {
            session.getTransaction().begin();
            Runner runner = Runner.newInstance("RUNNER-1");
            systemUnderTestDao.create(runner);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testWeCanRemoveARunner() throws LivingDocServerException {
        session.getTransaction().begin();
        systemUnderTestDao.removeRunner("RUNNER-TO-REMOVE");
        session.getTransaction().commit();

        assertNull(getById(Runner.class, - 2l));
    }

    @Test
    public void testWeCantRemoveANoneExistingRunner() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.removeRunner("RUNNER-NOT-FOUND");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCantRemoveARunnerIfASutIsReferecingIt() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.removeRunner("RUNNER-1");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_SUT_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testASutCanBeSelectedbyName() {
        assertNull(systemUnderTestDao.getByName("PROJECT-NOT-FOUND", "SUT-1"));
        assertNull(systemUnderTestDao.getByName("PROJECT-1", "SUT-NOT-FOUND"));
        assertNotNull(systemUnderTestDao.getByName("PROJECT-1", "SUT-1"));
    }

    @Test
    public void testASutCanBeCreated() throws LivingDocServerException {

        Project project = Project.newInstance("PROJECT-1");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-CREATED");
        sut.setProject(project);
        sut.setFixtureClasspaths(fixturePaths);
        sut.setFixtureFactory(null);
        sut.setFixtureFactoryArgs(null);
        sut.setIsDefault(true);
        sut.setProjectDependencyDescriptor(null);
        sut.setRunner(Runner.newInstance("RUNNER-1"));

        session.getTransaction().begin();
        systemUnderTestDao.create(sut);
        session.getTransaction().commit();

        assertNotNull(getById(SystemUnderTest.class, sut.getId()));
    }

    @Test
    public void testAnErrorOccuresWhenCreatingASutWithANoneExistingRunner() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-CREATED");
        sut.setRunner(Runner.newInstance("RUNNER-NOT-FOUND"));
        sut.setProject(Project.newInstance("PROJECT-1"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.create(sut);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testAnErrorOccuresWhenCreatingASutWithANoneExistingProject() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-CREATED");
        sut.setRunner(Runner.newInstance("RUNNER-1"));
        sut.setProject(Project.newInstance("PROJECT-NOT-FOUND"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.create(sut);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testTheUnicityOfTheProjectNameAndSutNameOnCreation() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        sut.setProject(Project.newInstance("PROJECT-1"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.create(sut);
            session.getTransaction().commit();
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testWeCanUpdateASut() throws LivingDocServerException {
        SystemUnderTest newSut = SystemUnderTest.newInstance("SUT-UPDATED");
        newSut.setProject(Project.newInstance("PROJECT-1"));
        newSut.setRunner(Runner.newInstance("RUNNER-2"));
        newSut.setProjectDependencyDescriptor("PROJECT-DEPENDENCY-DESCRIPTOR-11");

        session.getTransaction().begin();
        systemUnderTestDao.update("SUT-TO-UPDATE", newSut);
        session.getTransaction().commit();

        SystemUnderTest loadedSut = getById(SystemUnderTest.class, - 1l);
        assertEquals("PROJECT-1", loadedSut.getProject().getName());
        assertEquals("RUNNER-2", loadedSut.getRunner().getName());
        assertEquals("SUT-UPDATED", loadedSut.getName());
        assertEquals("PROJECT-DEPENDENCY-DESCRIPTOR-11", loadedSut.getProjectDependencyDescriptor());
    }

    @Test
    public void testWaCantUpdateASutWithToAnExisitingSutNameForTheSameProject() {
        SystemUnderTest newSut = SystemUnderTest.newInstance("SUT-1");
        newSut.setProject(Project.newInstance("PROJECT-1"));
        newSut.setRunner(Runner.newInstance("RUNNER-2"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.update("SUT-TO-UPDATE", newSut);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testWaCantUpdateASutWithANoneExistingRunner() {
        SystemUnderTest newSut = SystemUnderTest.newInstance("SUT-UPDATED");
        newSut.setProject(Project.newInstance("PROJECT-1"));
        newSut.setRunner(Runner.newInstance("RUNNER-NOT-FOUND"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.update("SUT-TO-UPDATE", newSut);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.RUNNER_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCanRemoveASut() throws LivingDocServerException {
        session.getTransaction().begin();
        systemUnderTestDao.remove("PROJECT-1", "SUT-TO-REMOVE");
        session.getTransaction().commit();

        assertNull(getById(SystemUnderTest.class, - 2l));
    }

    @Test
    public void testWeCantRemoveANoneExistingSut() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.remove("PROJECT-1", "SUT-NOT-FOUND");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCantRemoveASutThatIsReferenced() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.remove("PROJECT-1", "SUT-1");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_REFERENCE_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testWeCantRemoveASutThatIsAssociatedWithSpecifications() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.remove("PROJECT-1", "SUT-2");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_SPECIFICATION_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testWeCantRemoveASutThatIsAssociatedWithExecutions() {
        try {
            session.getTransaction().begin();
            systemUnderTestDao.remove("PROJECT-1", "SUT-3");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_EXECUTION_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testWeCanSelectANewSystemUnderTestAsDefault() throws LivingDocServerException {
        SystemUnderTest newDefaultSut = SystemUnderTest.newInstance("SUT-TO-BE-DEFAULT");
        newDefaultSut.setProject(Project.newInstance("PROJECT-1"));

        session.getTransaction().begin();
        systemUnderTestDao.setAsDefault(newDefaultSut);
        session.getTransaction().commit();

        List<SystemUnderTest> suts = systemUnderTestDao.getAllForProject("PROJECT-1");
        for (SystemUnderTest sut : suts) {
            if (sut.getName().equals(newDefaultSut.getName())) {
                assertTrue(sut.isDefault());
            } else {
                assertFalse(sut.isDefault());
            }
        }
    }

    @Test
    public void testWeCantSelectANewSystemUnderTestAsDefaultIfProjectIsNotFoound() {
        SystemUnderTest newDefaultSut = SystemUnderTest.newInstance("SUT-TO-BE-DEFAULT");
        newDefaultSut.setProject(Project.newInstance("PROJECT-NOT-FOUND"));

        try {
            session.getTransaction().begin();
            systemUnderTestDao.setAsDefault(newDefaultSut);
            session.getTransaction().commit();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCanRetrieveAllSutsForAGivenProject() {
        assertEquals(6, systemUnderTestDao.getAllForProject("PROJECT-1").size());
    }

    @Test
    public void testWeCanRetrieveAllSutsForAGivenRunner() {
        assertEquals(6, systemUnderTestDao.getAllForRunner("RUNNER-1").size());
    }

    @Test
    public void testWeCanRetriveAllReferences() {
        Project project = Project.newInstance("PROJECT-1");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        sut.setProject(project);

        assertEquals(1, systemUnderTestDao.getAllReferences(sut).size());
    }

    @Test
    public void testWeCanRetriveAllSpecificationsForAGivenSut() {
        Project project = Project.newInstance("PROJECT-1");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        sut.setProject(project);

        assertEquals(2, systemUnderTestDao.getAllSpecifications(sut).size());
    }
}
