package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.server.domain.dao.DocumentDao;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;


public class HibernateDocumentDao implements DocumentDao {
    public static final String SYSTEM_UNDER_TEST_NOT_FOUND_MSG = "SystemUnderTest not found";
    public static final String SYSTEM_UNDER_TEST = "systemUnderTest";
    public static final String SUT = "sut";
    public static final String SUT_NAME = "sut.name";
    public static final String REPO = "repo";
    public static final String REPO_UID = "repo.uid";
    public static final String SUPPRESS_UNCHECKED = "unchecked";
    private SessionService sessionService;
    private RepositoryDao repositoryDao;
    private SystemUnderTestDao systemUnderTestDao;

    public HibernateDocumentDao(SessionService sessionService, RepositoryDao repositoryDao,
        SystemUnderTestDao systemUnderTestDao) {
        this.sessionService = sessionService;
        this.repositoryDao = repositoryDao;
        this.systemUnderTestDao = systemUnderTestDao;
    }

    public HibernateDocumentDao(SessionService sessionService) {
        this(sessionService, new HibernateRepositoryDao(sessionService), new HibernateSystemUnderTestDao(sessionService));
    }

    @Override
    public Requirement getRequirementByName(String repositoryUid, String requirementName) {
        Criteria crit = sessionService.getSession().createCriteria(Requirement.class);
        crit.add(Restrictions.eq("name", requirementName));
        crit.createAlias("repository", "r");
        crit.add(Restrictions.eq("r.uid", repositoryUid));

        Requirement requirement = ( Requirement ) crit.uniqueResult();
        HibernateLazyInitializer.init(requirement);
        return requirement;
    }

    @Override
    public Requirement createRequirement(String repositoryUid, String requirementName) throws LivingDocServerException {
        Repository repository = repositoryDao.getByUID(repositoryUid);
        if (repository == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, "Repo not found");
        }

