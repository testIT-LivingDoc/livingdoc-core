package info.novatec.testit.livingdoc.server.database.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class HibernateDatabase {
    private final AnnotationConfiguration cfg;

    public HibernateDatabase(Properties properties) throws HibernateException {
        cfg = new AnnotationConfiguration();
        cfg.setProperties(properties);
        setAnnotadedClasses();
    }

    public void createDatabase() throws HibernateException {
        new SchemaExport(cfg).create(false, true);
    }

    public void dropDatabase() throws HibernateException {
        new SchemaExport(cfg).drop(false, true);
    }

    public Configuration getConfiguration() {
        return cfg;
    }

    public SessionFactory getSessionFactory() throws HibernateException {
        return cfg.buildSessionFactory();
    }

    private void setAnnotadedClasses() {
        cfg.addAnnotatedClass(SystemInfo.class).addAnnotatedClass(Project.class).addAnnotatedClass(Runner.class)
            .addAnnotatedClass(Repository.class).addAnnotatedClass(RepositoryType.class).addAnnotatedClass(
                SystemUnderTest.class).addAnnotatedClass(Requirement.class).addAnnotatedClass(Specification.class)
            .addAnnotatedClass(Reference.class).addAnnotatedClass(Execution.class);
    }

}
