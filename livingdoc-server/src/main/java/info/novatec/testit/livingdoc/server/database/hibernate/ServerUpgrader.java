package info.novatec.testit.livingdoc.server.database.hibernate;

import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.database.hibernate.upgrades.NoServerUpgrades;
import info.novatec.testit.livingdoc.server.database.hibernate.upgrades.ServerVersionUpgrader;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class ServerUpgrader {
    private final SessionService sessionService;
    private final SystemInfoDao systDao;
    private final SystemInfo systemInfo;

    public ServerUpgrader(SessionService sessionService) {
        this(sessionService, new HibernateSystemInfoDao(sessionService));
    }

    public ServerUpgrader(SessionService sessionService, SystemInfoDao systemInfoDao) {
        this.sessionService = sessionService;
        this.systDao = systemInfoDao;
        this.systemInfo = systDao.getSystemInfo();
    }

    public void upgradeTo(String version) throws Exception {
        String installedVersion = systemInfo.getServerVersion();
        if ( ! installedVersion.equals(version)) {
            ServerVersionUpgrader upgrader = versionUpgrader(installedVersion, version);
            upgrader.upgrade(sessionService);

            String newInstalledVersion = upgrader.upgradedTo() != null ? upgrader.upgradedTo() : version;
            systemInfo.setServerVersion(newInstalledVersion);
            upgradeTo(version);
        }

        systDao.store(systemInfo);
    }

    private ServerVersionUpgrader versionUpgrader(String fromVersion, String toVersion) throws InstantiationException,
        IllegalAccessException {
        String modifiedFromVersion = fromVersion.replaceAll("\\.", "_").replaceAll("\\-", "_");
        String className = "info.novatec.testit.livingdoc.server.database.hibernate.upgrades.UpgradeOf_"
            + modifiedFromVersion;
        try {
            return ( ServerVersionUpgrader ) Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            return new NoServerUpgrades(toVersion);
        }
    }
}
