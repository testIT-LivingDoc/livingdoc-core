package info.novatec.testit.livingdoc.server.database.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import info.novatec.testit.livingdoc.server.database.SessionService;


public class HibernateSessionService implements SessionService {
    private final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();
    private final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
    private SessionFactory sessionFactory;

    public HibernateSessionService(Properties properties) throws HibernateException {
        HibernateDatabase db = new HibernateDatabase(properties);
        sessionFactory = db.getSessionFactory();
    }

    public HibernateSessionService() {

    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void startSession() throws HibernateException {
        initSession();
    }

    @Override
    public Session getSession() throws HibernateException {
        Session s = threadSession.get();
        if (s == null) {
            s = initSession();
        }

        return s;
    }

    /**
     * closeSession
     */
    @Override
    public void closeSession() throws HibernateException {
        Session s = threadSession.get();
        threadSession.set(null);
        if (s != null) {
            s.close();
        }

        Transaction tx = threadTransaction.get();
        if (tx != null) {
            threadTransaction.set(null);
            // log warning todo
        }
    }

    @Override
    public void beginTransaction() throws HibernateException {
        Transaction tx = threadTransaction.get();
        if (tx == null) {
            tx = getSession().beginTransaction();
            threadTransaction.set(tx);
        }
    }

    @Override
    public void commitTransaction() throws HibernateException {
        Transaction tx = threadTransaction.get();
        threadTransaction.set(null);
        if (tx != null && ! tx.wasCommitted() && ! tx.wasRolledBack()) {
            tx.commit();
        }
    }

    @Override
    public void rollbackTransaction() throws HibernateException {
        Transaction tx = threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && ! tx.wasCommitted() && ! tx.wasRolledBack()) {
                tx.rollback();
            }
        } finally {
            closeSession();
        }
    }

    public void close() throws HibernateException {
        sessionFactory.close();
    }

    private synchronized Session initSession() {
        Session s = sessionFactory.openSession();
        threadSession.set(s);
        return s;
    }
}
