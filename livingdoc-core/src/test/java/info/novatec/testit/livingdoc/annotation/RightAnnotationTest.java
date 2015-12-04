package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class RightAnnotationTest {
    private FakeText text;
    private Annotation annotation;

    @Before
    public void setUp() throws Exception {
        text = new FakeText("expected");
        annotation = new RightAnnotation();
    }

    @Test
    public void testColorsExampleGreenAndDoesNotChangeText() {
        annotation.writeDown(text);
        assertEquals(Colors.GREEN, text.getStyle("background-color"));
        assertEquals("expected", text.getContent());
    }
}
