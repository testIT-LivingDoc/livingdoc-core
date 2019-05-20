package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.testutil.HibernateUtil;
import info.novatec.testit.livingdoc.util.EncryptionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RepositoryEntitiyTest {
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() throws IOException {
        session = HibernateUtil.getSessionFactory()
                .openSession();
        transaction = session.beginTransaction();


        session.createSQLQuery("delete from REPOSITORY")
                .executeUpdate();

        transaction.commit();
        transaction = session.beginTransaction();
    }

    @After
    public void tearDown() {
        transaction.rollback();
        session.close();
    }

    @Test
    public void encryptsPassword() {
        Repository repository = new Repository();
        repository.setUid("unique");
        repository.setName("name");
        repository.setPassword("LD");
        repository.setBaseUrl("as");
        repository.setBaseTestUrl("as");
        repository.setBaseRepositoryUrl("as");

        Long id = (Long) session.save(repository);

        session.flush();
        session.clear();


        String dbPassword = (String) session.createSQLQuery("select r.PASSWORD from REPOSITORY r where r.id = :id")
                .setParameter("id", id).uniqueResult();

        assertNotEquals(dbPassword, repository.getPassword());
        assertEquals(new EncryptionUtil().decrypt(dbPassword), repository.getPassword());
    }
}
