package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.hibernate.hsqldb.AbstractDBUnitHibernateMemoryTest;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;


public class HibernateRepositoryDaoTest extends AbstractDBUnitHibernateMemoryTest {
    private static final String DATAS = "/dbunit/datas/HibernateRepositoryDaoTest.xml";
    private RepositoryDao repoDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        insertIntoDatabase(DATAS);
        repoDao = new HibernateRepositoryDao(this);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testRepositoryAccessibility() {
        /* We Can Retrieve A Repository By Its UID */
        assertNotNull(repoDao.getByUID("UID-1"));

        /* We Can Retrieve A Repository For A Given Name And Project Name */
        assertNotNull(repoDao.getByName("PROJECT-1", "REPOSITORY-1"));

        /* We Can Retrieve All Available Repositories */
        assertEquals(5, repoDao.getAll().size());

        /* We Can Retrieve All Available Repositories For A Given Project */
        assertEquals(4, repoDao.getAll("PROJECT-1").size());

        /* We Can Retrieve All Available Test Repositories For A Given Project */
        assertEquals(2, repoDao.getAllTestRepositories("PROJECT-1").size());

        /* We Can Retrieve All Available Requirement Repositories For A Given
         * Project */
        assertEquals(3, repoDao.getAllRequirementRepositories("PROJECT-1").size());

        /* We Can Retrieve All Available Repositories For A Given Content Type */
        assertEquals(2, repoDao.getAllRepositories(ContentType.TEST).size());
        assertEquals(4, repoDao.getAllRepositories(ContentType.REQUIREMENT).size());
        assertEquals(1, repoDao.getAllRepositories(ContentType.BOTH).size());

        /* We Can Retrieve A Repository Type By Its Name */
        assertNotNull(repoDao.getTypeByName("TYPE-1"));

        /* We Can Retrieve All Available Repository Types */
        assertEquals(2, repoDao.getAllTypes().size());
    }

    @Test
    public void testTheCRUDOfTheRepository() throws LivingDocServerException {
        /* We Can Create A New Repository */
        Repository repo = Repository.newInstance("UID-CREATED");
        repo.setProject(Project.newInstance("PROJECT-1"));
        repo.setBaseRepositoryUrl("BASE-REPO-URL");
        repo.setBaseTestUrl("TEST-URL");
        repo.setBaseUrl("BASE-URL");
        repo.setType(RepositoryType.newInstance("TYPE-1"));
        repo.setContentType(ContentType.TEST);
        repo.setName("REPO-CREATED");

        session.getTransaction().begin();
        repo = repoDao.create(repo);
        session.getTransaction().commit();

        assertNotNull(getById(Repository.class, repo.getId()));
        assertEquals("REPO-CREATED", repo.getName());
        assertEquals("UID-CREATED", repo.getUid());
        assertEquals("BASE-REPO-URL", repo.getBaseRepositoryUrl());
        assertEquals("TEST-URL", repo.getBaseTestUrl());
        assertEquals("BASE-URL", repo.getBaseUrl());
        assertEquals(ContentType.TEST, repo.getContentType());
        assertEquals(RepositoryType.newInstance("TYPE-1"), repo.getType());

        /* We Cant Create A New Repository If Project Not Found */
        repo = Repository.newInstance("UID-CREATED");
        repo.setProject(Project.newInstance("PROJECT-NOT-FOUND"));
        repo.setType(RepositoryType.newInstance("TYPE-1"));

        try {
            session.getTransaction().begin();
            repo = repoDao.create(repo);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_NOT_FOUND, e.getId());
        }

