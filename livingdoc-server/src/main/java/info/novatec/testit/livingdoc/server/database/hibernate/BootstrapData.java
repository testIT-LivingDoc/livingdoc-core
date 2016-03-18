package info.novatec.testit.livingdoc.server.database.hibernate;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.server.LivingDocServer;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateRepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemUnderTestDao;


public class BootstrapData {
    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    private final SessionService sessionService;
    private final Properties properties;
    private SystemInfoDao systemInfoDao;
    private SystemUnderTestDao systemUnderTestDao;
    private RepositoryDao repositoryDao;

    public BootstrapData(SessionService sessionService, Properties properties) {
        this(sessionService, properties, new HibernateSystemInfoDao(sessionService), new HibernateSystemUnderTestDao(
            sessionService), new HibernateRepositoryDao(sessionService));
    }

    public BootstrapData(SessionService sessionService, Properties properties, SystemInfoDao systemInfoDao,
        SystemUnderTestDao systemUnderTestDao, RepositoryDao repositoryDao) {
        this.sessionService = sessionService;
        this.properties = properties;
        this.systemInfoDao = systemInfoDao;
        this.systemUnderTestDao = systemUnderTestDao;
        this.repositoryDao = repositoryDao;
    }

    public void execute() throws LivingDocServerException {
        try {
            sessionService.beginTransaction();

            new InitialDatas(systemInfoDao, repositoryDao).insert();
            new DefaultRunners(systemUnderTestDao, properties).insertJavaRunner();
            new ServerUpgrader(sessionService, systemInfoDao).upgradeTo(LivingDocServer.VERSION);

            sessionService.commitTransaction();
        } catch (Exception e) {
            sessionService.rollbackTransaction();
            log.error("Bootstrap Failure: ", e);
            throw new LivingDocServerException("", "Boostrap Failure", e);
        }
    }
}