        Requirement requirement = Requirement.newInstance(requirementName);
        repository.addRequirement(requirement);
        sessionService.getSession().save(repository);
        return requirement;
    }

    @Override
    public Requirement getOrCreateRequirement(String repositoryUid, String requirementName) throws LivingDocServerException {
        Requirement requirement = getRequirementByName(repositoryUid, requirementName);
        if (requirement == null) {
            return createRequirement(repositoryUid, requirementName);
        }

        HibernateLazyInitializer.init(requirement);
        return requirement;
    }

    /**
     * @throws LivingDocServerException
     */
    @Override
    public void removeRequirement(Requirement requirement) throws LivingDocServerException {
        requirement = getRequirementByName(requirement.getRepository().getUid(), requirement.getName());
        if (requirement != null) {
            requirement.getRepository().removeRequirement(requirement);
            sessionService.getSession().delete(requirement);
        }
    }

    @Override
    public Specification getSpecificationByName(String repositoryUid, String specificationName) {
        Criteria crit = sessionService.getSession().createCriteria(Specification.class);
        crit.add(Restrictions.ilike("name", specificationName, MatchMode.EXACT));
        crit.createAlias("repository", "r");
        crit.add(Restrictions.eq("r.uid", repositoryUid));

        Specification specification = ( Specification ) crit.uniqueResult();
        HibernateLazyInitializer.init(specification);
        return specification;
    }

    @Override
    public Specification getSpecificationById(Long id) {
        Criteria crit = sessionService.getSession().createCriteria(Specification.class);
        crit.add(Restrictions.eq("id", id));

        Specification specification = ( Specification ) crit.uniqueResult();
        HibernateLazyInitializer.init(specification);
        return specification;
    }

    @Override
    public Specification createSpecification(String systemUnderTestName, String repositoryUid, String specificationName)
        throws LivingDocServerException {
        Repository repository = repositoryDao.getByUID(repositoryUid);
        if (repository == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, "Repo not found");
        }

        SystemUnderTest sut;
        if (systemUnderTestName != null) {
            sut = systemUnderTestDao.getByName(repository.getProject().getName(), systemUnderTestName);
            if (sut == null) {
                throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, SYSTEM_UNDER_TEST_NOT_FOUND_MSG);
            }
        } else {
            sut = repository.getProject().getDefaultSystemUnderTest();
            if (sut == null) {
                throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_DEFAULT_SUT_NOT_FOUND,
                    "Default sut not found");
            }
        }

        Specification specification = Specification.newInstance(specificationName);
        repository.addSpecification(specification);
        specification.addSystemUnderTest(sut);

        if (repository.getContentType().equals(ContentType.REQUIREMENT)) {
            repository.setContentType(ContentType.BOTH);
        }

        sessionService.getSession().save(repository);

        return specification;
    }

    @Override
    public Specification getOrCreateSpecification(String systemUnderTestName, String repositoryUid, String specificationName)
        throws LivingDocServerException {
        Specification specification = getSpecificationByName(repositoryUid, specificationName);

        if (specification == null) {
            specification = createSpecification(systemUnderTestName, repositoryUid, specificationName);
        }

        return specification;
    }

    @Override
    public void updateSpecification(Specification oldSpecification, Specification newSpecification)
        throws LivingDocServerException {
        String oldUid = oldSpecification.getRepository().getUid();
        Specification specificationToUpdate = getSpecificationByName(oldUid, oldSpecification.getName());

        if (specificationToUpdate == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");
        }

        specificationToUpdate.setName(newSpecification.getName());
        sessionService.getSession().update(specificationToUpdate);
    }

    @Override
    public void removeSpecification(Specification specification) throws LivingDocServerException {
        specification = getSpecificationByName(specification.getRepository().getUid(), specification.getName());
        if (specification != null) {
            specification.getRepository().removeSpecification(specification);
            sessionService.getSession().delete(specification);
        }
    }

    @Override
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException {
        SystemUnderTest sut = systemUnderTestDao.getByName(systemUnderTest.getProject().getName(), systemUnderTest
            .getName());
        if (sut == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, SYSTEM_UNDER_TEST_NOT_FOUND_MSG);
        }

        specification = getSpecificationByName(specification.getRepository().getUid(), specification.getName());
        if (specification == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");
        }

        specification.addSystemUnderTest(sut);
        sessionService.getSession().save(specification);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException {
        if ( ! getAllReferences(systemUnderTest, specification).isEmpty()) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_REFERENCE_ASSOCIATED,
                "SystemUnderTest is in a reference");
        }

        SystemUnderTest sut = systemUnderTestDao.getByName(systemUnderTest.getProject().getName(), systemUnderTest
            .getName());
        if (sut == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, SYSTEM_UNDER_TEST_NOT_FOUND_MSG);
        }

        specification = getSpecificationByName(specification.getRepository().getUid(), specification.getName());
        if (specification == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");
        }

        specification.removeSystemUnderTest(sut);
        sessionService.getSession().save(specification);
    }

    @Override
    public Reference get(Reference reference) {
        final Criteria crit = sessionService.getSession().createCriteria(Reference.class);

        if (StringUtils.isEmpty(reference.getSections())) {
            crit.add(Restrictions.isNull("sections"));
        } else {
            crit.add(Restrictions.eq("sections", reference.getSections()));
        }

        crit.createAlias("requirement", "req");
        crit.add(Restrictions.eq("req.name", reference.getRequirement().getName()));
        crit.createAlias("req.repository", "reqRepo");
        crit.add(Restrictions.eq("reqRepo.uid", reference.getRequirement().getRepository().getUid()));
        crit.createAlias("specification", "spec");
        crit.add(Restrictions.eq("spec.name", reference.getSpecification().getName()));
        crit.createAlias("spec.repository", "specRepo");
        crit.add(Restrictions.eq("specRepo.uid", reference.getSpecification().getRepository().getUid()));
        crit.createAlias(SYSTEM_UNDER_TEST, SUT);
        crit.add(Restrictions.eq(SUT_NAME, reference.getSystemUnderTest().getName()));
        crit.createAlias("sut.project", "sp");
        crit.add(Restrictions.eq("sp.name", reference.getSystemUnderTest().getProject().getName()));

        Reference result = ( Reference ) crit.uniqueResult();
        HibernateLazyInitializer.init(result);
        return result;
    }

    public List<Reference> getAllReferences(SystemUnderTest systemUnderTest, Specification specification) {
        final Criteria crit = sessionService.getSession().createCriteria(Reference.class);
        crit.createAlias("specification", "spec");
        crit.add(Restrictions.eq("spec.name", specification.getName()));
        crit.createAlias("spec.repository", REPO);
        crit.add(Restrictions.eq(REPO_UID, specification.getRepository().getUid()));
        crit.createAlias(SYSTEM_UNDER_TEST, SUT);
        crit.add(Restrictions.eq(SUT_NAME, systemUnderTest.getName()));
        crit.createAlias("sut.project", "sp");
        crit.add(Restrictions.eq("sp.name", systemUnderTest.getProject().getName()));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Reference> references = crit.list();
        HibernateLazyInitializer.initCollection(references);
        return references;
    }

    @Override
    public List<Reference> getAllReferences(Specification specification) {
        final Criteria crit = sessionService.getSession().createCriteria(Reference.class);
        crit.createAlias("specification", "spec");
        crit.add(Restrictions.eq("spec.name", specification.getName()));
        crit.createAlias("spec.repository", REPO);
        crit.add(Restrictions.eq(REPO_UID, specification.getRepository().getUid()));
        crit.createAlias("requirement", "req");
        crit.addOrder(Order.asc("req.name"));
        crit.createAlias(SYSTEM_UNDER_TEST, SUT);
        crit.addOrder(Order.asc(SUT_NAME));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Reference> references = crit.list();
        HibernateLazyInitializer.initCollection(references);
        return references;
    }

    @Override
    public List<Reference> getAllReferences(Requirement requirement) {
        final Criteria crit = sessionService.getSession().createCriteria(Reference.class);
        crit.createAlias("requirement", "req");
        crit.add(Restrictions.eq("req.name", requirement.getName()));
        crit.createAlias("req.repository", REPO);
        crit.add(Restrictions.eq(REPO_UID, requirement.getRepository().getUid()));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Reference> references = crit.list();
        HibernateLazyInitializer.initCollection(references);
        return references;
    }

    @Override
    public Reference createReference(Reference reference) throws LivingDocServerException {
        String projectName = reference.getSystemUnderTest().getProject().getName();
        String reqRepoUid = reference.getRequirement().getRepository().getUid();
        String requirementName = reference.getRequirement().getName();
        String testName = reference.getSpecification().getName();
        String testRepoUid = reference.getSpecification().getRepository().getUid();
        String sutName = reference.getSystemUnderTest().getName();
        String sections = reference.getSections();

        Requirement requirement = getOrCreateRequirement(reqRepoUid, requirementName);

        checkDuplicatedReference(requirement, testName, testRepoUid, sutName, sections);

        Specification specification = getOrCreateSpecification(sutName, testRepoUid, testName);
        SystemUnderTest sut = systemUnderTestDao.getByName(projectName, sutName);

        reference = Reference.newInstance(requirement, specification, sut, sections);
        sessionService.getSession().save(reference);

        return reference;
    }

    private void checkDuplicatedReference(Requirement requirement, String testName, String testRepoUid, String sutName,
        String sections) throws LivingDocServerException {
        Set<Reference> references = requirement.getReferences();

        for (Reference reference : references) {
            if (reference.getSystemUnderTest().getName().equalsIgnoreCase(sutName) && reference.getSpecification()
                .getRepository().getUid().equalsIgnoreCase(testRepoUid) && reference.getSpecification().getName()
                    .equalsIgnoreCase(testName) && StringUtils.equals(reference.getSections(), sections)) {
                throw new LivingDocServerException(LivingDocServerErrorKey.REFERENCE_CREATE_ALREADYEXIST,
                    "Reference already exist");
            }
        }
    }

    @Override
    public void removeReference(Reference reference) throws LivingDocServerException {
        reference = get(reference);

        if (reference != null) {
            Requirement requirement = reference.getRequirement();
            requirement.removeReference(reference);
            reference.getSpecification().removeReference(reference);
            if (requirement.getReferences().isEmpty()) {
                requirement.getRepository().removeRequirement(requirement);
                sessionService.getSession().delete(requirement);
            }

            sessionService.getSession().delete(reference);
        }
    }

    @Override
    public Reference updateReference(Reference oldReference, Reference newReference) throws LivingDocServerException {
        removeReference(oldReference);
        return createReference(newReference);
    }

    @Override
    public Execution createExecution(Execution execution) throws LivingDocServerException {
        sessionService.getSession().save(execution);
        return execution;
    }

    @Override
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implemeted,
        String locale) throws LivingDocServerException {
        specification = getOrCreateSpecification(systemUnderTest.getName(), specification.getRepository().getUid(),
            specification.getName());
        systemUnderTest = systemUnderTestDao.getByName(systemUnderTest.getProject().getName(), systemUnderTest.getName());
        if (systemUnderTest == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, SYSTEM_UNDER_TEST_NOT_FOUND_MSG);
        }

        Execution exe = systemUnderTest.execute(specification, implemeted, null, locale);

        if (exe.wasRunned()) {
            if (exe.wasRemotelyExecuted()) {
                exe.setSpecification(getOrCreateSpecification(systemUnderTest.getName(), specification.getRepository()
                    .getUid(), specification.getName()));
                exe.setSystemUnderTest(systemUnderTestDao.getByName(systemUnderTest.getProject().getName(), systemUnderTest
                    .getName()));
            }

            specification.addExecution(exe);
            sessionService.getSession().save(exe);
        }

        return exe;
    }

    @Override
    public Reference runReference(Reference reference, String locale) throws LivingDocServerException {
        Reference loadedReference = get(reference);

        if (loadedReference == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REFERENCE_NOT_FOUND, "Reference not found");
        }

        Execution exe = loadedReference.execute(false, locale);

        if (exe.wasRunned()) {
            loadedReference.getSpecification().addExecution(exe);
            loadedReference.setLastExecution(exe);
            sessionService.getSession().save(exe);
        }

        return loadedReference;
    }

    @Override
    public List<Specification> getSpecifications(SystemUnderTest sut, Repository repository) {
        final Criteria crit = sessionService.getSession().createCriteria(Specification.class);
        crit.createAlias("repository", REPO);
        crit.add(Restrictions.eq(REPO_UID, repository.getUid()));

        crit.createAlias("targetedSystemUnderTests", "suts");
        crit.add(Restrictions.eq("suts.name", sut.getName()));
        crit.createAlias("suts.project", "sp");
        crit.add(Restrictions.eq("sp.name", sut.getProject().getName()));
        crit.addOrder(Order.asc("name"));

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Specification> specifications = crit.list();
        HibernateLazyInitializer.initCollection(specifications);
        return specifications;
    }

    @Override
    public List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults) {
        final Criteria crit = sessionService.getSession().createCriteria(Execution.class);

        crit.add(Restrictions.eq("specification.id", specification.getId()));

        if (sut != null) {
            crit.createAlias(SYSTEM_UNDER_TEST, SUT);
            crit.add(Restrictions.eq(SUT_NAME, sut.getName()));
        }

        /* crit.add(Restrictions.or(Restrictions.or(Restrictions.not(
         * Restrictions . eq("errors", 0)),
         * Restrictions.not(Restrictions.eq("success", 0))),
         * Restrictions.or(Restrictions.not(Restrictions.eq("ignored", 0)),
         * Restrictions.not(Restrictions.eq("failures", 0))))); */

        crit.addOrder(Order.desc("executionDate"));
        crit.setMaxResults(maxResults);

        @SuppressWarnings(SUPPRESS_UNCHECKED)
        List<Execution> executions = crit.list();
        HibernateLazyInitializer.initCollection(executions);
        Collections.reverse(executions);
        return executions;
    }

    @Override
    public Execution getSpecificationExecution(Long id) {
        final Criteria crit = sessionService.getSession().createCriteria(Execution.class);

        crit.add(Restrictions.eq("id", id));

        Execution execution = ( Execution ) crit.uniqueResult();
        HibernateLazyInitializer.init(execution);
        return execution;
    }
}
