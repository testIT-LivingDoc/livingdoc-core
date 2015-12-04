package info.novatec.testit.livingdoc.annotation;

import static info.novatec.testit.livingdoc.LivingDoc.$;

import info.novatec.testit.livingdoc.Text;


public class EnteredAnnotation implements Annotation {
    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.GREEN);
        text.setContent(message());
    }

    private String message() {
        StringBuilder message = new StringBuilder();
        message.append("<em>").append($("entered")).append("</em>");
        return message.toString();
    }
}
