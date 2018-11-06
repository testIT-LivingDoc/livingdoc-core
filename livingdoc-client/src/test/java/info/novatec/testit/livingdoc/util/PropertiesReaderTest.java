package info.novatec.testit.livingdoc.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sebastian Letzel
 */
public class PropertiesReaderTest {

    private PropertiesReader propertiesReader;

    @Before
    public void setUp() throws Exception {
        propertiesReader = new FakePropertiesReader("test.properties");
    }

    @Test
    public void shouldGetProperty() {
        String property = propertiesReader.getProperty("test");
        assertEquals("testproperty", property);
    }

    @Test
    public void shouldLoadDefaultProperty() {
        String defaultString = "default";

        String property = propertiesReader.getProperty("no-test", defaultString);

        assertEquals(property, defaultString);
    }

    @Test(expected = PropertiesReader.PropertyLoadException.class)
    public void shouldThrowExceptionOnMissingFile() {
        new FakePropertiesReader("nothing");
    }

    private class FakePropertiesReader extends PropertiesReader {
        public FakePropertiesReader(String propertyfile) {
            super(propertyfile);
        }
    }
}
