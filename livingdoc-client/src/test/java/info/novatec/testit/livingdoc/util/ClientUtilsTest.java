package info.novatec.testit.livingdoc.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author Miguel Ruiz
 */
public class ClientUtilsTest {

    private static final String SEPARATOR = ":";
    private static final String[] CREDENTIALS = new String[] {"user", "pass"};
    private static final String BASE64 = "dXNlcjpwYXNz";

     @Test
    public void encodeToBase64Test() throws UnsupportedEncodingException {
         String actual = ClientUtils.encodeToBase64(SEPARATOR, CREDENTIALS);
         Assert.assertEquals(BASE64, actual);
    }

    @Test
    public void encodeToBase64TestSeparatorNull() throws UnsupportedEncodingException {
        String actual = ClientUtils.encodeToBase64(null,CREDENTIALS);
        Assert.assertEquals("dXNlcnBhc3M=", actual);
    }

    @Test
    public void encodeToBase64TestSeparatorOneElement() throws UnsupportedEncodingException {
        String actual = ClientUtils.encodeToBase64(SEPARATOR, CREDENTIALS[0]);
        Assert.assertEquals("dXNlcg==", actual);
    }

    @Test
    public void decodeFromBase64Test() throws UnsupportedEncodingException {
         String[] actual = ClientUtils.decodeFromBase64(BASE64, SEPARATOR);
         Assert.assertArrayEquals(CREDENTIALS, actual);
    }

    @Test(expected = AssertionError.class)
    public void decodeFromBase64TestSeparatorNull() throws UnsupportedEncodingException {
        String[] actual = ClientUtils.decodeFromBase64(BASE64, null);
        Assert.assertArrayEquals(CREDENTIALS, actual);
    }
}
