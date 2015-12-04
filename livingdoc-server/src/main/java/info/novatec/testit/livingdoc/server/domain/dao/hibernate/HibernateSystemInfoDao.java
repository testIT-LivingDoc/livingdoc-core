package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;

import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.SystemInfo;
import info.novatec.testit.livingdoc.server.domain.dao.SystemInfoDao;


public class HibernateSystemInfoDao implements SystemInfoDao {
    private static final long SYSTEM_INFO = 1l;
    private SessionService sessionService;

    public HibernateSystemInfoDao(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public SystemInfo getSystemInfo() {
        final Criteria crit = sessionService.getSession().createCriteria(SystemInfo.class);
        crit.add(Property.forName("id").eq(SYSTEM_INFO));
        SystemInfo systemInfo = ( SystemInfo ) crit.uniqueResult();
        // crit.addOrder(Order.desc("id"));
        // List list = crit.list();
        // SystemInfo systemInfo = null;
        // if (list.size() > 0) {
        // systemInfo = (SystemInfo) list.get(0);
        // }
        HibernateLazyInitializer.init(systemInfo);
        return systemInfo;
    }

    @Override
    public void store(SystemInfo systemInfo) {
        sessionService.getSession().saveOrUpdate(systemInfo);
    }
}
