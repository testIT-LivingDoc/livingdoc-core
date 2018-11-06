package info.novatec.testit.livingdoc.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Sebastian Letzel
 */
public class EncryptionUtilTest {

    private EncryptionUtil cut;
    private String INPUT = "1234abcd";

    @Before
    public void setUp() {
        cut = new EncryptionUtil();
    }

    @Test
    public void shouldEncrypt() {
        String encrypted = cut.encrypt(INPUT);
        assertNotEquals(encrypted, INPUT);
    }

    @Test
    public void shouldDecrypt() {
        String encrypted = cut.encrypt(INPUT);
        String decrypted = cut.decrypt(encrypted);
        assertEquals(decrypted, INPUT);
    }

    @Test
    public void shouldEncryptToDifferentStringsWithSameDecrypt() {
        String encrypt1 = cut.encrypt(INPUT);
        String encrypt2 = cut.encrypt(INPUT);

        String decrypt1 = cut.decrypt(encrypt1);
        String decrypt2 = cut.decrypt(encrypt2);

        assertNotEquals(encrypt1, encrypt2);
        assertEquals(decrypt1, decrypt2);
    }
}
