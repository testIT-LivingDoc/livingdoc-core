package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.dao.ProjectDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;


public class HibernateSystemUnderTestDao implements SystemUnderTestDao {
    public static final String NAME = "name";
    public static final String SUPPRESS_UNCHECKED = "unchecked";
    public static final String RUNNER_NOT_FOUND_MSG = "Runner not found";
    public static final String SP_NAME = "sp.name";
    private SessionService sessionService;
    private ProjectDao projectDao;

    public HibernateSystemUnderTestDao(SessionService sessionService, ProjectDao projectDao) {
        this.sessionService = sessionService;
        this.projectDao = projectDao;
    }

    public HibernateSystemUnderTestDao(SessionService sessionService) {
        this(sessionService, new HibernateProjectDao(sessionService));
    }

    @Override
    public Runner getRunnerByName(String name) {
        final Criteria crit = sessionService.getSession().createCriteria(Runner.class);
        crit.add(Property.forName(NAME).eq(name));
        Runner runner = ( Runner ) crit.uniqueResult();
        HibernateLazyInitializer.init(runner);
        return runner;
    }

    @Override
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public List<Runner> getAllRunners() {
        final Criteria crit = sessionService.getSession().createCriteria(Runner.class);
        crit.addOrder(Order.asc(NAME));
        List<Runner> list = crit.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    @Override
    public Runner create(Runner runner) throws LivingDocServerException {
        if (getRunnerByName(runner.getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_ALREADY_EXISTS, "Runner already exists");
        }
        sessionService.getSession().save(runner);
        return runner;
    }

    @Override
    public Runner update(String oldRunnerName, Runner runner) throws LivingDocServerException {
        if ( ! runner.getName().equals(oldRunnerName) && getRunnerByName(runner.getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_ALREADY_EXISTS, "Runner already exists");
        }

        Runner runnerToUpdate = getRunnerByName(oldRunnerName);
        if (runnerToUpdate == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_NOT_FOUND, RUNNER_NOT_FOUND_MSG);
        }

        runnerToUpdate.setName(runner.getName());
        runnerToUpdate.setServerName(runner.getServerName());
        runnerToUpdate.setServerPort(runner.getServerPort());
        runnerToUpdate.setClasspaths(runner.getClasspaths());
        runnerToUpdate.setSecured(runner.isSecured());

        sessionService.getSession().update(runnerToUpdate);
        return runnerToUpdate;
    }

    @Override
    public void removeRunner(String runnerName) throws LivingDocServerException {
        Runner runner = getRunnerByName(runnerName);

        if (runner == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_NOT_FOUND, RUNNER_NOT_FOUND_MSG);
        }

