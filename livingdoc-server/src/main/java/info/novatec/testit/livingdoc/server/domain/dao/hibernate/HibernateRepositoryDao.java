package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.server.domain.dao.ProjectDao;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;


public class HibernateRepositoryDao implements RepositoryDao {
    public static final String SUPPRESS_UNCHECKED = "unchecked";
    private ProjectDao projectDao;
    private SessionService sessionService;

    public HibernateRepositoryDao(SessionService sessionService, ProjectDao projectDao) {
        this.sessionService = sessionService;
        this.projectDao = projectDao;
    }

    public HibernateRepositoryDao(SessionService sessionService) {
        this(sessionService, new HibernateProjectDao(sessionService));
    }

    @Override
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public List<Repository> getAll() {
        final Criteria criteria = sessionService.getSession().createCriteria(Repository.class);
        List<Repository> list = criteria.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    @Override
    public Repository getByUID(String repositoryUid) {
        final Criteria crit = sessionService.getSession().createCriteria(Repository.class);
        crit.add(Property.forName("uid").eq(repositoryUid));
        Repository repository = ( Repository ) crit.uniqueResult();
        HibernateLazyInitializer.init(repository);
        return repository;
    }

    @Override
    public Repository getByName(String projectName, String repositoryName) {
        final Criteria crit = sessionService.getSession().createCriteria(Repository.class);
        crit.add(Property.forName("name").eq(repositoryName));
        crit.createAlias("project", "p");
        crit.add(Restrictions.eq("p.name", projectName));
        Repository repository = ( Repository ) crit.uniqueResult();
        HibernateLazyInitializer.init(repository);
        return repository;
    }

    @Override
    public RepositoryType getTypeByName(String repositoryTypeName) {
        final Criteria crit = sessionService.getSession().createCriteria(RepositoryType.class);
        crit.add(Property.forName("name").eq(repositoryTypeName));
        RepositoryType repositoryType = ( RepositoryType ) crit.uniqueResult();
        HibernateLazyInitializer.init(repositoryType);
        return repositoryType;
    }

    @Override
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public List<Repository> getAll(String projectName) {
        final Criteria crit = sessionService.getSession().createCriteria(Repository.class);
        if (projectName != null) {
            crit.createAlias("project", "p");
            crit.add(Restrictions.eq("p.name", projectName));
        }
        List<Repository> list = crit.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    @Override
    public List<Repository> getAllTestRepositories(String projectName) {
        return getAllRepositories(projectName, ContentType.TEST);
    }

    @Override
    public List<Repository> getAllRequirementRepositories(String projectName) {
        return getAllRepositories(projectName, ContentType.REQUIREMENT);
    }

    @Override
    public List<Repository> getAllRepositories(ContentType type) {
        return getAllRepositories(null, type);
    }

    @SuppressWarnings(SUPPRESS_UNCHECKED)
    private List<Repository> getAllRepositories(String projectName, ContentType type) {
        final Criteria crit = sessionService.getSession().createCriteria(Repository.class);
        SimpleExpression restriction = Restrictions.eq("contentType", type);
        SimpleExpression bothRestriction = Restrictions.eq("contentType", ContentType.BOTH);
        crit.add(Restrictions.or(restriction, bothRestriction));
        if (projectName != null) {
            crit.createAlias("project", "p");
            crit.add(Restrictions.eq("p.name", projectName));
        }
        List<Repository> list = crit.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    /**
     * @throws LivingDocServerException
     */
    @Override
    public Repository create(Repository newRepository) throws LivingDocServerException {
        Project project = projectDao.getByName(newRepository.getProject().getName());
        if (project == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_NOT_FOUND, "project not found");
        }

        RepositoryType type = getTypeByName(newRepository.getType().getName());
        if (type == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_TYPE_NOT_FOUND, "Type not found");
        }

        newRepository.setType(type);
        project.addRepository(newRepository);
        sessionService.getSession().update(project);

        return newRepository;
    }

    @Override
    public void update(Repository repository) throws LivingDocServerException {
        Repository repositoryToUpdate = getByUID(repository.getUid());
        if (repositoryToUpdate == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, "Repository not found");
        }

        if ( ! repository.getProject().getName().equals(repositoryToUpdate.getProject().getName())) {
            if ( ! repositoryToUpdate.getSpecifications().isEmpty() || ! repositoryToUpdate.getRequirements().isEmpty()) {
                throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_DOC_ASSOCIATED, "Doc associated");
            }

            Project newProject = projectDao.getByName(repository.getProject().getName());
            if (newProject == null) {
                throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_NOT_FOUND, "project not found");
            }

            Project oldProject = repositoryToUpdate.getProject();
            oldProject.removeRepository(repositoryToUpdate);

            if (oldProject.getRepositories().isEmpty()) {
                sessionService.getSession().delete(oldProject);
            } else {
                sessionService.getSession().update(oldProject);
            }

            newProject.addRepository(repositoryToUpdate);
            sessionService.getSession().update(newProject);
        }

        repositoryToUpdate.setBaseRepositoryUrl(repository.getBaseRepositoryUrl());
        repositoryToUpdate.setBaseTestUrl(repository.getBaseTestUrl());
        repositoryToUpdate.setBaseUrl(repository.getBaseUrl());
        repositoryToUpdate.setContentType(repository.getContentType());
        repositoryToUpdate.setName(repository.getName());
        repositoryToUpdate.setUsername(repository.getUsername());
        repositoryToUpdate.setPassword(repository.getPassword());

        sessionService.getSession().update(repositoryToUpdate);
    }

    @Override
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public List<RepositoryType> getAllTypes() {
        final Criteria crit = sessionService.getSession().createCriteria(RepositoryType.class);
        List<RepositoryType> list = crit.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    @Override
    public RepositoryType create(RepositoryType repositoryType) {
        sessionService.getSession().save(repositoryType);
        return repositoryType;
    }

    @Override
    public void remove(String repositoryUid) throws LivingDocServerException {
        Repository repositoryToDelete = getByUID(repositoryUid);
        if (repositoryToDelete == null) {
            return;
        }

        if (repositoryToDelete.getRequirements().size() > 0 || repositoryToDelete.getSpecifications().size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_DOC_ASSOCIATED,
                "Requirement or specifications associated");
        }

        sessionService.getSession().delete(repositoryToDelete);
    }
}
