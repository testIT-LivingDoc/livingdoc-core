package info.novatec.testit.livingdoc.annotation;

import static info.novatec.testit.livingdoc.LivingDoc.$;

import info.novatec.testit.livingdoc.Text;
import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.expectation.Expectation;


public class WrongAnnotation implements Annotation {
    private Expectation expected;
    private Object actual;
    private boolean detailed;

    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.RED);
        if (detailed) {
            text.setContent(message());
        }
    }

    private String message() {
        StringBuilder message = new StringBuilder(19);
        message.append("<b>").append($("expected")).append(":</b> ");
        expected.describeTo(message);
        message.append(" <b>").append($("received")).append(":</b> ").append(TypeConversion.toString(actual));
        return message.toString();
    }

    public void giveDetails(Expectation paramExpected, Object paramActual) {
        this.expected = paramExpected;
        this.actual = paramActual;
        this.detailed = true;
    }

    public boolean isDetailed() {
        return detailed;
    }
}
