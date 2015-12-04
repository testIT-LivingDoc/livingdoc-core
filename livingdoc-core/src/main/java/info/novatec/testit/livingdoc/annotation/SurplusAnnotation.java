package info.novatec.testit.livingdoc.annotation;

import static info.novatec.testit.livingdoc.LivingDoc.$;

import info.novatec.testit.livingdoc.Text;


public class SurplusAnnotation implements Annotation {
    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.RED);
        text.setContent(message() + text.getContent());
    }

    private String message() {
        StringBuilder message = new StringBuilder();
        message.append("<em>").append($("surplus")).append("</em> ");
        return message.toString();
    }
}
