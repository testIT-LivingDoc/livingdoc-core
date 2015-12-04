package info.novatec.testit.livingdoc.runner;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.TestFileUtils;


public class SpecificationRunnerExecutorTest {

    @Test
    public void shouldExecuteDocumentSpecification() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).outputDirectory(outputDirectory).monitors(
            recorderMonitor, loggingMonitor).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("ACalculatorSample.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(6, statistics.totalCount());
        Assert.assertEquals(4, statistics.rightCount());
        Assert.assertEquals(1, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(1, statistics.exceptionCount());
    }

    @Test
    public void shouldExecuteSuiteSpecification() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/suite").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).outputDirectory(outputDirectory).monitors(
            recorderMonitor, loggingMonitor).build(SuiteRunner.class);

        new SpecificationRunnerExecutor(runner).execute("/");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(18, statistics.totalCount());
        Assert.assertEquals(12, statistics.rightCount());
        Assert.assertEquals(3, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(3, statistics.exceptionCount());
    }

    @Test
    public void shouldExecuteDocumentSpecificationWithExternalFixtureSource() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        // This jar contains a compiled calculator class. If you want to adjust
        // this calculator, you've to create a new jar by yourself.
        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).classLoader(customClassLoader)
            .systemUnderDevelopment(DefaultSystemUnderDevelopment.class.getName()).outputDirectory(outputDirectory).monitors(
                recorderMonitor, loggingMonitor).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("AExternalCalculatorSample.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(6, statistics.totalCount());
        Assert.assertEquals(4, statistics.rightCount());
        Assert.assertEquals(1, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(1, statistics.exceptionCount());
    }

    @Test
    public void shouldFailToExecuteDocumentSpecificationWithoutExternalFixtureSource() throws Exception {
        // This test does not pass a custom class loader, so the fixture should
        // not be found.

        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).systemUnderDevelopment(
            DefaultSystemUnderDevelopment.class.getName()).outputDirectory(outputDirectory).monitors(recorderMonitor,
                loggingMonitor).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("AExternalCalculatorSample.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(1, statistics.totalCount());
        Assert.assertEquals(0, statistics.rightCount());
        Assert.assertEquals(0, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(1, statistics.exceptionCount());
    }

    @Test
    public void shouldExecuteDocumentSpecificationWithExternalSystemUnderDevelopment() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        // This jar contains a compiled calculator class. If you want to adjust
        // this calculator, you've to create a new jar by yourself.
        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).classLoader(customClassLoader)
            .systemUnderDevelopment("info.novatec.testit.livingdoc.external.fixture.MyCustomSystemUnderDevelopment")
            .outputDirectory(outputDirectory).monitors(recorderMonitor, loggingMonitor).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("AExternalCalculatorSample.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(6, statistics.totalCount());
        Assert.assertEquals(4, statistics.rightCount());
        Assert.assertEquals(1, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(1, statistics.exceptionCount());
    }

    @Test
    public void shouldFailExecuteExternalCollectionExampleWithStaticFieldsAndNoFixtureClassLoaderInASecondRun()
        throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        // This jar contains a compiled calculator class. If you want to adjust
        // this calculator, you've to create a new jar by yourself.
        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunner runner = new SpecificationRunnerBuilder(repository).classLoader(customClassLoader)
            .systemUnderDevelopment(DefaultSystemUnderDevelopment.class.getName()).outputDirectory(outputDirectory).monitors(
                recorderMonitor, loggingMonitor).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("Demo/Collection.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(38, statistics.totalCount());
        Assert.assertEquals(38, statistics.rightCount());
        Assert.assertEquals(0, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(0, statistics.exceptionCount());

        File outputFile = new File(outputDirectory, "Demo_Collection2.html");

        new SpecificationRunnerExecutor(runner).outputFile(outputFile).execute("Demo/Collection.html");

        System.err.println(outputFile.getAbsolutePath());

        Assert.assertEquals(87, statistics.totalCount());
        Assert.assertEquals(76, statistics.rightCount());
        Assert.assertEquals(0, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        // This exceptions should occur because of the static field.
        Assert.assertEquals(11, statistics.exceptionCount());
    }

    @Test
    public void shouldExecuteExternalCollectionExampleWithCustomFixtureClassLoaderInASecondRun() throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        // This jar contains a compiled calculator class. If you want to adjust
        // this calculator, you've to create a new jar by yourself.
        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader fixtureClassLoader = ClassUtils.toClassLoaderWithDefaultParent(classPaths);
        ClassLoader secondFixtureClassLoader = ClassUtils.toClassLoaderWithDefaultParent(classPaths);

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunnerBuilder builder = new SpecificationRunnerBuilder(repository).systemUnderDevelopment(
            DefaultSystemUnderDevelopment.class.getName()).outputDirectory(outputDirectory).monitors(recorderMonitor,
                loggingMonitor);

        SpecificationRunner runner = builder.classLoader(fixtureClassLoader).build(DocumentRunner.class);
        SpecificationRunner secondRunner = builder.classLoader(secondFixtureClassLoader).build(DocumentRunner.class);

        new SpecificationRunnerExecutor(runner).execute("Demo/Collection.html");

        Statistics statistics = recorderMonitor.getStatistics();

        Assert.assertEquals(38, statistics.totalCount());
        Assert.assertEquals(38, statistics.rightCount());
        Assert.assertEquals(0, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        Assert.assertEquals(0, statistics.exceptionCount());

        new SpecificationRunnerExecutor(secondRunner).outputFile(new File(outputDirectory, "Demo_Collection2.html")).execute(
            "Demo/Collection.html");

        Assert.assertEquals(76, statistics.totalCount());
        Assert.assertEquals(76, statistics.rightCount());
        Assert.assertEquals(0, statistics.wrongCount());
        Assert.assertEquals(0, statistics.ignoredCount());
        // Since we use a new class loader for the second run, there should be
        // no exceptions.
        Assert.assertEquals(0, statistics.exceptionCount());
    }

    @Test
    public void shouldExecuteExternalCollectionExampleWithCustomFixtureClassLoaderInMultipleRunsUsingShortcutMethods()
        throws Exception {
        URI specsFolderUri = getClass().getResource("/specs-executor/external").toURI();
        File outputDirectory = TestFileUtils.createTempReportsDirectory();

        File specsFolder = new File(specsFolderUri);

        String repository = FileSystemRepository.class.getName() + ";" + specsFolder;

        // This jar contains a compiled calculator class. If you want to adjust
        // this calculator, you've to create a new jar by yourself.
        String externalDependencyJarPath = new File(specsFolder, "external-calculator.jar").getAbsolutePath();

        Set<String> classPaths = new HashSet<String>();
        classPaths.add(externalDependencyJarPath);

        ClassLoader customClassLoader = ClassUtils.toClassLoader(classPaths);

        RecorderMonitor recorderMonitor = new RecorderMonitor();
        LoggingMonitor loggingMonitor = new LoggingMonitor();

        SpecificationRunnerBuilder builder = new SpecificationRunnerBuilder(repository).classLoader(customClassLoader)
            .systemUnderDevelopment(DefaultSystemUnderDevelopment.class.getName()).outputDirectory(outputDirectory).monitors(
                recorderMonitor, loggingMonitor);

        int executions = 5;

        for (int i = 1; i <= executions; i ++ ) {
            ClassLoader fixtureClassLoader = ClassUtils.toClassLoaderWithDefaultParent(classPaths);
            SpecificationRunner runner = builder.classLoader(fixtureClassLoader).build(DocumentRunner.class);

            new SpecificationRunnerExecutor(runner).execute("Demo/Collection.html");

            Statistics statistics = recorderMonitor.getStatistics();

            Assert.assertEquals(38 * i, statistics.totalCount());
            Assert.assertEquals(38 * i, statistics.rightCount());
            Assert.assertEquals(0, statistics.wrongCount());
            Assert.assertEquals(0, statistics.ignoredCount());
            Assert.assertEquals(0, statistics.exceptionCount());

            fixtureClassLoader = null;
        }

    }

}
