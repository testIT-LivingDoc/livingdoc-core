package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class MissingAnnotationTest {

    @Test
    public void testColorsInRedAndDisplaysActualValueAsMissing() {
        FakeText text = new FakeText("given");

        MissingAnnotation wrong = new MissingAnnotation();
        wrong.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("<em>Missing</em> given", text.getContent());
    }
}
