package info.novatec.testit.livingdoc.runner;

import static info.novatec.testit.livingdoc.util.URIUtil.decoded;
import static info.novatec.testit.livingdoc.util.URIUtil.flatten;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.LivingDocCore;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.report.FileReportGenerator;
import info.novatec.testit.livingdoc.report.PlainReport;
import info.novatec.testit.livingdoc.report.ReportGenerator;
import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.repository.DocumentRepository;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.cli.ArgumentMissingException;
import info.novatec.testit.livingdoc.util.cli.Bean;
import info.novatec.testit.livingdoc.util.cli.CommandLine;
import info.novatec.testit.livingdoc.util.cli.Option;
import info.novatec.testit.livingdoc.util.cli.ParseException;
import info.novatec.testit.livingdoc.util.cli.StringArrayConverter;


public class CommandLineRunner {
    public static final String RUNNER_SUITE_OPTION = "suite";
    public static final String RUNNER_REPOSITORY_OPTION = "repository";
    private final CommandLine cli;
    private final Map<String, Object> options;
    private SpecificationRunner runner;
    private SpecificationRunnerMonitor monitor;
    private PrintStream out;

    public CommandLineRunner() {
        this(System.out);
    }

    public CommandLineRunner(PrintStream out) {
        this.cli = new CommandLine();
        this.options = new HashMap<String, Object>();
        this.monitor = new NullSpecificationRunnerMonitor();
        this.out = out;
    }

    public void setMonitor(Object monitor) {
        this.monitor = new SpecificationRunnerMonitorProxy(monitor);
    }

    public void run(String... args) throws ParseException, ArgumentMissingException, IOException {

        defineCommandLine();
        parseCommandLine(args);

        if (isOptionSpecified("help")) {
            displayUsage();
            return;
        }

        if (isOptionSpecified("version")) {
            displayVersion();
            return;
        }

        if ( ! isInputSpecified()) {
            throw new ArgumentMissingException("input");
        }

        runner = isOptionSpecified(RUNNER_SUITE_OPTION) ? new SuiteRunner() : new DocumentRunner();
        runSpec();

    }

    private void runSpec() throws IOException {
        options.putAll(cli.getOptionValues());
        options.put("report generator", reportGenerator());
        options.put("monitor", monitor);
        options.put(RUNNER_REPOSITORY_OPTION, repository());
        new Bean(runner).setProperties(options);
        runner.run(source(), destination());
    }

    private DocumentRepository repository() throws IOException {
        return isOptionSpecified(RUNNER_REPOSITORY_OPTION) ? ( DocumentRepository ) cli.getOptionValue(RUNNER_REPOSITORY_OPTION)
            : new FileSystemRepository(parentFile(getInput()));
    }

    private ReportGenerator reportGenerator() throws IOException {
        FileReportGenerator generator = new FileReportGenerator(createOutputDirectory());
        generator.adjustReportFilesExtensions(isOptionSpecified(RUNNER_SUITE_OPTION) || ! isOutputSpecified());
        generator.setReportClass(isOptionSpecified("xml") ? XmlReport.class : PlainReport.class);
        return generator;
    }

    private File createOutputDirectory() throws IOException {
        File outputDirectory = outputDirectory();
        if (outputDirectory.exists()) {
            if (outputDirectory.isDirectory()) {
                return outputDirectory;
            }
            throw new IOException("Output directory exist and is a file  :" + outputDirectory.getAbsolutePath());
        }
        if (outputDirectory.mkdirs()) {
            return outputDirectory;
        }
        throw new IOException("Output directory could not be created :" + outputDirectory.getAbsolutePath());
    }

    private boolean isInputSpecified() {
        return cli.getArgument(0) != null;
    }

    private String getInput() {
        return decoded(cli.getArgument(0));
    }

    private boolean isOutputSpecified() {
        return cli.getArgument(1) != null;
    }

    private String getOutput() {
        return decoded(cli.getArgument(1));
    }

    public String source() {
        return isOptionSpecified(RUNNER_REPOSITORY_OPTION) ? getInput() : fileName(getInput());
    }

