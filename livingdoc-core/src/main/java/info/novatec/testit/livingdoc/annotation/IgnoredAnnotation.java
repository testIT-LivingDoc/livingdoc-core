package info.novatec.testit.livingdoc.annotation;

import info.novatec.testit.livingdoc.Text;
import info.novatec.testit.livingdoc.TypeConversion;


public class IgnoredAnnotation implements Annotation {
    private final Object actual;

    public IgnoredAnnotation(Object actual) {
        this.actual = actual;
    }

    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.GRAY);
        text.setContent(TypeConversion.toString(actual));
    }
}
