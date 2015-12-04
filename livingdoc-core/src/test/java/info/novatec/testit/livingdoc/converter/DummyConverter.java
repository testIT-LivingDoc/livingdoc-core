package info.novatec.testit.livingdoc.converter;

public class DummyConverter extends AbstractTypeConverter {

    @Override
    protected Object doConvert(String value) {
        return new DummyType();
    }

    @Override
    public boolean canConvertTo(Class< ? > type) {
        return DummyType.class.isAssignableFrom(type);
    }

    public static class DummyType {
        // No implementation needed.
    }
}
