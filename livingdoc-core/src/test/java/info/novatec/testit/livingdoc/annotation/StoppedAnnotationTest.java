package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class StoppedAnnotationTest {

    @Test
    public void testColorsInRedAndDisplaysActualValueAsStopped() {
        FakeText text = new FakeText("text");

        StoppedAnnotation annotation = new StoppedAnnotation();
        annotation.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("<em>Stopped</em>", text.getContent());
    }
}
