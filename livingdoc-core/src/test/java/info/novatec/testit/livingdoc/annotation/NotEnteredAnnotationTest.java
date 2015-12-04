package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class NotEnteredAnnotationTest {

    @Test
    public void testColorsInRedAndDisplaysActualValueAsMissing() {
        FakeText text = new FakeText("text");

        NotEnteredAnnotation annotation = new NotEnteredAnnotation();
        annotation.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("<em>Not entered</em>", text.getContent());
    }
}
