package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.dao.ProjectDao;


public class HibernateProjectDao implements ProjectDao {

    private SessionService sessionService;

    public HibernateProjectDao(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Project getByName(String name) {
        final Criteria crit = sessionService.getSession().createCriteria(Project.class);
        crit.add(Property.forName("name").eq(name));
        Project project = ( Project ) crit.uniqueResult();
        HibernateLazyInitializer.init(project);
        return project;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> getAll() {
        final Criteria criteria = sessionService.getSession().createCriteria(Project.class);
        List<Project> list = criteria.list();
        HibernateLazyInitializer.initCollection(list);
        return list;
    }

    @Override
    public Project create(String name) throws LivingDocServerException {

        if (getByName(name) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_ALREADY_EXISTS, "Project already exists");
        }

        Project project = Project.newInstance(name);
        sessionService.getSession().save(project);
        return project;
    }

    @Override
    public void remove(String name) throws LivingDocServerException {
        Project project = getByName(name);
        if (project == null) {
            return;
        }

        if (project.getRepositories().size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_REPOSITORY_ASSOCIATED,
                "Repository associated");
        }

        if (project.getSystemUnderTests().size() > 0) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_SUTS_ASSOCIATED,
                "System under tests associated");
        }

        sessionService.getSession().delete(project);
    }

    @Override
    public Project update(String oldProjectName, Project project) throws LivingDocServerException {

        if ( ! oldProjectName.equals(project.getName()) && getByName(project.getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_ALREADY_EXISTS, "Project already exists");
        }

        Project projectToUpdate = getByName(oldProjectName);
        if (projectToUpdate == null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.PROJECT_NOT_FOUND, "Project not found");
        }

        projectToUpdate.setName(project.getName());

        sessionService.getSession().update(projectToUpdate);
        return projectToUpdate;
    }
}
