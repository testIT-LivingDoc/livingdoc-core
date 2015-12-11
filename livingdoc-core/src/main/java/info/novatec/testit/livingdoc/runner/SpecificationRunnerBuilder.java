package info.novatec.testit.livingdoc.runner;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.document.InterpreterSelector;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.document.LivingDocTableFilter;
import info.novatec.testit.livingdoc.report.FileReportGenerator;
import info.novatec.testit.livingdoc.report.PlainReport;
import info.novatec.testit.livingdoc.report.Report;
import info.novatec.testit.livingdoc.repository.DocumentRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ClassUtils;


/**
 * This class simplifies the programmatically construction of a
 * {@link SpecificationRunner}.
 * 
 * Especially the opportunity to set a class loader is important to include
 * third party extensions from a given class path.
 * 
 * Since this builder provides a fluent API, each setter will return the same
 * instance of the called builder instance.
 */
public class SpecificationRunnerBuilder {
    private final static Logger LOG = LoggerFactory.getLogger(SpecificationRunnerBuilder.class);

    // Mandatory parameters
    private final String documentRepositoryClass;

    // Optional parameters
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private File outputDirectory = new File(System.getProperty("user.dir"));
    private String systemUnderDevelopmentClass = DefaultSystemUnderDevelopment.class.getName();
    private String interpreterSelectorClass = LivingDocInterpreterSelector.class.getName();
    private String reportClass = PlainReport.class.getName();
    private boolean lazy = false;

    // Predefined parameters (not to set)
    private final CompositeSpecificationRunnerMonitor compositeMonitor = new CompositeSpecificationRunnerMonitor();
    private Set<String> sections = new HashSet<String>();

    public SpecificationRunnerBuilder(String documentRepositoryClass) {
        this.documentRepositoryClass = documentRepositoryClass;
    }

    /**
     * In some cases (for now only for the SuD) LivingDoc allows you to use your
     * own class loader.
     */
    public SpecificationRunnerBuilder classLoader(ClassLoader customClassLoader) {
        this.classLoader = customClassLoader;
        return this;
    }

    /**
     * The output folder for generated result sources (defaults to user's
     * temporary directory).
     * 
     * <p>
     * To make sure that a directory exists, you can call
     * {@link org.apache.commons.io.IOUtils#createDirectoryTree(File) IOUtils}
     * before passing it to the builder.
     */
    public SpecificationRunnerBuilder outputDirectory(File directory) {
        this.outputDirectory = directory;
        return this;
    }

    /**
     * A monitor is able to record the passed/failed tests. You can add your own
     * monitors to get detailed statistics.
     */
    public SpecificationRunnerBuilder monitors(SpecificationRunnerMonitor... monitors) {
        for (SpecificationRunnerMonitor monitor : monitors) {
            compositeMonitor.add(monitor);
        }
        return this;
    }

    /**
     * Specify the system under development, which defines how to load the
     * fixtures (defaults to {@link DefaultSystemUnderDevelopment}.
     * 
     * <p>
     * You can specify constructor arguments using the following syntax:
     * </p>
     * 
     * <ul>
     * <li><code>my.company.sud.MyOwnSudImpl;arg1;arg2</li>
     * <li><code>info.novatec.testit.livingdoc.systemunderdevelopment.
     * DefaultSystemUnderDevelopment</li>
     * </ul>
     */
    public SpecificationRunnerBuilder systemUnderDevelopment(String clazz) {
        this.systemUnderDevelopmentClass = clazz;
        return this;
    }

    /**
     * Specify the the interpreter selector class which defines how to load the
     * fixtures (defaults to {@link LivingDocInterpreterSelector}).
     */
    public SpecificationRunnerBuilder interpreterSelector(String clazz) {
        this.interpreterSelectorClass = clazz;
        return this;
    }

    /**
     * Specify the report output format (defaults to {@link PlainReport}).
     */
    public SpecificationRunnerBuilder report(String clazz) {
        this.reportClass = clazz;
        return this;
    }

    /**
     * You can add a section to filter the input specification, so LivingDoc
     * will only execute the specified sections.
     */
    public SpecificationRunnerBuilder withSection(String section) {
        this.sections.add(section);
        return this;
    }

    /**
     * You can add sections to filter the input specification, so LivingDoc will
     * only execute the specified sections.
     */
    public SpecificationRunnerBuilder sections(String[] paramSections) {
        Collections.addAll(this.sections, paramSections);
        return this;
    }

    /**
     * @see LivingDocTableFilter
     */
    public SpecificationRunnerBuilder lazy(boolean paramLazy) {
        this.lazy = paramLazy;
        return this;
    }

