package info.novatec.testit.livingdoc.util;

import org.apache.commons.lang3.CharEncoding;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;


public class ClientUtils {

    private ClientUtils() {
        throw new IllegalAccessError("Utility class");
    }

    // TODO would be good to replace Vector by ArrayList
    public static Vector<Object> vectorizeDeep(Object[] array) {

        Vector<Object> resultVector = new Vector<Object>(array.length);
        for (Object anArray : array) {
            if (anArray.getClass().isArray()) {
                resultVector.add(vectorizeDeep((Object[]) anArray));
            } else {
                resultVector.add(anArray);
            }
        }
        return resultVector;

    }

    /**
     * @param separator
     * @param args
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeToBase64(final String separator, final String... args) throws UnsupportedEncodingException {

        // TODO Since Java 8 it can be used: java.util.Base64.getEncoder().encodeToString(string);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            sb.append(args[i]);
            if (i != (args.length - 1) && separator != null) {
                sb.append(separator);
            }
        }

        return DatatypeConverter.printBase64Binary((sb.toString()).getBytes(CharEncoding.UTF_8));

    }

    /**
     * Returns an array with decoded elements from a string in base64.
     *
     * @param base64String
     * @param separator
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String[] decodeFromBase64(final String base64String, final String separator) throws UnsupportedEncodingException {

        // TODO Since Java 8 it can be used: java.util.Base64.getDecoder().decode(base64String);

        byte[] bytes = DatatypeConverter.parseBase64Binary(base64String);
        String decodedAuthorization = new String(bytes, CharEncoding.UTF_8);
        return separator != null ? decodedAuthorization.split(separator) : new String[]{decodedAuthorization};
    }

}