    private File outputDirectory() throws IOException {
        if (isOptionSpecified(RUNNER_SUITE_OPTION)) {
            return isOutputSpecified() ? new File(getOutput()) : ( File ) cli.getOptionValue("output");
        }
        return isOutputSpecified() ? parentFile(getOutput()) : ( File ) cli.getOptionValue("output");
    }

    private File parentFile(String pathname) throws IOException {
        return new File(pathname).getCanonicalFile().getParentFile();
    }

    private String fileName(String pathname) {
        return new File(pathname).getName();
    }

    private String destination() {
        if (isOptionSpecified(RUNNER_SUITE_OPTION)) {
            return "";
        }
        return isOutputSpecified() ? fileName(getOutput()) : isOptionSpecified(RUNNER_REPOSITORY_OPTION) ? flatten(getInput())
            : fileName(getInput());
    }

    private void parseCommandLine(String[] args) throws ParseException {
        cli.parse(args);
    }

    private boolean isOptionSpecified(String name) {
        return cli.hasOptionValue(name);
    }

    private void displayVersion() {
        out.println(String.format("LivingDoc version \"%s\"", LivingDocCore.VERSION));
    }

    private void displayUsage() {
        out.println(cli.usage());
    }

    private void defineCommandLine() {
        File workingDirectory = new File(System.getProperty("user.dir"));

        String banner = "livingdoc [options] input [output]\n"
            + "Run the input specification and produce a report in output file or in directory specified by -o";
        cli.setBanner(banner);

        cli.defineOption(cli.buildOption("lazy", "--lazy", "Execute document in lazy mode"));
        cli.defineOption(cli.buildOption("locale", "-l", "--locale LANG", "Set application language (en, fr, ...)").asType(
            Locale.class).whenPresent(new SetLocale()));
        cli.defineOption(cli.buildOption("system under development", "--sud", "-f CLASS;ARGS",
            "Use CLASS as the system under development and instantiate it with ARGS").convertedWith(new FactoryConverter())
            .defaultingTo(new DefaultSystemUnderDevelopment()));
        cli.defineOption(cli.buildOption("output", "-o DIRECTORY",
            "Produce reports in DIRECTORY (defaults to current directory)").defaultingTo(workingDirectory).asType(
                File.class));
        cli.defineOption(cli.buildOption(RUNNER_REPOSITORY_OPTION, "-r CLASS;ARGS",
            "Use CLASS as the document repository and instantiate it with ARGS (defaults to info.novatec.testit.livingdoc.repository.FileSystemRepository)")
            .convertedWith(new FactoryConverter()));
        cli.defineOption(cli.buildOption("interpreter selector", "--selector CLASS",
            "Use CLASS as the interpreter selector (defaults to info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector)")
            .asType(Class.class).defaultingTo(LivingDocInterpreterSelector.class));
        cli.defineOption(cli.buildOption(RUNNER_SUITE_OPTION, "-s", "--suite",
            "Run a suite rather than a single test (output must refer to a directory)"));
        cli.defineOption(cli.buildOption("sections", "-t SECTIONS",
            "Filter input specification to only execute SECTIONS (comma separated list of sections)").convertedWith(
                new StringArrayConverter(",")).defaultingTo(new String[0]));
        cli.defineOption(cli.buildOption("xml", "--xml", "Generate XML report (defaults to plain)"));
        cli.defineOption(cli.buildOption("help", "--help", "Display this help and exit"));
        cli.defineOption(cli.buildOption("version", "--version", "Output version information and exit"));
        cli.defineOption(cli.buildOption("debug", "--debug", "Enable debug mode").whenPresent(new SetDebugMode()));
        cli.defineOption(cli.buildOption("stop", "--stop", "Stop the execution of the specification on the first failure")
            .whenPresent(new SetStopOnFirstFailure()));
    }

    public static class SetDebugMode implements Option.Stub {
        @Override
        public void call(Option option) {
            LivingDoc.setDebugEnabled(true);
        }
    }

    public static class SetLocale implements Option.Stub {
        @Override
        public void call(Option option) {
            LivingDoc.setLocale(( Locale ) option.getValue());
        }
    }

    public static class SetStopOnFirstFailure implements Option.Stub {
        @Override
        public void call(Option option) {
            LivingDoc.setStopOnFirstFailure(true);
        }
    }
}
