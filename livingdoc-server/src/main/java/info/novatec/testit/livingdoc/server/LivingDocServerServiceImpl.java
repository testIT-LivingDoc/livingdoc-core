/* Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */
package info.novatec.testit.livingdoc.server;

import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.ERROR;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.EXECUTION_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.PROJECT_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.PROJECT_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.PROJECT_UPDATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REFERENCE_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REFERENCE_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REFERENCE_UPDATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REPOSITORY_NOT_FOUND;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REPOSITORY_REGISTRATION_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REPOSITORY_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REPOSITORY_UPDATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.REQUIREMENT_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_EXECUTIONS;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_REFERENCE;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_REFERENCES;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_REQUIREMENT_REPOS;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_SPECIFICATION_REPOS;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RETRIEVE_SUTS;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RUNNER_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RUNNER_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RUNNER_UPDATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.RUN_REFERENCE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATIONS_NOT_FOUND;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_ADD_SUT_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_REMOVE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_REMOVE_SUT_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_RUN_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SPECIFICATION_UPDATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SUT_CREATE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SUT_DELETE_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SUT_SET_DEFAULT_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.SUT_UPDATE_FAILED;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.repository.DocumentRepository;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.DocumentNode;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.ReferenceNode;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.RequirementSummary;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.server.domain.dao.DocumentDao;
import info.novatec.testit.livingdoc.server.domain.dao.ProjectDao;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import info.novatec.testit.livingdoc.server.transfer.SpecificationLocation;


public class LivingDocServerServiceImpl implements LivingDocServerService {

    private static final Logger log = LoggerFactory.getLogger(LivingDocServerServiceImpl.class);
    public static final String NUMBER = " number: ";

    private SessionService sessionService;
    private ProjectDao projectDao;
    private RepositoryDao repositoryDao;
    private SystemUnderTestDao sutDao;
    private DocumentDao documentDao;

    public LivingDocServerServiceImpl(SessionService sessionService, ProjectDao projectDao, RepositoryDao repositoryDao,
        SystemUnderTestDao sutDao, DocumentDao documentDao) {

        this.sessionService = sessionService;
        this.projectDao = projectDao;
        this.repositoryDao = repositoryDao;
        this.sutDao = sutDao;
        this.documentDao = documentDao;
    }

