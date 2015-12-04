package info.novatec.testit.livingdoc.server.database.configuration;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.server.configuration.ServerConfiguration;


public class ServerConfigurationTest {
    private ServerConfiguration config;

    @Before
    public void setUp() throws Exception {
        URL url = ServerConfigurationTest.class.getResource("configuration-test.xml");
        config = ServerConfiguration.load(url);
    }

    @Test
    public void testThatPropertiesAreProperlyLoaded() {
        assertEquals("value1", config.getProperties().getProperty("property1"));
        assertEquals("value2", config.getProperties().getProperty("property2"));
        assertEquals("some/url/with/slashes", config.getProperties().getProperty("property3"));
    }
}
