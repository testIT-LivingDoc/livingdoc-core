package info.novatec.testit.livingdoc.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.util.CollectionUtil;


public class PrimitiveIntArrayConverter extends AbstractPrimitiveArrayConverter {

    @Override
    public Object parse(String value, Class< ? > type) {
        String text = removeSquareBrackets(value);

        List<Object> values = new ArrayList<Object>();
        if (StringUtils.isBlank(text)) {
            return CollectionUtil.toArray(values, type.getComponentType());
        }

        String[] parts = text.split(separators);
        for (String part : parts) {
            values.add(TypeConversion.parse(part.trim(), type.getComponentType()));
        }

        return CollectionUtil.toPrimitiveIntArray(values);
    }

    @Override
    public String toString(Object value) {
        int[] array = ( int[] ) value;

        if (array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append(TypeConversion.toString(array[0]));
        if (array.length == 1) {
            return builder.toString();
        }

        for (int i = 1; i < array.length; i ++ ) {
            builder.append(", ").append(TypeConversion.toString(array[i]));
        }
        return builder.toString();
    }
}
