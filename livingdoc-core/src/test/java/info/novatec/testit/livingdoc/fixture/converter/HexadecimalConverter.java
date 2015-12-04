package info.novatec.testit.livingdoc.fixture.converter;

import info.novatec.testit.livingdoc.converter.IntegerConverter;


public class HexadecimalConverter extends IntegerConverter {

    @Override
    protected String doToString(Object value) {
        return String.format("%x", value);
    }

}
