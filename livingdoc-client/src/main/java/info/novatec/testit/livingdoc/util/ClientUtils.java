package info.novatec.testit.livingdoc.util;

import java.util.List;
import java.util.Vector;


public class ClientUtils {

    public static List<Object> vectorizeDeep(Object[] array) {

        List<Object> resultVector = new Vector<Object>(array.length);
        for (int i = 0; i < array.length; i ++ ) {
            if (array[i].getClass().isArray()) {
                resultVector.add(vectorizeDeep(( Object[] ) array[i]));
            } else {
                resultVector.add(array[i]);
            }
        }
        return resultVector;

    }
}
