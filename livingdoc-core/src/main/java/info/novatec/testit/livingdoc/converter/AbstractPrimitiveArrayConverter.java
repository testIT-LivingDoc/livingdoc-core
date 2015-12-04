package info.novatec.testit.livingdoc.converter;

import info.novatec.testit.livingdoc.TypeConversion;


public class AbstractPrimitiveArrayConverter extends ArrayConverter {
    @Override
    public boolean canConvertTo(Class< ? > type) {
        return isArray(type) && TypeConversion.supports(type.getComponentType()) && type.getComponentType().isPrimitive();
    }
}
