package info.novatec.testit.livingdoc.agent.server;

import org.apache.logging.log4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import java.io.*;
import java.nio.charset.*;

@SpringBootApplication
public class Agent {
    public static final String REMOTE_AGENT_HANDLER_NAME = "livingdoc-agent1";

    private static final Logger log = LogManager.getLogger(Agent.class);

    public static void main(String[] args) {
        try {
            AgentConfiguration configuration = new AgentConfiguration(args);

            if (configuration.isSecured() && configuration.getKeyStorePassword() == null) {
                configuration.setKeyStorePassword(askPassword());
            }

            SpringApplication.run(Agent.class, args);

            log.info("LivingDoc Remote Agent ({}) is listening for {} connections on port {}... ", REMOTE_AGENT_HANDLER_NAME,
                    configuration.isSecured() ? "SSL " : "", configuration.getPort());

            log.info("File encoding : {}, Charset : {}", System.getProperty("file.encoding"), Charset.defaultCharset()
                    .toString());
        } catch (IOException ex) {
            log.error("Error starting LivingDoc Remote Agent", ex);
        }
    }

    private static String askPassword() {
        return String.valueOf(System.console().readPassword("Enter keystore password : "));
    }
}