        /* We Cant Create A New Repository If The Type Is Not Found */
        try {
            session.getTransaction().begin();
            repo = Repository.newInstance("UID-CREATED");
            repo.setProject(Project.newInstance("PROJECT-1"));
            repo.setType(RepositoryType.newInstance("TYPE-NOT-FOUND"));

            repo = repoDao.create(repo);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_TYPE_NOT_FOUND, e.getId());
        }

        /* We Can Create A New Repository Type */
        RepositoryType type = RepositoryType.newInstance("TYPE-CREATED");
        type.setClassName("REPO-CLASS");
        type.setDocumentUrlFormat("DOC-FORMAT");
        type.setTestUrlFormat("TEST-FORMAT");

        session.getTransaction().begin();
        type = repoDao.create(type);
        session.getTransaction().commit();

        assertNotNull(getById(RepositoryType.class, type.getId()));
        assertEquals("TYPE-CREATED", type.getName());
        assertEquals("REPO-CLASS", type.getClassName());
        assertEquals("DOC-FORMAT", type.getDocumentUrlFormat());
        assertEquals("TEST-FORMAT", type.getTestUrlFormat());

        /* We Can Update A Repository */
        Repository repository = Repository.newInstance("UID-TO-UPDATE-2");
        repository.setProject(Project.newInstance("PROJECT-2"));
        repository.setBaseRepositoryUrl("BASE-REPO-URL-2");
        repository.setBaseTestUrl("TEST-URL-2");
        repository.setBaseUrl("BASE-URL-2");
        repository.setContentType(ContentType.TEST);
        repository.setName("REPO-UPDATED");
        repository.setUsername("LD");
        repository.setPassword("LD");

        session.getTransaction().begin();
        repoDao.update(repository);
        session.getTransaction().commit();

        Repository loadedRepo = getById(Repository.class, - 2l);
        assertNotNull(loadedRepo);
        assertEquals("REPO-UPDATED", loadedRepo.getName());
        assertEquals("BASE-REPO-URL-2", loadedRepo.getBaseRepositoryUrl());
        assertEquals("TEST-URL-2", loadedRepo.getBaseTestUrl());
        assertEquals("BASE-URL-2", loadedRepo.getBaseUrl());
        assertEquals("LD", loadedRepo.getUsername());
        assertEquals("LD", loadedRepo.getPassword());
        assertEquals(ContentType.TEST, loadedRepo.getContentType());
        assertEquals("PROJECT-2", loadedRepo.getProject().getName());

        /* We Cant Update A None Existing Repository */
        try {
            session.getTransaction().begin();
            repoDao.update(Repository.newInstance("UID-NOT-FOUND"));
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, e.getId());
        }

        /* We Cant Update A None Existing Project */
        repository = Repository.newInstance("UID-2");
        repository.setProject(Project.newInstance("PROJECT-NOT-FOUND"));
        repository.setBaseRepositoryUrl("BASE-REPO-URL-2");
        repository.setBaseTestUrl("TEST-URL-2");
        repository.setBaseUrl("BASE-URL-2");
        repository.setContentType(ContentType.TEST);
        repository.setName("REPO-UPDATED");

        try {
            session.getTransaction().begin();
            repoDao.update(repository);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.PROJECT_NOT_FOUND, e.getId());
        }

        /* We Cant Update An Associated Repository With Requirements Or
         * Specifications To Another Project */
        repository = Repository.newInstance("UID-1");
        repository.setProject(Project.newInstance("PROJECT-2"));
        repository.setBaseRepositoryUrl("BASE-REPO-URL-2");
        repository.setBaseTestUrl("TEST-URL-2");
        repository.setBaseUrl("BASE-URL-2");
        repository.setContentType(ContentType.TEST);
        repository.setName("REPO-UPDATED");

        try {
            session.getTransaction().begin();
            repoDao.update(repository);
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_DOC_ASSOCIATED, e.getId());
        }

        /* We Cant Remove A Repository If Docs Are Associated */
        try {
            session.getTransaction().begin();
            repoDao.remove("UID-1");
            session.getTransaction().commit();
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_DOC_ASSOCIATED, e.getId());
        }
    }

    @Test
    public void testWhenUpdatingARepositoryProjectsWithNoMoreRepositoriesAreRemove() throws LivingDocServerException {
        Repository repository = Repository.newInstance("UID-TO-UPDATE");
        repository.setProject(Project.newInstance("PROJECT-1"));
        repository.setBaseRepositoryUrl("BASE-REPO-URL-2");
        repository.setBaseTestUrl("TEST-URL-2");
        repository.setBaseUrl("BASE-URL-2");
        repository.setContentType(ContentType.TEST);
        repository.setName("REPO-UPDATED");

        session.getTransaction().begin();
        repoDao.update(repository);
        session.getTransaction().commit();

        assertNull(getById(Project.class, 3l));
    }

    @Test
    public void testWeCanRemoveAnEmptyRepositor() throws LivingDocServerException {
        session.getTransaction().begin();
        repoDao.remove("UID-TO-REMOVE");
        session.getTransaction().commit();

        assertNull(getById(Repository.class, 3l));
    }
}
