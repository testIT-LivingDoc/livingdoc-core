package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;


public class ProjectTest {
    @Test
    public void testTheBasicBehaviorOfTheEquals() {
        assertFalse(Project.newInstance("PROJECT-1").equals(null));
        assertFalse(Project.newInstance("PROJECT-1").equals(new Integer(0)));
        assertFalse(Project.newInstance("PROJECT-1").equals(Project.newInstance("PROJECT-2")));
        assertTrue(Project.newInstance("PROJECT-1").equals(Project.newInstance("PROJECT-1")));
    }

    @Test
    public void testThatTheProjectIsAlphaComparable() {
        assertEquals(0, Project.newInstance("PROJECT-1").compareTo(Project.newInstance("PROJECT-1")));
        assertEquals( - 1, Project.newInstance("PROJECT-1").compareTo(Project.newInstance("PROJECT-2")));
        assertEquals(1, Project.newInstance("PROJECT-2").compareTo(Project.newInstance("PROJECT-1")));
    }

    @Test
    public void testThatTwoProjectWithDifferentNamesAreNotEqual() {
        Project project1 = Project.newInstance("NAME");
        Project project2 = Project.newInstance("DIFFERENT NAME");

        assertFalse(project1.equals(project2));
    }

    @Test
    public void testThatAddingARepositoryItWillBeInTheRepositoriesList() throws LivingDocServerException {
        Project project = Project.newInstance("NAME");
        Repository repository = Repository.newInstance("UID");
        project.addRepository(repository);

        assertTrue(project.getRepositories().contains(repository));
        assertEquals(project, repository.getProject());
    }

    @Test
    public void testThatAddingARepositoryThatIsAlreadyInTheRepositoriesListWillTriggerAnAxception() {
        Repository repository = Repository.newInstance("UID");
        repository.setName("TheRepo");
        Set<Repository> repositories = new HashSet<Repository>();
        repositories.add(repository);

        Project project = Project.newInstance("NAME");
        project.setRepositories(repositories);
        try {
            project.addRepository(repository);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testThatRemovingARepositoryItWillBeNoLongerInTheRepositoriesList() throws LivingDocServerException {
        Repository repository = Repository.newInstance("UID");
        Set<Repository> repositories = new HashSet<Repository>();
        repositories.add(repository);

        Project project = Project.newInstance("NAME");
        project.setRepositories(repositories);
        project.removeRepository(repository);

        assertFalse(project.getRepositories().contains(repository));
        assertNull(repository.getProject());
    }

    @Test
    public void testThatRemovingARepositoryThatIsNotInTheRepositoriesListWillTriggerAnAxception() {
        Project project = Project.newInstance("NAME");
        Repository repository = Repository.newInstance("UID");

        try {
            project.removeRepository(repository);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testThatWeCanNotAddANewRepositoryWithTheSameNameAsOneAlreadyPresent() throws Exception {
        Project project = Project.newInstance("NAME");
        Repository repo1 = Repository.newInstance("UID1");
        repo1.setName("REPO-1");

        Repository repo2 = Repository.newInstance("UID2");
        repo2.setName("repo-1");

        project.addRepository(repo1);

        try {
            project.addRepository(repo2);
            fail("Should not be able to add a second Repo with same name");
        } catch (LivingDocServerException ex) {
            assertEquals(LivingDocServerErrorKey.REPOSITORY_ALREADY_EXISTS, ex.getId());
        }
    }

    @Test
    public void testThatAddingASUTItWillBeInTheSUTsList() throws LivingDocServerException {
        Project project = Project.newInstance("NAME");
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");
        project.addSystemUnderTest(sut);

        assertTrue(project.getSystemUnderTests().contains(sut));
    }

    @Test
    public void testThatAddingASUTThatIsAlreadyInTheSUTsListWillTriggerAnAxception() {
        Project project = Project.newInstance("NAME");
        Set<SystemUnderTest> suts = new HashSet<SystemUnderTest>();

        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");
        sut.setProject(project);
        suts.add(sut);
        project.setSystemUnderTests(suts);

        try {
            project.addSystemUnderTest(sut);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_ALREADY_EXISTS, e.getId());
        }
    }

    @Test
    public void testThatRemovingASUTItWillBeNoLongerInTheSUTsList() throws LivingDocServerException {
        Project project = Project.newInstance("NAME");
        Set<SystemUnderTest> suts = new HashSet<SystemUnderTest>();

        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");
        sut.setProject(project);
        suts.add(sut);
        project.setSystemUnderTests(suts);

        project.removeSystemUnderTest(sut);
        assertFalse(project.getSystemUnderTests().contains(sut));
        assertNull(sut.getProject());
    }

    @Test
    public void testThatRemovingASUTThatIsNotInTheSUTsListWillTriggerAnAxception() {
        Project project = Project.newInstance("NAME");
        Set<SystemUnderTest> suts = new HashSet<SystemUnderTest>();
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");
        sut.setProject(project);
        suts.add(sut);

        try {
            project.removeSystemUnderTest(sut);
            fail();
        } catch (LivingDocServerException e) {
            assertEquals(LivingDocServerErrorKey.SUT_NOT_FOUND, e.getId());
        }
    }

    @Test
    public void testThatTheWeCanRetrieveTheDefaultSUT() {
        Project project = Project.newInstance("NAME");
        Set<SystemUnderTest> suts = new HashSet<SystemUnderTest>();
        SystemUnderTest someSut = SystemUnderTest.newInstance("SUT");
        someSut.setProject(project);
        SystemUnderTest defaultSut = SystemUnderTest.newInstance("SUT-DEFAULT");
        defaultSut.setProject(project);
        defaultSut.setIsDefault(true);

        suts.add(someSut);
        suts.add(defaultSut);
        project.setSystemUnderTests(suts);

        assertEquals(defaultSut, project.getDefaultSystemUnderTest());
    }

    @Test
    public void testThatWeCanNotAddAnewSUTWithTheSameNameAsOneAlreadyPresent() throws Exception {
        Project project = Project.newInstance("NAME");
        project.addSystemUnderTest(SystemUnderTest.newInstance("SUT-1"));
        project.addSystemUnderTest(SystemUnderTest.newInstance("SUT-2"));

        try {
            project.addSystemUnderTest(SystemUnderTest.newInstance("sut-1"));
            fail("Should not be able to add a second SUT with same name");
        } catch (LivingDocServerException ex) {
            assertEquals(LivingDocServerErrorKey.SUT_ALREADY_EXISTS, ex.getId());
        }
    }

}
