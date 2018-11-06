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
    public void encrypt() {
        String encrypted = cut.encrypt(INPUT);
        assertNotEquals(encrypted, INPUT);
    }

    @Test
    public void decrypt() {
        String encrypted = cut.encrypt(INPUT);
        String decrypted = cut.decrypt(encrypted);
        assertEquals(decrypted, INPUT);
    }
}
