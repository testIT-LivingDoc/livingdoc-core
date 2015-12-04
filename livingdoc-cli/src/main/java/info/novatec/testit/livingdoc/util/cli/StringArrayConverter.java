package info.novatec.testit.livingdoc.util.cli;

import org.apache.commons.lang3.StringUtils;


public class StringArrayConverter implements Converter<String[]> {
    private String separators;

    public StringArrayConverter(String separators) {
        this.separators = separators;
    }

    @Override
    public String[] convert(String value) {
        return StringUtils.isBlank(value) ? new String[0] : value.split(separators);
    }
}
