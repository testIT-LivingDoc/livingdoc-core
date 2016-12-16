package info.novatec.testit.livingdoc.fixture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.fixture.systemunderdevelopment.LivingDocTestSystemUnderDevelopment;
import info.novatec.testit.livingdoc.report.FileReportGenerator;
import info.novatec.testit.livingdoc.report.PlainReport;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.runner.CompositeSpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.runner.LoggingMonitor;
import info.novatec.testit.livingdoc.runner.RecorderMonitor;
import info.novatec.testit.livingdoc.runner.SuiteRunner;
import info.novatec.testit.livingdoc.util.TestFileUtils;


public class AcceptanceWithImportTest {

    private File outputDir, suiteoutput, specificationDirectory;
    private SuiteRunner runner = new SuiteRunner();
    private String input;
    private FileSystemRepository repo;
    private CompositeSpecificationRunnerMonitor monitors;
    private FileReportGenerator generator;
    private LoggingMonitor log = new LoggingMonitor();
    private RecorderMonitor recorder = new RecorderMonitor();

    @Before
    public void setup() throws UnsupportedEncodingException {
        outputDir = TestFileUtils.createTempReportsDirectory();
        specificationDirectory = new File(URLDecoder.decode(getResourcePath("/"), "UTF-8"));
        input = "/specs/WithImports";

        suiteoutput = new File(URLDecoder.decode(outputDir.getAbsolutePath(), "UTF-8") + "/Seeds");
        suiteoutput.mkdir();
        repo = new FileSystemRepository(specificationDirectory);
        generator = new FileReportGenerator(suiteoutput);
        setupGenerator(generator);
        monitors = new CompositeSpecificationRunnerMonitor();
        monitors.add(log);
        recorder = new RecorderMonitor();
        monitors.add(recorder);
    }

    private void setupGenerator(FileReportGenerator generator) {
        generator.adjustReportFilesExtensions(true);
        generator.setReportClass(PlainReport.class);
    }

    private String getResourcePath(String name) {
        return AcceptanceWithImportTest.class.getResource(name).getPath();
    }

    @Test
    public void shouldRunAcceptanceTestsForCore() {
        runner.setRepository(repo);
        runner.setSystemUnderDevelopment(new LivingDocTestSystemUnderDevelopment());
        runner.setReportGenerator(generator);
        runner.setInterpreterSelector(LivingDocInterpreterSelector.class);
        runner.setSections();
        runner.setMonitor(monitors);

        runner.run(input, suiteoutput.getAbsolutePath());
        System.out.println(outputDir);
        assertTrue(suiteoutput.isDirectory());
        assertEquals(4, suiteoutput.listFiles().length);
        assertEquals(83, recorder.getStatistics().totalCount());
        assertEquals(66, recorder.getStatistics().rightCount());
        assertEquals(0, recorder.getStatistics().wrongCount());
        assertEquals(17, recorder.getStatistics().ignoredCount());
        assertEquals(0, recorder.getStatistics().exceptionCount());
    }

}
