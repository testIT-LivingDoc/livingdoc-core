/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.server.rpc.runner;

import static info.novatec.testit.livingdoc.util.URIUtil.decoded;
import static info.novatec.testit.livingdoc.util.URIUtil.flatten;

import java.io.*;
import java.util.*;

import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.LivingDocCore;
import info.novatec.testit.livingdoc.runner.NullSpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.runner.SpecificationRunner;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerMonitorProxy;
import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.rpc.runner.report.FileReportGenerator;
import info.novatec.testit.livingdoc.server.rpc.runner.report.HtmlReport;
import info.novatec.testit.livingdoc.server.rpc.runner.report.ReportGenerator;
import info.novatec.testit.livingdoc.server.rpc.runner.report.XmlReport;
import info.novatec.testit.livingdoc.util.cli.ArgumentMissingException;
import info.novatec.testit.livingdoc.util.cli.Bean;
import info.novatec.testit.livingdoc.util.cli.CommandLine;
import info.novatec.testit.livingdoc.util.cli.Option;
import info.novatec.testit.livingdoc.util.cli.ParseException;
import org.apache.commons.lang3.StringUtils;


public class CommandLineRemoteRunner {
    public static final String RUNNER_SUITE_OPTION = "suite";
    private final CommandLine cli;
    private final Map<String, Object> options;
    private SpecificationRunner runner;
    private SpecificationRunnerMonitor monitor;
    private PrintStream out;
    private String user;
    private String password;

    public CommandLineRemoteRunner() {
        this(System.out);
    }

    public CommandLineRemoteRunner(PrintStream out) {
        this.cli = new CommandLine();
        this.options = new HashMap<String, Object>();
        this.monitor = new NullSpecificationRunnerMonitor();
        this.out = out;
    }

    public void setMonitor(Object monitor) {
        this.monitor = new SpecificationRunnerMonitorProxy(monitor);
    }

    public void run(String... args) throws ParseException, IOException, LivingDocServerException {
        defineCommandLine();
        if ( ! parseCommandLine(args)) {
            return;
        }
        runSpec();
    }

    private void runSpec() throws IOException, LivingDocServerException {
        options.putAll(cli.getOptionValues());
        options.put("reportGenerator", reportGenerator());
        options.put("monitor", monitor);
        options.put("restRemoteRunner", restRemoteRunner());
        new Bean(runner).setProperties(options);
        runner.run(source(), destination());
    }

    private ReportGenerator reportGenerator() throws IOException {
        File outputDirectory = existsOutputDirectory() ? outputDirectory() : createOutputDirectory();
        FileReportGenerator generator = new FileReportGenerator(outputDirectory);
        generator.adjustReportFilesExtensions(optionSpecified(RUNNER_SUITE_OPTION) || output() == null);
        generator.setReportClass(optionSpecified("xml") ? XmlReport.class : HtmlReport.class);
        return generator;
    }

    private RestRemoteRunner restRemoteRunner() throws IOException {
        return new RestRemoteRunner(url(), getUser(), getPassword());
    }

    private boolean existsOutputDirectory() throws IOException {
        File outputDirectory = outputDirectory();
        return outputDirectory != null && outputDirectory.exists() && outputDirectory.isDirectory();
    }

    @SuppressWarnings("null")
    private File createOutputDirectory() throws IOException {
        File outputDirectory = outputDirectory();
        if (outputDirectory != null && outputDirectory.mkdirs()) {
            return outputDirectory;
        }
        throw new IOException("Output directory could not be created :" + outputDirectory == null ? "NULL" : outputDirectory
            .getAbsolutePath());
    }

