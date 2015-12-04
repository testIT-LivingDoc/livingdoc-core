package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class SkippedAnnotationTest {

    @Test
    public void testColorsInOrangeAndDisplaysActualValueAsSkipped() {
        FakeText text = new FakeText("text");

        SkippedAnnotation annotation = new SkippedAnnotation();
        annotation.writeDown(text);
        assertEquals(Colors.ORANGE, text.getStyle("background-color"));
        assertEquals("<em>Skipped</em>", text.getContent());
    }
}
