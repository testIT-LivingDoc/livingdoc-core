package info.novatec.testit.livingdoc.report;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;

import info.novatec.testit.livingdoc.TextExample;
import info.novatec.testit.livingdoc.document.Document;


public class PlainReportTest {

    @Test
    public void testCanPrintGeneratedReport() throws Exception {

        StringWriter out = new StringWriter();
        PlainReport report = new PlainReport("test");

        String lorem = "Lorem ipsum";

        report.generate(Document.text(new TextExample(lorem)));
        report.printTo(out);

        assertEquals(lorem, out.toString());
    }
}
