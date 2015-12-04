package info.novatec.testit.livingdoc.annotation;

import static info.novatec.testit.livingdoc.LivingDoc.$;

import info.novatec.testit.livingdoc.Text;


public class NotEnteredAnnotation implements Annotation {
    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.RED);
        text.setContent(message());
    }

    private String message() {
        StringBuilder message = new StringBuilder();
        message.append("<em>").append($("not_entered")).append("</em>");
        return message.toString();
    }
}
