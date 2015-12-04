package info.novatec.testit.livingdoc.report;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;


public class FileReportGeneratorTest {
    private static String tempDirectory = System.getProperty("java.io.tmpdir");

    @Test
    public void testEscapeDoubleQuoteCharactersWhenWrittingFile() {
        try {
            PlainReport report = new PlainReport(
                "LivingDoc Confluence_LIVINGDOC_Action Access Resolution \"quote\"_report.xml");
            FileReportGenerator reportGenerator = new FileReportGenerator(new File(tempDirectory));
            reportGenerator.closeReport(report);
        } catch (Exception e) {
            fail("Unable to write the report on file system : " + e.getMessage());
        }
    }
}
