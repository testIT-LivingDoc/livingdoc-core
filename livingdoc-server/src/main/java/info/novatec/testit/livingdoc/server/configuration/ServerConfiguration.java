package info.novatec.testit.livingdoc.server.configuration;

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class ServerConfiguration {
    private static String PROPERTIES_TAG = "properties";
    private static String PROPERTY_TAG = "property";
    private static String NAME_TAG = "name";

    private static ServerConfiguration config;

    private Document configDocument;
    private Properties properties = new DefaultServerProperties();

    private ServerConfiguration() {
    }

    public static ServerConfiguration load(URL url) throws DocumentException {
        config = new ServerConfiguration();
        config.loadConfig(url);
        return config;
    }

    public static ServerConfiguration instance() {
        if (config == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return config;
    }

    public Properties getProperties() {
        return properties;
    }

    private void loadConfig(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        configDocument = reader.read(url);
        loadProperties();
    }

    private void loadProperties() {
        Iterator< ? > iter = configDocument.getRootElement().elementIterator(PROPERTIES_TAG);
        while (iter.hasNext()) {
            Element elementProperties = ( Element ) iter.next();
            for (Iterator< ? > iterProperty = elementProperties.elementIterator(PROPERTY_TAG); iterProperty.hasNext();) {
                Element elementProperty = ( Element ) iterProperty.next();
                Attribute attributeName = elementProperty.attribute(NAME_TAG);
                properties.put(getStringData(attributeName), getStringData(elementProperty));
            }
        }
    }

    private String getStringData(Element element) {
        String value = element.getStringValue();
        value = value.trim().replace('\\', '/');
        return value;
    }

    private String getStringData(Attribute element) {
        String value = element.getStringValue();
        value = value.trim().replace('\\', '/');
        return value;
    }
}
