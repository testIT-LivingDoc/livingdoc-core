package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.dao.ProjectDao;


public class HibernateProjectDaoTest extends AbstractDBUnitHibernateMemoryTest {
    private static final String DATAS = "/dbunit/datas/HibernateProjectDaoTest.xml";
    private ProjectDao projectDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        insertIntoDatabase(DATAS);
        projectDao = new HibernateProjectDao(this);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        deleteFromDatabase(DATAS);
        super.tearDown();
    }

    @Test
    public void testThatAProjectCanBeSelectedbyName() {
        Project projectByName = projectDao.getByName("PROJECT-1");
        assertNotNull(projectByName);
    }

    @Test
    public void testThatSelectByNameThrowsAnExceptionIfTheProjectIsNotPresisted() {
        assertNull(projectDao.getByName("PROJECT-NOT-FOUND"));
    }

    @Test
    public void testThatAProjectCanBeCreated() throws LivingDocServerException {
        session.getTransaction().begin();
        Project projectCreated = projectDao.create("PROJECT-CREATED");
        session.getTransaction().commit();

        assertNotNull(getById(Project.class, projectCreated.getId()));
        assertEquals("PROJECT-CREATED", projectCreated.getName());
    }

    @Test
    public void testThatTheUnicityOfTheProjectNameOnCreation() {
        try {
            projectDao.create("PROJECT-1");
            fail();
        } catch (LivingDocServerException ex) {
            assertEquals(LivingDocServerErrorKey.PROJECT_ALREADY_EXISTS, ex.getId());
        }
    }

    @Test
    public void testThatTheRepositoryAreWellAssociated() {
        Project project = projectDao.getByName("PROJECT-1");
        assertEquals(1, project.getRepositories().size());
    }

    @Test
    public void testThatWeCanRetrieveAllProjects() {
        List<Project> projects = projectDao.getAll();
        assertEquals(6, projects.size());
    }

    @Test
    public void testThatWeCanRemoveAProjectThatIsNotAssociatedToEntities() throws LivingDocServerException {
        int numberOfProjects = projectDao.getAll().size();
        Transaction startedTransaction = session.beginTransaction();
        projectDao.remove("PROJECT-TO-REMOVE-NOASSOCIATIONS");
        startedTransaction.commit();
        assertEquals(numberOfProjects - 1, projectDao.getAll().size());
    }

    @Test
    public void testThatWeCannotRemoveAProjectAssociatedToARepository() {
        Project projectToRemove = projectDao.getByName("PROJECT-TO-REMOVE-5");
        assertEquals(1, projectToRemove.getRepositories().size());

        try {
            projectDao.remove(projectToRemove.getName());
            fail();
        } catch (LivingDocServerException ex) {
            assertEquals(LivingDocServerErrorKey.PROJECT_REPOSITORY_ASSOCIATED, ex.getId());
        }
    }

    @Test
    public void testThatWeCannotRemoveAProjectAssociatedToSUTS() {
        Project projectToRemove = projectDao.getByName("PROJECT-TO-REMOVE-6");
        assertEquals(1, projectToRemove.getSystemUnderTests().size());

        try {
            projectDao.remove(projectToRemove.getName());
            fail();
        } catch (LivingDocServerException ex) {
            assertEquals(LivingDocServerErrorKey.PROJECT_SUTS_ASSOCIATED, ex.getId());
        }
    }

    @Test
    public void testWeCantUpdateANotFoundProject() {
        try {
            session.getTransaction().begin();
            projectDao.update("PROJECT-NOT-FOUND", Project.newInstance("TO-SOME-PROJECT"));
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testWeCantUpdateAProjectToAnAlreadyUsedName() {
        try {
            session.getTransaction().begin();
            projectDao.update("PROJECT-1", Project.newInstance("PROJECT-2"));
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testWeCanUpdateAProject() throws LivingDocServerException {

        session.getTransaction().begin();
        Project projectUpdated = projectDao.update("PROJECT-1", Project.newInstance("PROJECT-1-UPDATED"));
        session.getTransaction().commit();

        assertNotNull(getById(Project.class, projectUpdated.getId()));
        assertEquals("PROJECT-1-UPDATED", projectUpdated.getName());
    }
}
