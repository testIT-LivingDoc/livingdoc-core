package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.util.FakeText;


public class WrongAnnotationTest {
    private FakeText text;
    private WrongAnnotation wrong;

    @Before
    public void setUp() throws Exception {
        text = new FakeText("expected");
        wrong = new WrongAnnotation();
    }

    @Test
    public void testColorsInRedAndDoNotReplaceTextByDefault() {
        wrong.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("expected", text.getContent());
    }

    @Test
    public void testCanReplaceTextWithExpectationAndActual() {
        wrong.giveDetails(ShouldBe.equal("expected"), "actual");
        wrong.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("<b>Expected:</b> expected <b>Received:</b> actual", text.getContent());
    }
}
