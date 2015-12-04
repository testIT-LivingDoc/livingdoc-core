package info.novatec.testit.livingdoc.agent.server;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.secure.SecureWebServer;
import org.apache.xmlrpc.secure.SecurityTool;


public class Agent {
    public static final String REMOTE_AGENT_HANDLER_NAME = "livingdoc-agent1";

    private static final Logger log = LogManager.getLogger(Agent.class);

    private static WebServer webServer;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Shutting down LivingDoc Remote Agent...");
                shutdown();
            }
        });

        try {
            AgentConfiguration configuration = new AgentConfiguration(args);

            if (configuration.isSecured()) {
                SecurityTool.setKeyStore(configuration.getKeyStore());
                SecurityTool.setKeyStorePassword(configuration.getKeyStorePassword() == null ? askPassword() : configuration
                    .getKeyStorePassword());

                webServer = new SecureWebServer(configuration.getPort());
            } else {
                webServer = new WebServer(configuration.getPort());
            }

            Service service = new ServiceImpl();
            webServer.addHandler(REMOTE_AGENT_HANDLER_NAME, service);
            webServer.start();

            log.info("LivingDoc Remote Agent ({}) is listening for {} connections on port {}... ", REMOTE_AGENT_HANDLER_NAME,
                configuration.isSecured() ? "SSL " : "", configuration.getPort());

            log.info("File encoding : {}, Charset : {}", System.getProperty("file.encoding"), Charset.defaultCharset()
                .toString());
        } catch (Exception ex) {
            log.error("Error starting LivingDoc Remote Agent", ex);
        }
    }

    private static String askPassword() {
        return String.valueOf(System.console().readPassword("Enter keystore password : "));
    }

    public static void shutdown() {
        if (webServer != null) {
            webServer.shutdown();
            webServer = null;
        }
    }
}
