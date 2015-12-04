package info.novatec.testit.livingdoc.server.database.hibernate.upgrades;

import info.novatec.testit.livingdoc.server.database.SessionService;


public interface ServerVersionUpgrader {
    public void upgrade(SessionService service) throws Exception;

    public String upgradedTo();
}
