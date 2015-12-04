package info.novatec.testit.livingdoc.samples.application.phonebook;

import java.net.URL;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


public class HibernateDatabase {
    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";
    private final AnnotationConfiguration cfg;

    public HibernateDatabase(Properties properties) throws HibernateException {
        cfg = new AnnotationConfiguration();
        cfg.setProperties(properties);
        setAnnotadedClasses();
        loadConfig();
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
        cfg.addAnnotatedClass(Country.class).addAnnotatedClass(State.class).addAnnotatedClass(PhoneBook.class)
            .addAnnotatedClass(PhoneBookEntry.class);
    }

    private void loadConfig() {
        URL xmlConfig = HibernateDatabase.class.getClassLoader().getResource(HIBERNATE_CONFIG_FILE);
        if (xmlConfig != null) {
            cfg.configure(xmlConfig);
        }
    }
}
