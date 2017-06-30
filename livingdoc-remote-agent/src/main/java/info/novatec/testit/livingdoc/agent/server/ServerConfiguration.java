package info.novatec.testit.livingdoc.agent.server;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

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

    protected int getPort(){return AgentConfiguration.getPort();}

    protected boolean isSecured() {
        return AgentConfiguration.isSecured();
    }
}