    /**
     * Builds a {@link SpecificationRunner} of the given class.
     * 
     * @param <S> Let you specify the concrete runner implementation.
     * @param passedSpecificationRunnerClass Subclass of the specification
     * runner which should be instantiated.
     * @return the specification runner.
     * 
     * @throws ClassNotFoundException if a specified class could not be found
     * under the given class loader context.
     * @throws UndeclaredThrowableException if a specified class could not be
     * initialized. Contains the original exception.
     * @throws IllegalArgumentException if the passed specification runner class
     * is not a supported runner.
     * 
     */
    public <S extends SpecificationRunner> S build(Class<S> passedSpecificationRunnerClass) throws ClassNotFoundException {
        if (DocumentRunner.class.isAssignableFrom(passedSpecificationRunnerClass)) {
            return passedSpecificationRunnerClass.cast(buildDocumentRunner());
        }

        if (SuiteRunner.class.isAssignableFrom(passedSpecificationRunnerClass)) {
            return passedSpecificationRunnerClass.cast(buildSuiteRunner());
        }

        throw new IllegalArgumentException("Invalid specification runner");
    }

    private SpecificationRunner buildDocumentRunner() throws ClassNotFoundException {
        FileReportGenerator reportGenerator = createDefaultReportGenerator();
        String[] sectionsArray = sections.toArray(new String[sections.size()]);
        SystemUnderDevelopment systemUnderDevelopment = instantiateSystemUnderDevelopment();
        DocumentRepository repository = instantiateDocumentRepository();
        Class< ? extends InterpreterSelector> selectorClass = loadInterpreterSelectorClass();

        DocumentRunner runner = new DocumentRunner();
        runner.setRepository(repository);
        runner.setSystemUnderDevelopment(systemUnderDevelopment);
        runner.setReportGenerator(reportGenerator);
        runner.setInterpreterSelector(selectorClass);
        runner.setSections(sectionsArray);
        runner.setMonitor(compositeMonitor);
        runner.setLazy(lazy);
        return runner;
    }

    private SpecificationRunner buildSuiteRunner() throws ClassNotFoundException {
        // One of the biggest differences between the suite and the document
        // runner is the report generation. The suite itself generates no
        // reports. It delegates the report generation to the underlying
        // document runner. Therefore we've to create separate report
        // generators.
        FileReportGenerator reportGenerator = createDefaultReportGenerator();
        reportGenerator.adjustReportFilesExtensions(true);
        String[] sectionsArray = sections.toArray(new String[sections.size()]);
        SystemUnderDevelopment systemUnderDevelopment = instantiateSystemUnderDevelopment();
        DocumentRepository repository = instantiateDocumentRepository();
        Class< ? extends InterpreterSelector> selectorClass = loadInterpreterSelectorClass();

        SuiteRunner runner = new SuiteRunner();
        runner.setRepository(repository);
        runner.setSystemUnderDevelopment(systemUnderDevelopment);
        runner.setReportGenerator(reportGenerator);
        runner.setInterpreterSelector(selectorClass);
        runner.setSections(sectionsArray);
        runner.setMonitor(compositeMonitor);
        runner.setLazy(lazy);
        return runner;
    }

    /**
     * Instantiate the system under development from a string, to be able to use
     * third party SuDs.
     */
    private SystemUnderDevelopment instantiateSystemUnderDevelopment() {
        LOG.debug("Creating SUD " + systemUnderDevelopmentClass);
        SystemUnderDevelopment systemUnderDevelopment = ClassUtils.createInstanceFromClassNameWithArguments(classLoader,
            systemUnderDevelopmentClass, SystemUnderDevelopment.class);
        systemUnderDevelopment.setClassLoader(classLoader);
        return systemUnderDevelopment;
    }

    /**
     * Instantiate the document repository from a string, to be able to use
     * third party repositories.
     */
    private DocumentRepository instantiateDocumentRepository() {
        DocumentRepository documentRepository = ClassUtils.createInstanceFromClassNameWithArguments(classLoader,
            documentRepositoryClass, DocumentRepository.class);
        return documentRepository;
    }

    /**
     * We load the interpreter selector by a class name, to be able to use third
     * party interpreter selectors.
     */
    @SuppressWarnings("unchecked")
    private Class< ? extends InterpreterSelector> loadInterpreterSelectorClass() throws ClassNotFoundException {
        return ( Class< ? extends InterpreterSelector> ) ClassUtils.loadClass(classLoader, interpreterSelectorClass);
    }

    /**
     * We load the report class by name, to be able to use third party report
     * formats.
     */
    @SuppressWarnings("unchecked")
    private Class< ? extends Report> loadReportClass() throws ClassNotFoundException {
        return ( Class< ? extends Report> ) ClassUtils.loadClass(classLoader, reportClass);
    }

    private FileReportGenerator createDefaultReportGenerator() throws ClassNotFoundException {
        Class< ? extends Report> loadedReportClass = loadReportClass();
        FileReportGenerator reportGenerator = new FileReportGenerator(outputDirectory);
        reportGenerator.setReportClass(loadedReportClass);
        return reportGenerator;
    }

}
