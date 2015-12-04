package info.novatec.testit.livingdoc.alias;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.runner.LoggingMonitor;
import info.novatec.testit.livingdoc.runner.RecorderMonitor;
import info.novatec.testit.livingdoc.runner.SpecificationRunner;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerBuilder;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerExecutor;
import info.novatec.testit.livingdoc.runner.SuiteRunner;
import info.novatec.testit.livingdoc.util.TestFileUtils;


/**
 * <b>Note:</b> Some aliases may be overridden by a specific test file under
 * src/test/resources.
 */
public class AliasAcceptanceTest {

    @Test
    public void shouldRunAcceptanceTestsForCore() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-alias").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);
        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        LoggingMonitor log = new LoggingMonitor();
        RecorderMonitor recorder = new RecorderMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).outputDirectory(outputDirectory).monitors(
            log, recorder).build(SuiteRunner.class);

        new SpecificationRunnerExecutor(runner).execute("/");

        System.out.println("\n" + recorder.getStatistics());
        System.out.println("Output generated in: " + outputDirectory);
        assertEquals(26, recorder.getStatistics().totalCount());
        assertEquals(21, recorder.getStatistics().rightCount());
        assertEquals(0, recorder.getStatistics().wrongCount());
        assertEquals(0, recorder.getStatistics().ignoredCount());
        assertEquals(5, recorder.getStatistics().exceptionCount());
    }

}
