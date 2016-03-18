package info.novatec.testit.livingdoc.agent.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComandLineHelper {

    private static final Logger log = LogManager.getLogger(ComandLineHelper.class);

    private static String SECURED = "-secured";
    private static String PORT = "-port";
    private static String KEYSTORE = "-keystore";
    private static String CONFIG = "-config";

    private String args[];

    public ComandLineHelper(String args[]) {
        this.args = args.clone();
    }

    public String getConfig() {
        return getParameterValue(CONFIG);
    }

    public boolean isSecured() {
        for (String arg : args) {
            if (SECURED.equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public Integer getPort(int defaultPort) {
        String port = getParameterValue(PORT);

        try {
            if (port != null) {
                return Integer.parseInt(port);
            }
        } catch (NumberFormatException ex) {
            // No implementation needed.
            log.info("LivingDoc Remote Agent's PORT is not an integer, it was: ", PORT);
        }

        return defaultPort;
    }

    public String getKeyStore() {
        return getParameterValue(KEYSTORE);
    }

    private String getParameterValue(String parameter) {
        if (args == null) {
            return null;
        }

        for (int i = 0; i < args.length; i ++ ) {
            if (parameter.equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
                return null;
            }
        }

        return null;
    }
}
