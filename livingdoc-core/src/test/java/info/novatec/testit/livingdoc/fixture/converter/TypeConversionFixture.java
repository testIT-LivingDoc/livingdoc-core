package info.novatec.testit.livingdoc.fixture.converter;

import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.converter.TypeConverter;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.NameUtils;


@FixtureClass
public class TypeConversionFixture {

    private Class< ? > type;

    public TypeConversionFixture() {
        LivingDoc.register(new TypeConversionFixtureConverter());
    }

    private static Class< ? > getClass(String typename) throws ClassNotFoundException {
        return ClassUtils.loadClass("info.novatec.testit.livingdoc.fixture.converter.TypeConversionFixture$" + NameUtils
            .toClassName(typename));
    }

    public String thatConvertingToUses(String typeName) throws ClassNotFoundException {
        TypeConverter converter = TypeConversion.converterForType(ClassUtils.loadClass(typeName));
        return converter.getClass().getSimpleName();
    }

    public void register(String converterName) throws ClassNotFoundException, InstantiationException,
        IllegalAccessException {
        Class< ? > clazz = ClassUtils.loadClass(converterName);
        LivingDoc.register(( TypeConverter ) clazz.newInstance());
    }

    public void unregisterLastAddedCustomConverter() {
        LivingDoc.unregisterLastAddedCustomConverter();
    }

    public void type(String typename) throws ClassNotFoundException {

        type = getClass(typename);
    }

    public String parseWith() {
        return String.valueOf(TypeConversion.parse("xcare", type));
    }

    public String toStringWith() throws Throwable {
        return TypeConversion.toString(type.newInstance());
    }

    public static class TypeConversionFixtureConverter implements TypeConverter {
        @Override
        public boolean canConvertTo(Class< ? > type) {
            return TypeWithConverterNoSelf.class.isAssignableFrom(type) || TypeWithConverterWithSelf.class.isAssignableFrom(
                type);
        }

        @Override
        public Object parse(String value, Class< ? > type) {
            return "converter.parse";
        }

        @Override
        public String toString(Object value) {
            return "converter.toString";
        }
    }

    public static class TypeNoConverterNoSelf {
        @Override
        public String toString() {
            return "class.toString";
        }
    }

    @FixtureClass
    @SuppressWarnings("unused")
    public static class TypeNoConverterWithSelf {
        private String toStringValue = "class.toString";

        public TypeNoConverterWithSelf() {
        }

        private TypeNoConverterWithSelf(String toStringValue) {
            this.toStringValue = toStringValue;
        }

        public static TypeNoConverterWithSelf parse(String value) {
            return new TypeNoConverterWithSelf("self.parse");
        }

        public static TypeNoConverterWithSelf valueOf(String value) {
            return new TypeNoConverterWithSelf("self.valueOf");
        }

        public static String toString(TypeNoConverterWithSelf value) {
            return "self.toString";
        }

        @Override
        public String toString() {
            return toStringValue;
        }
    }

    @FixtureClass
    @SuppressWarnings("unused")
    public static class TypeNoConverterWithSelfByValueOf {
        private String toStringValue = "class.toString";

        public TypeNoConverterWithSelfByValueOf() {
        }

        private TypeNoConverterWithSelfByValueOf(String toStringValue) {
            this.toStringValue = toStringValue;
        }

        public static TypeNoConverterWithSelfByValueOf valueOf(String value) {
            return new TypeNoConverterWithSelfByValueOf("self.valueOf");
        }

        public static String toString(TypeNoConverterWithSelf value) {
            return "self.toString";
        }

        @Override
        public String toString() {
            return toStringValue;
        }
    }

    public static class TypeWithConverterNoSelf {
        @Override
        public String toString() {
            return "class.toString";
        }
    }

    @SuppressWarnings("unused")
    public static class TypeWithConverterWithSelf {
        private String toStringValue = "class.toString";

        public TypeWithConverterWithSelf() {
        }

        private TypeWithConverterWithSelf(String toStringValue) {
            this.toStringValue = toStringValue;
        }

        public static TypeWithConverterWithSelf parse(String value) {
            return new TypeWithConverterWithSelf("self.parse");
        }

        public static TypeWithConverterWithSelf valueOf(String value) {
            return new TypeWithConverterWithSelf("self.valueOf");
        }

        public static String toString(TypeWithConverterWithSelf value) {
            return "self.toString";
        }

        @Override
        public String toString() {
            return toStringValue;
        }
    }
}
