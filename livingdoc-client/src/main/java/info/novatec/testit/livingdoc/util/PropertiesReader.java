package info.novatec.testit.livingdoc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Sebastian Letzel
 */
public abstract class PropertiesReader extends Properties {

    public PropertiesReader(String propertiesFileName) {
        readPropertiesFile(propertiesFileName);
    }

    private void readPropertiesFile(String propertiesFileName) {
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            load(input);
        } catch (IOException e) {
            throw new PropertyLoadException(e, propertiesFileName);
        } catch (NullPointerException e) {
            throw new PropertyLoadException(e, propertiesFileName);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class PropertyLoadException extends IllegalArgumentException {

        PropertyLoadException(Exception e, String fileName) {
            super("Error while loading Properties: " + fileName, e);
        }
    }
}
