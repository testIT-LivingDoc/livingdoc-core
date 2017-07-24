package info.novatec.testit.livingdoc.agent.server.configuration;

import info.novatec.testit.livingdoc.agent.server.*;
import org.apache.catalina.connector.*;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.*;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.context.embedded.tomcat.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.context.event.*;

import java.io.*;
import java.util.concurrent.*;

@Configuration
public class ServerConfiguration {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.setPort(getPort());
                if (isSecured()) {
                    try {
                        Ssl ssl = new Ssl();
                        ssl.setEnabled(true);
                        ssl.setKeyStore(AgentConfiguration.getKeyStore());
                        ssl.setKeyPassword(AgentConfiguration.getKeyStorePassword());
                        container.setSsl(ssl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public EmbeddedServletContainerCustomizer tomcatCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    ((TomcatEmbeddedServletContainerFactory) container)
                            .addConnectorCustomizers(gracefulShutdown());
                }

            }
        };
    }


    private static class GracefulShutdown implements TomcatConnectorCustomizer,
            ApplicationListener<ContextClosedEvent> {

        private volatile Connector connector;
        private static final Logger log = LoggerFactory.getLogger(ServerConfiguration.class);

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            this.connector.pause();
            log.info("Shutting Down LivingDoc Remote Agent");
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                        log.warn("Tomcat thread pool did not shut down gracefully within "
                                + "30 seconds. Proceeding with forceful shutdown");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }


    protected int getPort() {
        return AgentConfiguration.getPort();
    }

    protected boolean isSecured() {
        return AgentConfiguration.isSecured();
    }
}
