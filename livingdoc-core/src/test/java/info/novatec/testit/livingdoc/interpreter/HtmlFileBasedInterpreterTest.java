package info.novatec.testit.livingdoc.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.report.FileReportGenerator;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.runner.DocumentRunner;
import info.novatec.testit.livingdoc.runner.RecorderMonitor;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.URIUtil;


public class HtmlFileBasedInterpreterTest {
    private File specificationDirectory = null;

    @Before
    public void setUp() {
        File baseDirectory = new File(URIUtil.decoded(getRessource("/")));

        specificationDirectory = findSpecsDirectory(baseDirectory.getParentFile());
        assertNotNull(specificationDirectory);
    }

    @Test
    public void testWorkflowInterpreter(){
        testInterpreter(WorkflowInterpreter.class, 11);
    }
    @Test
    public void testDecisionTableInterpreter(){
        testInterpreter(DecisionTableInterpreter.class, 4);
    }
    private void testInterpreter(Class<? extends Interpreter> interpreterType, int expectedRightCount) {
        DocumentRunner runner = new DocumentRunner();
        RecorderMonitor recorderMonitor = new RecorderMonitor();
        runner.setInterpreterSelector(LivingDocInterpreterSelector.class);
        runner.setSystemUnderDevelopment(new DefaultSystemUnderDevelopment());
        runner.setSections();
        runner.setReportGenerator(new FileReportGenerator(specificationDirectory));
        runner.setRepository(new FileSystemRepository(specificationDirectory));
        runner.setMonitor(recorderMonitor);
        runner.run("specs/"+interpreterType.getSimpleName()+".html", "specs/"+interpreterType.getSimpleName()+".out.html");
        assertNotNull(recorderMonitor.getStatistics());
        assertEquals(0, recorderMonitor.getStatistics().exceptionCount());
        assertEquals(0,recorderMonitor.getStatistics().ignoredCount());
        assertEquals(0,recorderMonitor.getStatistics().wrongCount());
        assertEquals(expectedRightCount, recorderMonitor.getStatistics().rightCount());
         
    }

    private String getRessource(String name) {
        return HtmlFileBasedInterpreterTest.class.getResource(name).getPath();
    }

    private File findSpecsDirectory(File root) {
        String[] names = new String[] { "classes", "test-classes" };

        for (String name : names) {
            File dir = new File(root, name);

            if (new File(dir, "specs").exists()) {
                return dir;
            }
        }

        return null;
    }

   
}
