package info.novatec.testit.livingdoc.server.database.hibernate;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.repository.AtlassianRepository;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.server.LivingDocServer;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.RepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateRepositoryDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class InitialDatas {
    public static final String DEFAULT_VERSION = "1.0";

    private final SystemInfoDao systDao;
    private final RepositoryDao repoDao;

    public InitialDatas(SessionService sessionService) {
        this(new HibernateSystemInfoDao(sessionService),
                new HibernateRepositoryDao(sessionService));
    }

    public InitialDatas(SystemInfoDao systemInfoDao, RepositoryDao repositoryDao) {
        this.systDao = systemInfoDao;
        this.repoDao = repositoryDao;
    }

    public void insert() {
        insertSystemInfo();
        insertRepositoryTypes();
    }

    private void insertSystemInfo() {
        SystemInfo systemInfo = systDao.getSystemInfo();
        if (systemInfo == null) {
            systemInfo = new SystemInfo();
            systemInfo.setId(1l);
            systemInfo.setServerVersion(LivingDocServer.VERSION);
            systDao.store(systemInfo);
        }

        if (StringUtils.isEmpty(systemInfo.getServerVersion())) {
            systemInfo.setServerVersion(DEFAULT_VERSION);
            systDao.store(systemInfo);
        }
    }

    private void insertRepositoryTypes() {
        if (repoDao.getTypeByName("CONFLUENCE") == null) {
            RepositoryType type = RepositoryType.newInstance("CONFLUENCE");
            type.setDocumentUrlFormat("%s/%s");
            type.setTestUrlFormat(null);
            type.setClassName(AtlassianRepository.class.getName());
            repoDao.create(type);
        }
        if (repoDao.getTypeByName("FILE") == null) {
            RepositoryType type = RepositoryType.newInstance("FILE");
            type.setDocumentUrlFormat("%s%s");
            type.setTestUrlFormat("%s%s");
            type.setClassName(FileSystemRepository.class.getName());

            repoDao.create(type);
        }
    }
}
