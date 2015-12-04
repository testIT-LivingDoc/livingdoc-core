package info.novatec.testit.livingdoc.server.database.hibernate.upgrades;

import info.novatec.testit.livingdoc.server.database.SessionService;


public class UpgradeOf_VERSION_THAT_WILL_CAUSE_A_FAILURE implements ServerVersionUpgrader {

    @Override
    public String upgradedTo() {
        return "VERSION.THAT.NEEDS.MORE.UPGRADES";
    }

    @Override
    public void upgrade(SessionService service) throws Exception {
        throw new Exception("FAILURE");
    }
}
