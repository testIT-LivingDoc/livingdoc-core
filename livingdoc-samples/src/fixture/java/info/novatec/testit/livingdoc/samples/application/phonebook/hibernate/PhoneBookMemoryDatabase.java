package info.novatec.testit.livingdoc.samples.application.phonebook.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import info.novatec.testit.livingdoc.samples.application.phonebook.HibernateDatabase;
import info.novatec.testit.livingdoc.samples.application.phonebook.SessionService;

public class PhoneBookMemoryDatabase implements SessionService 
{
    protected HibernateDatabase db;
    protected SessionFactory sessionFactory;
    protected Session session;
    protected Transaction transaction;
    
    public void mount() throws HibernateException {
        db = new HibernateDatabase(properties());
        db.createDatabase();
        sessionFactory = db.getSessionFactory();
    }

    public void tearDown() throws HibernateException {
        db.dropDatabase();
    }
    
    public Properties properties() {
        Properties properties = new Properties();

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect"); 
        properties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        properties.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:testdb");
        properties.setProperty("hibernate.connection.username", "sa");
        properties.setProperty("hibernate.connection.password", "");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");        
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
        properties.setProperty("hibernate.connection.pool_size", "1");
        
        return properties;
    }
    
    public void startSession() throws HibernateException {
        if (session == null) {
            session = sessionFactory.openSession();
        }
    }
    
    public void closeSession() throws HibernateException {
        if (session == null) {
            session.close();
            session = null;
        }
    }

    public Session getSession() {
        return session;
    }
    
    public void beginTransaction() throws HibernateException {
        if (session == null) {
            throw new RuntimeException("No session");
        }
        transaction = session.beginTransaction();
    }
    
    public void commitTransaction() throws HibernateException {
        if (transaction == null) {
            throw new RuntimeException("No transaction");
        }
        transaction.commit();
    }
    
    public void rollbackTransaction() throws HibernateException {
        if (transaction == null) {
            throw new RuntimeException("No transaction");
        }
        transaction.rollback();
    }

    public Object doInTransaction(HibernateMemoryCallback callback) throws HibernateException {
       startSession();
       Transaction tx = session.beginTransaction();
       Object obj = callback.execute(session);
       tx.commit();
       closeSession();
       return obj;
    }
    
    public Object doInSession(HibernateMemoryCallback callback) throws HibernateException {
       startSession();
       Object obj = callback.execute(session);
       closeSession();
       return obj;
    }
    
    public void create(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            public Object execute(Session session) throws HibernateException {
                session.save(entity);
                return null;
            }
        });
    }
    
    public void update(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            public Object execute(Session session) throws HibernateException {
                session.saveOrUpdate(entity);
                return null;
            }
        });
    }
    
    public void delete(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            public Object execute(Session session) throws HibernateException {
                session.delete(entity);
                return null;
            }
        });
    }
    
    public Object getById(final Class theClass, final long id) throws HibernateException {
        return doInSession(new HibernateMemoryCallback() {
            public Object execute(Session session) throws HibernateException {
                return session.get(theClass, new Long(id));
            }
        });
    }

    public Object getByIdInUnwrappedSession(final Class theClass, final long id) throws HibernateException {
        return session.get(theClass, new Long(id));
    }
    
    public Configuration getHibernateConfiguration() {
        return db.getConfiguration();
    }
}
