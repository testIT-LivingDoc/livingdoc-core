package info.novatec.testit.livingdoc.runner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.report.FileReportGenerator;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.URIUtil;


public class DocumentRunnerTest {
    private File specificationDirectory = null;

    @Before
    public void setUp() {
        File baseDirectory = new File(URIUtil.decoded(getRessource("/")));

        specificationDirectory = findSpecsDirectory(baseDirectory.getParentFile());
        assertNotNull(specificationDirectory);
    }

    @Test
    public void testCallsBackOnSystemUnderDevelopmentOnStartAndEndOfDocumentExecution() {
        DocumentRunner runner = new DocumentRunner();

        FakeSystemUnderDevelopment sud = new FakeSystemUnderDevelopment();

        runner.setInterpreterSelector(LivingDocInterpreterSelector.class);
        runner.setSystemUnderDevelopment(sud);
        runner.setSections();
        runner.setReportGenerator(new FileReportGenerator(specificationDirectory));
        runner.setRepository(new FileSystemRepository(specificationDirectory));

        runner.run("specs/ABankSample.html", "specs/ABankSample.html.out");

        assertTrue(sud.onStartDocumentHasBeenCalled);
        assertTrue(sud.onEndDocumentHasBeenCalled);
    }

    private String getRessource(String name) {
        return DocumentRunnerTest.class.getResource(name).getPath();
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

    private static class FakeSystemUnderDevelopment extends DefaultSystemUnderDevelopment {
        public boolean onStartDocumentHasBeenCalled = false;
        public boolean onEndDocumentHasBeenCalled = false;

        @Override
        public void addImport(String packageName) {
            // No implementation needed.
        }

        @Override
        public Fixture getFixture(String name, String... params) throws Throwable {
            return null;
        }

        @Override
        public void onEndDocument(Document document) {
            onEndDocumentHasBeenCalled = true;
        }

        @Override
        public void onStartDocument(Document document) {
            onStartDocumentHasBeenCalled = true;
        }
    }
}
