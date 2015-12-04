package info.novatec.testit.livingdoc.server.database.hibernate.upgrades;

import info.novatec.testit.livingdoc.server.database.SessionService;


/**
 * Example of upgrade class.
 * 
 * @author JSC
 * 
 */
public class UpgradeOf_1_0_0_SNAPSHOT implements ServerVersionUpgrader {

    @Override
    public String upgradedTo() {
        return "1.0";
    }

    @Override
    public void upgrade(SessionService sessionService) {
        // NO IMPLEMENTATION
    }

}
