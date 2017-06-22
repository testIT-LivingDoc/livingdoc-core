package info.novatec.testit.livingdoc.agent.server;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.setPort(getPort());
                if (isSecured()) {
                    Ssl ssl = new Ssl();
                    ssl.setEnabled(true);
                    ssl.setKeyStore("keystore.jks");
                    ssl.setKeyPassword("admin123");
                    container.setSsl(ssl);

                }
            }
        };
    }

    protected int getPort() {
        return 56000;
    }

    protected boolean isSecured() {
        return false;
    }
}