        if ( ! getAllForRunner(runnerName).isEmpty()) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_SUT_ASSOCIATED,
                "Runner is associated with suts");
        }

        sessionService.getSession().delete(runner);
    }

    @Override
    public SystemUnderTest getByName(String projectName, String sutName) {
        final Criteria crit = sessionService.getSession().createCriteria(SystemUnderTest.class);
        crit.add(Property.forName(NAME).eq(sutName));
        crit.createAlias("project", "p");
        crit.add(Restrictions.eq("p.name", projectName));
        SystemUnderTest systemUnderTest = ( SystemUnderTest ) crit.uniqueResult();
        HibernateLazyInitializer.init(systemUnderTest);
        return systemUnderTest;
    }

    @Override
    public List<SystemUnderTest> getAllForProject(String projectName) {
        final Criteria crit = sessionService.getSession().createCriteria(SystemUnderTest.class);
        crit.createAlias("project", "p");
        crit.add(Restrictions.eq("p.name", projectName));
        crit.addOrder(Order.asc(NAME));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<SystemUnderTest> systemUnderTests = crit.list();
        HibernateLazyInitializer.initCollection(systemUnderTests);
        return systemUnderTests;
    }

    @Override
    public List<SystemUnderTest> getAllForRunner(String runnerName) {
        final Criteria crit = sessionService.getSession().createCriteria(SystemUnderTest.class);
        crit.createAlias("runner", "r");
        crit.add(Restrictions.eq("r.name", runnerName));
        crit.addOrder(Order.asc(NAME));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<SystemUnderTest> systemUnderTests = crit.list();
        HibernateLazyInitializer.initCollection(systemUnderTests);
        return systemUnderTests;
    }

    @Override
    public List<Reference> getAllReferences(SystemUnderTest sut) {
        final Criteria crit = sessionService.getSession().createCriteria(Reference.class);
        crit.createAlias("systemUnderTest", "sut");
        crit.add(Restrictions.eq("sut.name", sut.getName()));
        crit.createAlias("sut.project", "sp");
        crit.add(Restrictions.eq(SP_NAME, sut.getProject().getName()));
        crit.addOrder(Order.asc(SP_NAME));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Reference> references = crit.list();
        HibernateLazyInitializer.initCollection(references);
        return references;
    }

    @Override
    public List<Specification> getAllSpecifications(SystemUnderTest sut) {
        final Criteria crit = sessionService.getSession().createCriteria(Specification.class);
        crit.createAlias("targetedSystemUnderTests", "sut");
        crit.add(Restrictions.eq("sut.name", sut.getName()));
        crit.createAlias("sut.project", "sp");
        crit.add(Restrictions.eq(SP_NAME, sut.getProject().getName()));
        crit.addOrder(Order.asc(SP_NAME));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Specification> specifications = crit.list();
        HibernateLazyInitializer.initCollection(specifications);
        return specifications;
    }

    @Override
    public SystemUnderTest create(SystemUnderTest newSut) throws LivingDocServerException {
        Runner runner = getRunnerByName(newSut.getRunner().getName());
        if (runner == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_NOT_FOUND, RUNNER_NOT_FOUND_MSG);
        }
        newSut.setRunner(runner);

        Project project = projectDao.getByName(newSut.getProject().getName());
        if (project == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_NOT_FOUND, "project not found");
        }
        project.addSystemUnderTest(newSut);

        sessionService.getSession().update(project);

        return newSut;
    }

    @Override
    public SystemUnderTest update(String oldSutName, SystemUnderTest updatedSut) throws LivingDocServerException {
        if ( ! updatedSut.getName().equals(oldSutName) && getByName(updatedSut.getProject().getName(), updatedSut
            .getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_ALREADY_EXISTS, "SUT already exists");
        }

        Runner runner = getRunnerByName(updatedSut.getRunner().getName());
        if (runner == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.RUNNER_NOT_FOUND, RUNNER_NOT_FOUND_MSG);
        }
        SystemUnderTest sutToUpdate = getByName(updatedSut.getProject().getName(), oldSutName);
        sutToUpdate.setRunner(runner);

        sutToUpdate.setName(updatedSut.getName());
        sutToUpdate.setSutClasspaths(updatedSut.getSutClasspaths());
        sutToUpdate.setFixtureClasspaths(updatedSut.getFixtureClasspaths());
        sutToUpdate.setFixtureFactory(updatedSut.getFixtureFactory());
        sutToUpdate.setFixtureFactoryArgs(updatedSut.getFixtureFactoryArgs());
        sutToUpdate.setIsDefault(updatedSut.isDefault());
        sutToUpdate.setProjectDependencyDescriptor(updatedSut.getProjectDependencyDescriptor());

        sessionService.getSession().update(sutToUpdate);
        return sutToUpdate;
    }

    @Override
    public void remove(String projectName, String sutName) throws LivingDocServerException {
        SystemUnderTest sut = getByName(projectName, sutName);

        if (sut == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, "SUT not found");
        }

        if (getAllReferences(sut).size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_REFERENCE_ASSOCIATED,
                "The SUT has associated references");
        }

        if (getAllSpecifications(sut).size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_SPECIFICATION_ASSOCIATED,
                "The SUT has associated specifications");
        }

        if (getAllExecutions(sut).size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_EXECUTION_ASSOCIATED,
                "The SUT has associated specifications");
        }

        sut.getProject().removeSystemUnderTest(sut);
        sessionService.getSession().delete(sut);
    }

    @Override
    public void setAsDefault(SystemUnderTest systemUnderTest) throws LivingDocServerException {
        Project project = projectDao.getByName(systemUnderTest.getProject().getName());
        if (project == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_NOT_FOUND, "project not found");
        }

        for (SystemUnderTest sut : project.getSystemUnderTests()) {
            sut.setIsDefault(sut.getName().equals(systemUnderTest.getName()));
        }

        sessionService.getSession().update(project);
    }

    public List<Execution> getAllExecutions(SystemUnderTest systemUnderTest) {
        final Criteria crit = sessionService.getSession().createCriteria(Execution.class);
        crit.createAlias("systemUnderTest", "sut");
        crit.add(Restrictions.eq("sut.name", systemUnderTest.getName()));
        crit.createAlias("sut.project", "sp");
        crit.add(Restrictions.eq(SP_NAME, systemUnderTest.getProject().getName()));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Execution> executions = crit.list();
        HibernateLazyInitializer.initCollection(executions);
        return executions;
    }
}
