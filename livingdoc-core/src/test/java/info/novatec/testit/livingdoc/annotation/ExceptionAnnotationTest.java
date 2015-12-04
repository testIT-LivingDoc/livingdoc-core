package info.novatec.testit.livingdoc.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.util.FakeText;


public class ExceptionAnnotationTest {
    private FakeText text;

    @Before
    public void setUp() throws Exception {
        text = new FakeText("content");
    }

    @Test
    public void testColorsInYellowAndAddErrorBacktraceToText() {
        Throwable error = new RuntimeException("error message");
        error.setStackTrace(new StackTraceElement[] { new StackTraceElement("Class", "method", "Class.java", - 1) });

        ExceptionAnnotation exception = new ExceptionAnnotation(error);
        exception.writeDown(text);
        String expected = "content<hr/><pre><font size=\"-2\">" + error.toString()
            + "<br/>at Class.method(Class.java)</font></pre>";
        System.out.println(expected);
        System.out.println(text.getContent());
        assertEquals(expected, text.getContent());
    }
}