    private String url() {
        return ( String ) cli.getOptionValue("url");
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private static String scanCredentials(){
        return String.valueOf(System.console().readPassword("Enter password: "));

    }

    public void setCredentialsFromArguments(String user, String pass) {
        this.user = user;
        if (pass != null) {
            this.password = pass;
        }else {
            this.password = scanCredentials();
        }
    }

    public void setCredentialsFromFile(String path) throws LivingDocServerException, IOException {

        File f = new File(path);
        FileInputStream fis = null;
        if (f.isFile()) {
            fis = new FileInputStream(f);
            try {
                Properties prop = new Properties();
                prop.load(fis);
                this.user = prop.getProperty("livingdoc.confluence.user");
                this.password = prop.getProperty("livingdoc.confluence.password");

            } catch (IOException exc) {
                throw new LivingDocServerException(LivingDocServerErrorKey.RETRIEVE_FILE_FAILED,
                        "File not accepted, please check the documentation");
            } finally{
                fis.close();
            }

        }
    }

    private String input() {
        return cli.getArgument(0) != null ? decoded(cli.getArgument(0)) : null;
    }

    private String output() {
        return cli.getArgument(1) != null ? decoded(cli.getArgument(1)) : null;
    }

    public String source() {
        return optionSpecified("repository") ? input() : fileName(input());
    }

    private File outputDirectory() throws IOException {
        if (optionSpecified(RUNNER_SUITE_OPTION)) {
            return output() != null ? new File(output()) : ( File ) cli.getOptionValue("output");
        }
        return output() != null ? parentFile(output()) : ( File ) cli.getOptionValue("output");
    }

    private File parentFile(String pathname) throws IOException {
        return new File(pathname).getCanonicalFile().getParentFile();
    }

    private String fileName(String pathname) {
        return new File(pathname).getName();
    }

    private String destination() {
        if (optionSpecified(RUNNER_SUITE_OPTION)) {
            return "";
        }
        return output() != null ? fileName(output()) : optionSpecified("repository") ? flatten(input()) : fileName(input());
    }

    private boolean parseCommandLine(String[] args) throws ParseException, LivingDocServerException, IOException {
        cli.parse(args);
        if (optionSpecified("help")) {
            return displayUsage();
        }
        if (optionSpecified("version")) {
            return displayVersion();
        }
        if (optionSpecified("user")) {
            if (optionSpecified("password")) {
                setCredentialsFromArguments((String) cli.getOptionValue("user"), (String) cli.getOptionValue("password"));
            }else{
                setCredentialsFromArguments((String) cli.getOptionValue("user"), null);
            }
        } else if(optionSpecified("config")) {
            setCredentialsFromFile(( String ) cli.getOptionValue("config"));
        }
        if (StringUtils.isEmpty(getUser())) {
            throw new ArgumentMissingException("user");
        }
        if (input() == null) {
            throw new ArgumentMissingException("input");
        }
        if (url() == null) {
            throw new ArgumentMissingException("url");
        }
        if ( ! optionSpecified("project")) {
            throw new ArgumentMissingException("project");
        }
        if ( ! optionSpecified("systemUnderTest")) {
            throw new ArgumentMissingException("system under test");
        }
        if ( ! optionSpecified("repositoryId")) {
            throw new ArgumentMissingException("repository id");
        }

        runner = optionSpecified(RUNNER_SUITE_OPTION) ? new RemoteSuiteRunner() : new RemoteDocumentRunner();
        return true;
    }

    private boolean optionSpecified(String name) {
        return cli.hasOptionValue(name);
    }

    private boolean displayVersion() {
        out.println(String.format("LivingDoc version \"%s\"", LivingDocCore.VERSION));
        return false;
    }

    private boolean displayUsage() {
        out.println(cli.usage());
        return false;
    }

    private void defineCommandLine() {
        File workingDirectory = new File(System.getProperty("user.dir"));

        String banner = "livingdoc [options] input [output]\n"
            + "Run the input specification and produce a report in output file or in directory specified by -o";
        cli.setBanner(banner);
        cli.defineOption(cli.buildOption("locale", "-l", "--locale LANG", "Set application language (en, fr, ...)").asType(
            Locale.class).whenPresent(new SetLocale()));
        cli.defineOption(cli.buildOption("url", "-u", "--url URL", "LivingDoc Server Context Path"));
        cli.defineOption(cli.buildOption("project", "-p", "--project PROJECT", "Project Name"));
        cli.defineOption(cli.buildOption("systemUnderTest", "-t", "--sut SUT", "System Under Test Name"));
        cli.defineOption(cli.buildOption("repositoryId", "--rep ID", "Repository Id"));
        cli.defineOption(cli.buildOption("output", "-o DIRECTORY",
            "Produce reports in DIRECTORY (defaults to current directory)").defaultingTo(workingDirectory).asType(
                File.class));
        cli.defineOption(cli.buildOption(RUNNER_SUITE_OPTION, "-s", "--suite",
            "Run a suite rather than a single test (output must refer to a directory)"));
        cli.defineOption(cli.buildOption("xml", "--xml", "Generate XML report (defaults to plain)"));
        cli.defineOption(cli.buildOption("help", "--help", "Display this help and exit"));
        cli.defineOption(cli.buildOption("version", "--version", "Output version information and exit"));
        cli.defineOption(cli.buildOption("debug", "--debug", "Enable debug mode").whenPresent(new SetDebugMode()));
        cli.defineOption(cli.buildOption("user", "--user USER", "Confluence username"));
        cli.defineOption(cli.buildOption("password", "--password PASSWORD", "Confluence password"));
        cli.defineOption(cli.buildOption("config", "--config PATH", "Confluence credentials file path"));
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
}
