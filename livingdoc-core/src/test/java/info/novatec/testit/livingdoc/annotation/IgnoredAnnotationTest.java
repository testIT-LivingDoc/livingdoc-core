package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class IgnoredAnnotationTest {

    @Test
    public void testColorsInGrayAndReplacesTextWithActualValue() {
        FakeText text = new FakeText("given");
        IgnoredAnnotation ignored = new IgnoredAnnotation("actual");
        ignored.writeDown(text);
        assertEquals(Colors.GRAY, text.getStyle("background-color"));
        assertEquals("actual", text.getContent());
    }
}
