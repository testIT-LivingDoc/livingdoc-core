package info.novatec.testit.livingdoc.runner;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ClassUtils;


/**
 * Note: Since we have no getters in the runners, there is no proper way to
 * check if the property was set.
 */
public class SpecificationRunnerBuilderTest {

    private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));
    private final String DUMMY_REPO = FileSystemRepository.class.getName() + ";" + TEMP_DIR;

    @Test
    public void shouldBuildSuiteRunnerWithMandatoryParameters() throws Exception {
        SpecificationRunnerBuilder builder = new SpecificationRunnerBuilder(DUMMY_REPO);

        SuiteRunner suiteRunner = builder.build(SuiteRunner.class);

        Assert.assertNotNull(suiteRunner);
    }

    @Test
    public void shouldBuildDocumentRunnerWithMandatoryParameters() throws Exception {
        SpecificationRunnerBuilder builder = new SpecificationRunnerBuilder(DUMMY_REPO);

        DocumentRunner documentRunner = builder.build(DocumentRunner.class);
        Assert.assertNotNull(documentRunner);

    }

    @Test
    public void shouldBuildRunnerWithExistingOutputDirectory() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).outputDirectory(TEMP_DIR).build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithCustomMonitor() throws Exception {
        LoggingMonitor monitor = new LoggingMonitor();

        new SpecificationRunnerBuilder(DUMMY_REPO).monitors(monitor).build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithSpecificReport() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).report(XmlReport.class.getName()).build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithSpecificSections() throws Exception {
        String[] sections = new String[] { "section1", "section2" };

        new SpecificationRunnerBuilder(DUMMY_REPO).sections(sections).build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithSpecificSection() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).withSection("section1").build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithSystemUnderDevelopment() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).systemUnderDevelopment(DefaultSystemUnderDevelopment.class.getName())
            .build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithLazyMode() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).lazy(true).build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithCustomInterpreterSelector() throws Exception {
        new SpecificationRunnerBuilder(DUMMY_REPO).interpreterSelector(LivingDocInterpreterSelector.class.getName()).build(
            DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithExternalSystemUnderDevelopmentAndCustomClassLoader() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File specsFolder = new File(specsFolderUri);

        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        new SpecificationRunnerBuilder(DUMMY_REPO).classLoader(customClassLoader).systemUnderDevelopment(
            "info.novatec.testit.livingdoc.external.fixture.MyCustomSystemUnderDevelopment").build(DocumentRunner.class);
    }

    @Test
    public void shouldBuildRunnerWithExternalInterpreterSelectorAndCustomClassLoader() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File specsFolder = new File(specsFolderUri);

        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        new SpecificationRunnerBuilder(DUMMY_REPO).classLoader(customClassLoader).interpreterSelector(
            "info.novatec.testit.livingdoc.external.fixture.MyCustomInterpreterSelector").build(DocumentRunner.class);
    }

}
