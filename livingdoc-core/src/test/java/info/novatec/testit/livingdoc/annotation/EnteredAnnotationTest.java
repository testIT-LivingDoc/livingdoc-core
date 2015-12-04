package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class EnteredAnnotationTest {
    @Test
    public void testColorsInRedAndDisplaysActualValueAsMissing() {
        FakeText text = new FakeText("text");

        EnteredAnnotation annotation = new EnteredAnnotation();
        annotation.writeDown(text);
        assertEquals(Colors.GREEN, text.getStyle("background-color"));
        assertEquals("<em>Entered</em>", text.getContent());
    }
}
