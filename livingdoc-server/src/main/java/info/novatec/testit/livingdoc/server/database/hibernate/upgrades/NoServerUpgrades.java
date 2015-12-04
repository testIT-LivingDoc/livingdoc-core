package info.novatec.testit.livingdoc.server.database.hibernate.upgrades;

import info.novatec.testit.livingdoc.server.database.SessionService;


public class NoServerUpgrades implements ServerVersionUpgrader {
    private String currentVersion;

    public NoServerUpgrades(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public String upgradedTo() {
        return currentVersion;
    }

    @Override
    public void upgrade(SessionService service) {
        // NO UPGRADES TO PERFORM
    }
}
