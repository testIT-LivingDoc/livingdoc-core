package info.novatec.testit.livingdoc.util.cli;

import java.io.File;


public class FileConverter implements Converter<Object> {
    @Override
    public Object convert(String value) {
        return new File(value);
    }
}