    public LivingDocServerServiceImpl() {

    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void setRepositoryDao(RepositoryDao repositoryDao) {
        this.repositoryDao = repositoryDao;
    }

    public void setSutDao(SystemUnderTestDao sutDao) {
        this.sutDao = sutDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Runner getRunner(String name) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Runner runner = sutDao.getRunnerByName(name);

            log.debug("Retrieved Runner name: " + name);

            return runner;
        } catch (Exception ex) {
            throw handleException(ERROR, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<Runner> getAllRunners() throws LivingDocServerException {
        try {
            sessionService.startSession();

            List<Runner> runners = sutDao.getAllRunners();

            log.debug("Retrieved All Runner number: " + runners.size());

            return runners;
        } catch (Exception ex) {
            throw handleException(ERROR, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public void createRunner(Runner runner) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.create(runner);

            sessionService.commitTransaction();
            log.debug("Created Runner: " + runner.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(RUNNER_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public void updateRunner(String oldRunnerName, Runner runner) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.update(oldRunnerName, runner);

            sessionService.commitTransaction();
            log.debug("Updated Runner: " + oldRunnerName);
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(RUNNER_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public void removeRunner(String name) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.removeRunner(name);

            sessionService.commitTransaction();
            log.debug("Removed Runner: " + name);
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(RUNNER_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Repository getRepository(String uid, Integer maxUsers) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository repository = loadRepository(uid);

            if (maxUsers != null) {
                repository.setMaxUsers(maxUsers);
            }

            return repository;
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Repository getRegisteredRepository(Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository registeredRepository = loadRepository(repository.getUid());

            registeredRepository.setMaxUsers(repository.getMaxUsers());

            return registeredRepository;
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Repository registerRepository(Repository repository) throws LivingDocServerException {
        try {
            Repository repo = repository;
            sessionService.startSession();
            sessionService.beginTransaction();

            Project project = projectDao.getByName(repo.getProject().getName());

            if (project == null) {
                projectDao.create(repo.getProject().getName());
            }

            repo = repositoryDao.create(repo);

            sessionService.commitTransaction();

            log.debug("Registered Repository: " + repo.getUid());

            return repo;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REPOSITORY_REGISTRATION_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void updateRepositoryRegistration(Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            Project project = projectDao.getByName(repository.getProject().getName());

            if (project == null) {
                projectDao.create(repository.getProject().getName());
            }

            repositoryDao.update(repository);

            sessionService.commitTransaction();
            log.debug("Updated Repository: " + repository.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REPOSITORY_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public void removeRepository(String repositoryUid) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            repositoryDao.remove(repositoryUid);

            sessionService.commitTransaction();
            log.debug("Removed Repository: " + repositoryUid);
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REPOSITORY_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<Repository> getRepositoriesOfAssociatedProject(String projectName) throws LivingDocServerException {

        try {
            sessionService.startSession();

            return repositoryDao.getAll(projectName);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Project getProject(String name) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Project project = projectDao.getByName(name);

            log.debug("Retrieved Project name: " + name);
            return project;
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<Project> getAllProjects() {
        try {
            sessionService.startSession();

            List<Project> projects = projectDao.getAll();

            log.debug("Retrieved All Projects number: " + projects.size());
            return projects;
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Project createProject(Project project) throws LivingDocServerException {

        Project newProject;

        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            newProject = projectDao.create(project.getName());

            sessionService.commitTransaction();
            log.debug("Created Project: " + project.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(PROJECT_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }

        return newProject;
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Project updateProject(String oldProjectName, Project project) throws LivingDocServerException {

        Project projectUpdated;
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            projectUpdated = projectDao.update(oldProjectName, project);

            sessionService.commitTransaction();
            log.debug("Updated Project: " + project.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(PROJECT_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }

        return projectUpdated;
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<Repository> getAllSpecificationRepositories() throws LivingDocServerException {
        try {
            sessionService.startSession();

            List<Repository> repositories = repositoryDao.getAllRepositories(ContentType.TEST);

            log.debug("Retrieved All Specification Repositories number: " + repositories.size());
            return repositories;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SUTS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<Repository> getSpecificationRepositoriesOfAssociatedProject(String repositoryUid)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository repository = loadRepository(repositoryUid);

            List<Repository> repositories = repositoryDao.getAllTestRepositories(repository.getProject().getName());

            log.debug("Retrieved Test Repositories Of Associated Project of " + repository.getUid() + NUMBER + repositories
                .size());
            return repositories;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SPECIFICATION_REPOS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * CANT SECURE
     */
    @Override
    public List<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest sut) throws LivingDocServerException {
        try {
            sessionService.startSession();

            List<Repository> repositories = repositoryDao.getAllTestRepositories(sut.getProject().getName());

            log.debug("Retrieved Test Repositories Of Associated Project of " + sut.getName() + NUMBER + repositories
                .size());
            return repositories;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SPECIFICATION_REPOS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * CANT SECURE
     */
    @Override
    public List<Repository> getSpecificationRepositoriesForSystemUnderTest(SystemUnderTest sut)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            List<Repository> repositories = repositoryDao.getAllTestRepositories(sut.getProject().getName());

            log.debug("Retrieved Test Repositories Of Associated Project of " + sut.getName() + NUMBER + repositories
                .size());
            return repositories;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SPECIFICATION_REPOS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<Repository> getRequirementRepositoriesOfAssociatedProject(String repositoryUid)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository repository = loadRepository(repositoryUid);

            List<Repository> repositories = repositoryDao.getAllRequirementRepositories(repository.getProject().getName());

            log.debug("Retrieved Requirement Repositories Of Associated Project of " + repository.getUid() + NUMBER
                + repositories.size());
            return repositories;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_REQUIREMENT_REPOS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(String repositoryUid)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository repository = loadRepository(repositoryUid);

            List<SystemUnderTest> suts = sutDao.getAllForProject(repository.getProject().getName());

            log.debug("Retrieved SUTs Of Associated Project of " + repository.getUid() + NUMBER + suts.size());
            return suts;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SUTS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<SystemUnderTest> getSystemUnderTestsOfProject(String projectName) throws LivingDocServerException {
        try {
            sessionService.startSession();
            List<SystemUnderTest> suts = sutDao.getAllForProject(projectName);

            log.debug("Retrieved SUTs of Project: " + projectName + NUMBER + suts.size());
            return suts;
        } catch (NullPointerException ex) {
            throw new LivingDocServerException(RETRIEVE_SUTS, ex);
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SUTS, ex);
        } finally {
            if (sessionService != null && sessionService.getSession() != null) {
                sessionService.closeSession();
            }
        }
    }

    /**
     * SECURED
     */
    @Override
    public void addSpecificationSystemUnderTest(SystemUnderTest sut, Specification specification)
        throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(specification.getRepository().getUid());

            documentDao.addSystemUnderTest(sut, specification);

            sessionService.commitTransaction();
            log.debug("Added SUT " + sut.getName() + " to SUT list of specification: " + specification.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_ADD_SUT_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void removeSpecificationSystemUnderTest(SystemUnderTest sut, Specification specification)
        throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(specification.getRepository().getUid());

            documentDao.removeSystemUnderTest(sut, specification);

            sessionService.commitTransaction();
            log.debug("Removed SUT " + sut.getName() + " to SUT list of specification: " + specification.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_REMOVE_SUT_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public boolean doesSpecificationHasReferences(Specification specification) throws LivingDocServerException {
        try {
            sessionService.startSession();

            boolean hasReferences = ! documentDao.getAllReferences(specification).isEmpty();

            log.debug("Does Specification " + specification.getName() + "  Has References: " + hasReferences);
            return hasReferences;
        } catch (Exception ex) {
            throw handleException(ERROR, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<Reference> getSpecificationReferences(Specification specification) throws LivingDocServerException {
        try {
            sessionService.startSession();

            loadRepository(specification.getRepository().getUid());

            List<Reference> references = documentDao.getAllReferences(specification);

            log.debug("Retrieved Specification " + specification.getName() + " Test Cases number: " + references.size());
            return references;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_REFERENCES, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            /* Repository repository =
             * loadRepository(specification.getRepository().getUid());
             * verifyRepositoryPermission(repository, Permission.READ); */

            return documentDao.getSpecificationExecutions(specification, sut, maxResults);
        } catch (Exception ex) {
            throw handleException(RETRIEVE_EXECUTIONS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Execution getSpecificationExecution(Long id) throws LivingDocServerException {
        try {
            sessionService.startSession();

            return documentDao.getSpecificationExecution(id);
        } catch (Exception ex) {
            throw handleException(RETRIEVE_EXECUTIONS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public boolean doesRequirementHasReferences(Requirement requirement) throws LivingDocServerException {
        try {
            sessionService.startSession();
            boolean hasReferences = ! documentDao.getAllReferences(requirement).isEmpty();

            log.debug("Does Requirement " + requirement.getName() + " Document Has References: " + hasReferences);
            return hasReferences;
        } catch (Exception ex) {
            throw handleException(ERROR, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<Reference> getRequirementReferences(Requirement requirement) throws LivingDocServerException {
        try {
            sessionService.startSession();

            loadRepository(requirement.getRepository().getUid());

            List<Reference> references = documentDao.getAllReferences(requirement);

            log.debug("Retrieved Requirement " + requirement.getName() + " Document References number: " + references
                .size());
            return references;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_REFERENCES, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public RequirementSummary getRequirementSummary(Requirement requirement) throws LivingDocServerException {
        try {
            Requirement req = requirement;
            sessionService.startSession();

            Repository repository = loadRepository(req.getRepository().getUid());

            req = documentDao.getRequirementByName(repository.getUid(), req.getName());

            log.debug("Retrieved Requirement " + req.getName() + " Summary");
            return req.getSummary();
        } catch (Exception ex) {
            throw handleException(ERROR, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Reference getReference(Reference reference) throws LivingDocServerException {
        try {
            Reference ref = reference;
            sessionService.startSession();

            loadRepository(ref.getSpecification().getRepository().getUid());

            ref = documentDao.get(ref);
            if (ref == null) {
                return null;
            }

            log.debug("Retrieved Reference: " + ref.getRequirement().getName() + "," + ref.getSpecification().getName());
            return ref;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_REFERENCE, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public SystemUnderTest getSystemUnderTest(SystemUnderTest sut, Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();

            SystemUnderTest sutDb = sutDao.getByName(sut.getProject().getName(), sut.getName());

            sessionService.commitTransaction();
            log.debug("Retrieved SystemUnderTest: " + sut.getName());
            return sutDb;
        } catch (Exception ex) {
            throw handleException(RETRIEVE_SUTS, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void createSystemUnderTest(SystemUnderTest sut, Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            SystemUnderTest newSut = sutDao.create(sut);

            sessionService.commitTransaction();
            log.debug("Updated SystemUnderTest: " + newSut.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SUT_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest sut, Repository repository)
        throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.update(oldSystemUnderTestName, sut);

            sessionService.commitTransaction();
            log.debug("Updated SystemUnderTest: " + oldSystemUnderTestName);
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SUT_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void removeSystemUnderTest(SystemUnderTest sut, Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.remove(sut.getProject().getName(), sut.getName());

            sessionService.commitTransaction();
            log.debug("Removed SystemUnderTest: " + sut.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SUT_DELETE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void setSystemUnderTestAsDefault(SystemUnderTest sut, Repository repository) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            sutDao.setAsDefault(sut);

            sessionService.commitTransaction();
            log.debug("Setted as default SystemUnderTest: " + sut.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SUT_SET_DEFAULT_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NOT SECURED FOR SYNCHRONIZATION PURPOSES
     */
    @Override
    public void removeRequirement(Requirement requirement) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            documentDao.removeRequirement(requirement);

            sessionService.commitTransaction();
            log.debug("Removed Requirement: " + requirement.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REQUIREMENT_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Specification getSpecification(Specification specification) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Specification specificationFound = documentDao.getSpecificationByName(specification.getRepository().getUid(),
                specification.getName());

            if (specificationFound != null) {
                log.debug("Specification found : " + specificationFound.getName());
            }

            return specificationFound;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_NOT_FOUND, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public Specification getSpecificationById(Long id) throws LivingDocServerException {
        try {
            sessionService.startSession();

            Specification specificationFound = documentDao.getSpecificationById(id);

            if (specificationFound != null) {
                log.debug("Specification found : " + specificationFound.getName());
            }

            return specificationFound;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_NOT_FOUND, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<Specification> getSpecifications(SystemUnderTest systemUnderTest, Repository repository)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            List<Specification> specifications = documentDao.getSpecifications(systemUnderTest, repository);

            log.debug("Retrieved specifications for sut: " + systemUnderTest.getName() + " and repoUID:" + repository
                .getUid());
            return specifications;
        } catch (Exception ex) {
            throw handleException(SPECIFICATIONS_NOT_FOUND, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public List<SpecificationLocation> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName)
        throws LivingDocServerException {
        try {
            sessionService.startSession();

            Repository repository = loadRepository(repositoryUID);

            List<SpecificationLocation> locations = new ArrayList<SpecificationLocation>();
            SystemUnderTest systemUnderTest = sutDao.getByName(repository.getProject().getName(), systemUnderTestName);
            List<Specification> specifications = documentDao.getSpecifications(systemUnderTest, repository);

            for (Specification specification : specifications) {
                SpecificationLocation specificationLocation = new SpecificationLocation();
                specificationLocation.setRepositoryTypeClassName(specification.getRepository().getType().getClassName());
                specificationLocation.setBaseTestUrl(specification.getRepository().getBaseTestUrl());
                specificationLocation.setUsername(StringUtils.stripToEmpty(specification.getRepository().getUsername()));
                specificationLocation.setPassword(StringUtils.stripToEmpty(specification.getRepository().getPassword()));
                specificationLocation.setSpecificationName(specification.getName());
                locations.add(specificationLocation);
            }
            log.debug("Retrieved specification list: " + repository.getName());
            return locations;
        } catch (Exception ex) {
            throw handleException(SPECIFICATIONS_NOT_FOUND, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest)
        throws LivingDocServerException {
        try {
            Repository repo = repository;
            sessionService.startSession();

            String user = repo.getUsername();
            String pwd = repo.getPassword();

            repo = loadRepository(repo.getUid());

            ClassLoader currentClassLoader = this.getClass().getClassLoader();

            SystemUnderTest systemUnderTestDb = sutDao.getByName(repo.getProject().getName(), systemUnderTest.getName());
            DocumentRepository docRepo = repo.asDocumentRepository(currentClassLoader, user, pwd);

            log.debug("Retrieved specification Hierarchy: " + repo.getName());
            DocumentNode hierarchy = XmlRpcDataMarshaller.toDocumentNode(new Vector<Object>(docRepo
                .listDocumentsInHierarchy()));
            setExecutionEnable(hierarchy, repo.getUid(), systemUnderTestDb);
            return hierarchy;
        } catch (Exception ex) {
            throw handleException(SPECIFICATIONS_NOT_FOUND, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Specification createSpecification(Specification specification) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(specification.getRepository().getUid());

            Specification specificationDb = documentDao.createSpecification(null, specification.getRepository().getUid(),
                specification.getName());

            sessionService.commitTransaction();
            log.debug("Created Specification: " + specification.getName());
            return specificationDb;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NOT SECURED FOR SYNCHRONIZATION PURPOSES
     */
    @Override
    public void updateSpecification(Specification oldSpecification, Specification newSpecification)
        throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            documentDao.updateSpecification(oldSpecification, newSpecification);

            sessionService.commitTransaction();
            log.debug("Updated Specification: " + oldSpecification.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NOT SECURED FOR SYNCHRONIZATION PURPOSES
     */
    @Override
    public void removeSpecification(Specification specification) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            documentDao.removeSpecification(specification);

            sessionService.commitTransaction();
            log.debug("Removed specification: " + specification.getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void createReference(Reference reference) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(reference.getSpecification().getRepository().getUid());

            documentDao.createReference(reference);

            sessionService.commitTransaction();
            log.debug("Created Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification()
                .getName());
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REFERENCE_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Reference updateReference(Reference oldReference, Reference newReference) throws LivingDocServerException {
        try {
            Reference newRef = newReference;
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(oldReference.getSpecification().getRepository().getUid());

            newRef = documentDao.updateReference(oldReference, newRef);

            log.debug("Updated Reference: " + newRef.getRequirement().getName() + "," + newRef.getSpecification().getName());
            sessionService.commitTransaction();

            return newRef;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REFERENCE_UPDATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public void removeReference(Reference reference) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(reference.getSpecification().getRepository().getUid());

            documentDao.removeReference(reference);

            log.debug("Removed Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification()
                .getName());
            sessionService.commitTransaction();
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(REFERENCE_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Execution createExecution(SystemUnderTest systemUnderTest, Specification specification, XmlReport xmlReport)
        throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(specification.getRepository().getUid());

            Execution execution = Execution.newInstance(specification, systemUnderTest, xmlReport);

            documentDao.createExecution(execution);

            sessionService.commitTransaction();

            return execution;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(EXECUTION_CREATE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification,
        boolean implementedVersion, String locale) throws LivingDocServerException {
        try {
            log.debug("Running Specification: " + specification.getName() + " ON System: " + systemUnderTest.getName());

            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(specification.getRepository().getUid());

            Execution exe = documentDao.runSpecification(systemUnderTest, specification, implementedVersion, locale);
            log.debug("Sucessfully runned Specification: " + specification.getName() + " ON System: " + systemUnderTest
                .getName());
            sessionService.commitTransaction();

            return exe;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(SPECIFICATION_RUN_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * SECURED
     */
    @Override
    public Reference runReference(Reference reference, String locale) throws LivingDocServerException {
        try {
            Reference ref = reference;
            sessionService.startSession();
            sessionService.beginTransaction();

            loadRepository(ref.getSpecification().getRepository().getUid());

            ref = documentDao.runReference(ref, locale);

            log.debug("Runned Reference: " + ref.getRequirement().getName() + "," + ref.getSpecification().getName()
                + " ON System: " + ref.getSystemUnderTest().getName());
            sessionService.commitTransaction();

            return ref;
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(RUN_REFERENCE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    /**
     * NO NEEDS TO SECURE THIS
     */
    @Override
    public void removeProject(Project project, boolean cascade) throws LivingDocServerException {
        try {
            sessionService.startSession();
            sessionService.beginTransaction();

            if (cascade) {
                List<Repository> repositories = repositoryDao.getAll(project.getName());

                for (Repository repo : repositories) {
                    repositoryDao.remove(repo.getUid());
                }

                List<SystemUnderTest> systemUnderTests = sutDao.getAllForProject(project.getName());

                for (SystemUnderTest sut : systemUnderTests) {
                    sutDao.remove(sut.getProject().getName(), sut.getName());
                }
            }

            projectDao.remove(project.getName());

            log.debug("Removed Project: " + project.getName());

            sessionService.commitTransaction();
        } catch (Exception ex) {
            sessionService.rollbackTransaction();
            throw handleException(PROJECT_REMOVE_FAILED, ex);
        } finally {
            sessionService.closeSession();
        }
    }

    private LivingDocServerException handleException(String id, Exception ex) {
        log.error(id, ex);

        if (ex instanceof LivingDocServerException) {
            return ( LivingDocServerException ) ex;
        }
        return new LivingDocServerException(id, ex);
    }

    private Repository loadRepository(String uid) throws LivingDocServerException {
        Repository repoDb = repositoryDao.getByUID(uid);

        if (repoDb == null) {
            throw new LivingDocServerException(REPOSITORY_NOT_FOUND, "Repository not registered");
        }

        return repoDb;
    }

    private void setExecutionEnable(DocumentNode node, String repoUID, SystemUnderTest systemUnderTest) {
        if (node.isExecutable()) {
            return;
        }

        if (node instanceof ReferenceNode) {
            ReferenceNode refNode = ( ReferenceNode ) node;
            Specification spec = documentDao.getSpecificationByName(refNode.getRepositoryUID(), refNode.getTitle());
            node.setIsExecutable(spec != null && refNode.getSutName().equals(systemUnderTest.getName()));
        } else {
            Specification spec = documentDao.getSpecificationByName(repoUID, node.getTitle());
            node.setIsExecutable(spec != null && spec.getTargetedSystemUnderTests().contains(systemUnderTest));
            for (DocumentNode children : node.getChildren()) {
                setExecutionEnable(children, repoUID, systemUnderTest);
            }
        }
    }
}
