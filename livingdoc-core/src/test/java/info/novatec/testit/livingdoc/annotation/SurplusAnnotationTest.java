package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class SurplusAnnotationTest {

    @Test
    public void testColorsInRedAndDisplaysGivenValueAsSurplus() {
        FakeText text = new FakeText("actual");

        SurplusAnnotation wrong = new SurplusAnnotation();
        wrong.writeDown(text);
        assertEquals(Colors.RED, text.getStyle("background-color"));
        assertEquals("<em>Surplus</em> actual", text.getContent());
    }
}
