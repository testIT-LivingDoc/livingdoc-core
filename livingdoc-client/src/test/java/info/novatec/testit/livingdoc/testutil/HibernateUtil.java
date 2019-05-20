package info.novatec.testit.livingdoc.testutil;

import info.novatec.testit.livingdoc.server.domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws IOException {
        if (sessionFactory == null) {
            ServiceRegistry serviceRegistry = configureServiceRegistry();
            sessionFactory = makeSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        metadataSources.addAnnotatedClass(SystemInfo.class)
                .addAnnotatedClass(Project.class)
                .addAnnotatedClass(Runner.class)
                .addAnnotatedClass(Repository.class)
                .addAnnotatedClass(RepositoryType.class)
                .addAnnotatedClass(SystemUnderTest.class)
                .addAnnotatedClass(Requirement.class)
                .addAnnotatedClass(Specification.class)
                .addAnnotatedClass(Reference.class)
                .addAnnotatedClass(Execution.class);

        Metadata metadata = metadataSources.getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }

    private static ServiceRegistry configureServiceRegistry() throws IOException {
        return configureServiceRegistry(getProperties());
    }

    private static ServiceRegistry configureServiceRegistry(Properties properties) throws IOException {
        return new StandardServiceRegistryBuilder().applySettings(properties)
                .build();
    }

    public static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        URL propertiesURL = Thread.currentThread()
                .getContextClassLoader()
                .getResource("hibernate.properties");

        FileInputStream inputStream = new FileInputStream(propertiesURL.getFile());
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }
        return properties;
    }
}
