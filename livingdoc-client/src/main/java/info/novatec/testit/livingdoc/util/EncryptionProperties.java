package info.novatec.testit.livingdoc.util;

/**
 * @author Sebastian Letzel
 */
public class EncryptionProperties extends PropertiesReader {

    private static final String ENCRYPTION_PROPERTIES_FILENAME = "encryption.properties";

    public EncryptionProperties() {
        super(ENCRYPTION_PROPERTIES_FILENAME);
    }

    /**
     * Prevent changing of encryption properties.
     */
    @Override
    public synchronized Object setProperty(String key, String value) {
        return getProperty(key);
    }
}
