package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;


public class ExceptionImposterTest {
    private Exception original;

    @Test
    public void testShouldNotImposterizeUncheckedExceptions() {
        original = new RuntimeException();
        assertSame(original, ExceptionImposter.imposterize(original));
    }

    @Test
    public void testShouldImposterizeCheckedExceptionsAndKeepAReference() {
        original = new Exception();
        RuntimeException imposter = ExceptionImposter.imposterize(original);
        assertTrue(imposter instanceof ExceptionImposter);
        assertSame(original, ( ( ExceptionImposter ) imposter ).getRealException());
    }

    @Test
    public void testShouldMimicImposterizedExceptionToStringOutput() {
        original = new Exception("Detail message");
        RuntimeException imposter = ExceptionImposter.imposterize(original);
        assertEquals(original.toString(), imposter.toString());
    }

    @Test
    public void testShouldCopyImposterizedExceptionStackTrace() {
        original = new Exception("Detail message");
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize(original);
        assertTrue(Arrays.toString(imposter.getStackTrace()), Arrays.equals(original.getStackTrace(), imposter
            .getStackTrace()));
    }

    @Test
    public void testShouldMimicImposterizedExceptionStackTraceOutput() {
        original = new Exception("Detail message");
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize(original);
        assertEquals(captureStackTrace(original), captureStackTrace(imposter));
    }

    private String captureStackTrace(Exception exception) {
        StringWriter capture = new StringWriter();
        exception.printStackTrace(new PrintWriter(capture));
        capture.flush();
        return capture.toString();
    }
}
