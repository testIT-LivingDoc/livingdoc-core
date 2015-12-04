package info.novatec.testit.livingdoc.fixture;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.runner.LoggingMonitor;
import info.novatec.testit.livingdoc.runner.RecorderMonitor;
import info.novatec.testit.livingdoc.runner.SpecificationRunner;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerBuilder;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerExecutor;
import info.novatec.testit.livingdoc.runner.SuiteRunner;
import info.novatec.testit.livingdoc.util.TestFileUtils;


public class AcceptanceWithoutImportTest {

    // Testsetting Parameters
    private final String repo = FileSystemRepository.class.getName();
    private final String inputDir = "/specs/NoImports";

    // Hardcoded variables
    private static File outputDir;
    private SpecificationRunner runner;
    private RecorderMonitor recorderMonitor = new RecorderMonitor();
    private LoggingMonitor loggingMonitor = new LoggingMonitor();
    private Statistics statistics;

    @Before
    public void setup() throws Exception {
        outputDir = TestFileUtils.createTempReportsDirectory();
        runner = new SpecificationRunnerBuilder(getRepository()).outputDirectory(outputDir).monitors(recorderMonitor,
            loggingMonitor).build(SuiteRunner.class);
    }

    @Test
    public void shouldRunSpecWithoutImport() {
        new SpecificationRunnerExecutor(runner).execute("/");

        statistics = recorderMonitor.getStatistics();
        System.out.println(outputDir);
        assertEquals(23, outputDir.listFiles().length);
        assertEquals(0, statistics.exceptionCount());
        assertEquals(13, statistics.ignoredCount());
        assertEquals(367, statistics.rightCount());
        assertEquals(0, statistics.wrongCount());
        assertEquals(0, statistics.exceptionCount());
    }

    private String getRepository() throws URISyntaxException {
        URI specsFolder = getClass().getResource(inputDir).toURI();
        File specs = new File(specsFolder);

        return repo + ";" + specs;
    }
}
