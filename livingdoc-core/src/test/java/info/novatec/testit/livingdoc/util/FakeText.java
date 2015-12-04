package info.novatec.testit.livingdoc.util;

import java.util.HashMap;
import java.util.Map;

import info.novatec.testit.livingdoc.Text;


public class FakeText implements Text {
    private String text;
    private Map<String, String> styles = new HashMap<String, String>();

    public FakeText(String text) {
        this.text = text;
    }

    @Override
    public void setStyle(String property, String value) {
        styles.put(property, value);
    }

    @Override
    public String getStyle(String property) {
        return styles.get(property);
    }

    @Override
    public void setContent(String content) {
        text = content;
    }

    @Override
    public String getContent() {
        return text;
    }
}
