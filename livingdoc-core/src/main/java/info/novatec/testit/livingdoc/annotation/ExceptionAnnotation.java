package info.novatec.testit.livingdoc.annotation;

import info.novatec.testit.livingdoc.Text;
import info.novatec.testit.livingdoc.util.ExceptionUtils;


public class ExceptionAnnotation implements Annotation {
    private final Throwable error;

    public ExceptionAnnotation(Throwable error) {
        this.error = error;
    }

    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.YELLOW);
        text.setContent(text.getContent() + "<hr/><pre><font size=\"-2\">" + ExceptionUtils.stackTrace(error, "<br/>", 10)
            + "</font></pre>");
    }

    @Override
    public String toString() {
        return ExceptionUtils.stackTrace(error, "\n", 10);
    }
}
