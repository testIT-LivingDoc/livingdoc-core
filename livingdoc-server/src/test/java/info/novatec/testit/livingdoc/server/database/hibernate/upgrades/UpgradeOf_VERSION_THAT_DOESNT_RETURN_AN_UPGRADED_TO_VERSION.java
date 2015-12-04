package info.novatec.testit.livingdoc.server.database.hibernate.upgrades;

import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemInfoDao;


public class UpgradeOf_VERSION_THAT_DOESNT_RETURN_AN_UPGRADED_TO_VERSION implements ServerVersionUpgrader {

    @Override
    public String upgradedTo() {
        return null;
    }

    @Override
    public void upgrade(SessionService service) {
        SystemInfoDao systemDao = new HibernateSystemInfoDao(service);
        systemDao.getSystemInfo();
    }
}
