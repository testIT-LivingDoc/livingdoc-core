package info.novatec.testit.livingdoc.annotation;

import info.novatec.testit.livingdoc.Text;


public class RightAnnotation implements Annotation {
    @Override
    public void writeDown(Text text) {
        text.setStyle(Styles.BACKGROUND_COLOR, Colors.GREEN);
    }
}
