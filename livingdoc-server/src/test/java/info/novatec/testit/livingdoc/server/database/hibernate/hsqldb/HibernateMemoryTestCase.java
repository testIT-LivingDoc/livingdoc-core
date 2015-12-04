package info.novatec.testit.livingdoc.server.database.hibernate.hsqldb;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;

import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.database.hibernate.HibernateDatabase;


public abstract class HibernateMemoryTestCase implements SessionService {
    protected HibernateDatabase db;
    protected SessionFactory sessionFactory;
    protected Session session;
    protected Transaction transaction;

    @Before
    public void setUp() throws Exception {
        db = new HibernateDatabase(properties());
        db.createDatabase();
        sessionFactory = db.getSessionFactory();
    }

    @After
    public void tearDown() throws Exception {
        sessionFactory.close();
        db.dropDatabase();
    }

    public Properties properties() {
        Properties properties = new Properties();

        // properties.setProperty("hibernate.dialect",
        // "org.hibernate.dialect.H2Dialect");
        // properties.setProperty("hibernate.connection.driver_class",
        // "org.h2.Driver");
        // properties.setProperty("hibernate.connection.url",
        // "jdbc:h2:mem:test;MVCC=true");

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        properties.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:test");
        // properties.setProperty("hibernate.connection.url",
        // "jdbc:hsqldb:mem:test;hsqldb.tx=mvcc;hsqldb.tx_level=read_commited");
        properties.setProperty("hibernate.connection.username", "sa");
        properties.setProperty("hibernate.connection.password", "");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
        properties.setProperty("hibernate.connection.pool_size", "1");

        return properties;
    }

    @Override
    public void startSession() throws HibernateException {
        if (session == null) {
            session = sessionFactory.openSession();
        }
    }

    @Override
    public void closeSession() throws HibernateException {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void beginTransaction() throws HibernateException {
        if (session == null) {
            throw new RuntimeException("No session");
        }
        transaction = session.beginTransaction();
    }

    @Override
    public void commitTransaction() throws HibernateException {
        if (transaction == null) {
            throw new RuntimeException("No transaction");
        }
        transaction.commit();
    }

    @Override
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
        boolean sessionActive = session != null;

        if ( ! sessionActive) {
            startSession();
        }

        Object obj = callback.execute(session);

        if ( ! sessionActive) {
            closeSession();
        }

        return obj;
    }

    public void create(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            @Override
            public Object execute(Session paramSession) throws HibernateException {
                paramSession.save(entity);
                return null;
            }
        });
    }

    public void update(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            @Override
            public Object execute(Session paramSession) throws HibernateException {
                paramSession.saveOrUpdate(entity);
                return null;
            }
        });
    }

    public void delete(final Object entity) throws HibernateException {
        doInTransaction(new HibernateMemoryCallback() {
            @Override
            public Object execute(Session paramSession) throws HibernateException {
                paramSession.delete(entity);
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T getById(final Class<T> theClass, final long id) throws HibernateException {
        return ( T ) doInSession(new HibernateMemoryCallback() {
            @Override
            public Object execute(Session paramSession) throws HibernateException {
                return paramSession.get(theClass, new Long(id));
            }
        });
    }

    public Object getByIdInUnwrappedSession(final Class< ? > theClass, final long id) throws HibernateException {
        return session.get(theClass, new Long(id));
    }

    public Configuration getHibernateConfiguration() {
        return db.getConfiguration();
    }
}
