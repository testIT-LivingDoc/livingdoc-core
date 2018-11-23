package info.novatec.testit.livingdoc.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author Sebastian Letzel
 */
public class EncryptionUtil {

    private static final EncryptionProperties properties = new EncryptionProperties();
    private final StandardPBEStringEncryptor encryptor;

    public EncryptionUtil() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(getPassPhraseFromProperty());
        encryptor.setAlgorithm(getAlgorithmFromProperty());
    }

    private String getAlgorithmFromProperty() {
        return properties.getProperty("algorithm", "PBEWithMD5AndTripleDES");
    }

    private String getPassPhraseFromProperty() {
        return properties.getProperty("secret-key");
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String encrypted) {
        return encryptor.decrypt(encrypted);
    }
}
