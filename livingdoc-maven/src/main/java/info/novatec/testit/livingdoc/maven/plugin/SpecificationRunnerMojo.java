/* Copyright (c) 2007 Pyxis Technologies inc.
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
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.maven.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.runner.CompositeSpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.runner.RecorderMonitor;


/**
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 * @description Runs LivingDoc specifications
 */
public class SpecificationRunnerMojo extends AbstractMojo {
    /**
     * Set this to 'true' to bypass livingdoc tests entirely. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.livingdoc.test.skip}"
     * default-value="false"
     */
    private boolean skip;

    /**
     * Project fixture classpath.
     * 
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> classpathElements;

    /**
     * The directory where compiled fixture classes go.
     * 
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    protected File fixtureOutputDirectory;

    /**
     * The SystemUnderDevelopment class to use
     * 
     * @parameter default-value=
     * "info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment"
     * @required
     */
    protected String systemUnderDevelopment;

    /**
     * @parameter expression="${project.build.directory}/livingdoc-reports"
     * @required
     */
    protected File reportsDirectory;

    /**
     * @parameter expression="${maven.livingdoc.reports.type}"
     * default-value="html"
     */
    protected String reportsType;

    /**
     * @parameter expression="${livingdoc.repositories}"
     * @required
     */
    protected List<Repository> repositories;

    /**
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List<Artifact> pluginDependencies;

    /**
     * Set this to 'true' to stop the execution on a failure.
     * 
     * @parameter expression="${maven.livingdoc.test.stop}"
     * default-value="false"
     */
    protected boolean stopOnFirstFailure;

    /**
     * Set the locale for the execution.
     * 
     * @parameter expression="${maven.livingdoc.locale}"
     */
    protected String locale;

    /**
     * Set the Selector class.
     * 
     * @parameter expression="${maven.livingdoc.selector}" default-value=
     * "info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector"
     */
    protected String selector;

    /**
     * Set the Debug mode.
     * 
     * @parameter expression="${maven.livingdoc.debug}" default-value="false"
     */
    protected boolean debug;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.livingdoc.test.failure.ignore}"
     * default-value="false"
     */
    protected boolean testFailureIgnore;

    private Statistics statistics;

    private boolean testFailed;
    private boolean exceptionOccured;

    public SpecificationRunnerMojo() {
        this.statistics = new Statistics();
        this.repositories = new ArrayList<Repository>();
    }

    public void addRepository(Repository repository) {
        repositories.add(repository);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Not executing specifications.");
        } else {
            prepareReportsDir();
            printBanner();
            runAllTests();
            printFooter();
            checkTestsResults();
        }
    }

    private void checkTestsResults() throws MojoExecutionException, MojoFailureException {
        if (exceptionOccured) {
            notifyExceptionsOccured();
        }
        if (testFailed) {
            notifyTestsFailed();
        }
    }

    private void notifyExceptionsOccured() throws MojoExecutionException {
        if (testFailureIgnore) {
            getLog().error("Some livingdoc tests did not run\n");
        } else {
            throw new MojoExecutionException("Some livingdoc tests did not run");
        }
    }

    private void notifyTestsFailed() throws MojoFailureException {
        if (testFailureIgnore) {
            getLog().error("There were livingdoc tests failures\n");
        } else {
            throw new MojoFailureException("There were livingdoc tests failures");
        }
    }

    private void printBanner() {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("  L I V I N G D O C  S P E C I F I C A T I O N S  ");
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    private void runAllTests() throws MojoExecutionException {
        for (Repository repository : repositories) {
            if (shouldStop()) {
                break;
            }

            runAllIn(repository);
        }
    }

    private void runAllIn(Repository repository) throws MojoExecutionException {
        runTestsIn(repository);
        runSuitesIn(repository);
    }

    private void runSuitesIn(Repository repository) throws MojoExecutionException {
        for (String suite : repository.getSuites()) {
            if (shouldStop()) {
                break;
            }

            String repoCmdOption = repository.getType() + ( repository.getRoot() != null ? ";" + repository.getRoot() : "" );
            String outputDir = new File(reportsDirectory, repository.getName()).getAbsolutePath();

            List<String> args = args("-f", systemUnderDevelopment, "-s", "-r", repoCmdOption, "-o", outputDir, suite);

            run(args);
        }
    }

    private void runTestsIn(Repository repository) throws MojoExecutionException {
        for (String test : repository.getTests()) {
            if (shouldStop()) {
                break;
            }

            String repoCmdOption = repository.getType() + ( repository.getRoot() != null ? ";" + repository.getRoot() : "" );
            String outputDir = new File(reportsDirectory, repository.getName()).getAbsolutePath();

            List<String> args = args("-f", systemUnderDevelopment, "-r", repoCmdOption, "-o", outputDir, test);

            run(args);
        }
    }

    private void run(List<String> args) throws MojoExecutionException {
        DynamicCoreInvoker runner = new DynamicCoreInvoker(createClassLoader());
        CompositeSpecificationRunnerMonitor monitors = new CompositeSpecificationRunnerMonitor();
        monitors.add(new LoggerMonitor(getLog()));
        RecorderMonitor recorder = new RecorderMonitor();
        monitors.add(recorder);
        runner.setMonitor(monitors);

        try {
            runner.run(toArray(args));
        } catch (Exception e) {
            exceptionOccured = true;
            throw new MojoExecutionException("Unable to run tests", e);
        }

        exceptionOccured |= recorder.hasException();
        testFailed |= recorder.hasTestFailures();
        statistics.tally(recorder.getStatistics());
    }

    private void printFooter() {
        System.out.println();
        System.out.println("Results:");
        System.out.println(statistics);
        System.out.println();
    }

    private ClassLoader createClassLoader() throws MojoExecutionException {
        List<URL> urls = new ArrayList<URL>();
        for (Iterator< ? > it = classpathElements.iterator(); it.hasNext();) {
            String s = ( String ) it.next();
            urls.add(toURL(new File(s)));
        }

        urls.add(toURL(fixtureOutputDirectory));

        if ( ! containsLivingDocCore(urls)) {
            urls.add(getDependencyURL("livingdoc-core"));
        }

        urls.add(getDependencyURL("commons-codec"));

        URL[] classpath = urls.toArray(new URL[urls.size()]);

        return new URLClassLoader(classpath, ClassLoader.getSystemClassLoader());
    }

    private URL getDependencyURL(String name) throws MojoExecutionException {
        if (pluginDependencies != null && ! pluginDependencies.isEmpty()) {
            for (Iterator<Artifact> it = pluginDependencies.iterator(); it.hasNext();) {
                Artifact artifact = it.next();
                if (artifact.getArtifactId().equals(name) && artifact.getType().equals("jar")) {
                    return toURL(artifact.getFile());
                }
            }
        }
        throw new MojoExecutionException("Dependency not found: " + name);
    }

    private URL toURL(File f) throws MojoExecutionException {
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Invalid dependency: " + f.getAbsolutePath(), e);
        }
    }

    private boolean containsLivingDocCore(List<URL> urls) {

        for (Iterator<URL> it = urls.iterator(); it.hasNext();) {
            URL url = it.next();

            if (url.getFile().indexOf("livingdoc-core") != - 1 && url.getFile().endsWith(".jar")) {
                return true;
            }
        }

        return false;
    }

    private void prepareReportsDir() throws MojoExecutionException {
        if (reportsDirectory.exists()) {
            if ( ! reportsDirectory.isDirectory()) {
                throw new MojoExecutionException("Reports directory exist as a file : " + reportsDirectory
                    .getAbsolutePath());
            }
        } else if ( ! reportsDirectory.mkdirs()) {
            throw new MojoExecutionException("Could not create reports directory: " + reportsDirectory.getAbsolutePath());
        }
    }

    private boolean shouldStop() {
        return stopOnFirstFailure && statistics.indicatesFailure();
    }

    private List<String> args(String... args) {
        List<String> arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(args));

        if ( ! StringUtils.isEmpty(locale)) {
            arguments.add("--locale");
            arguments.add(locale);
        }

        if ( ! StringUtils.isEmpty(selector) && ! LivingDocInterpreterSelector.class.getName().equals(selector)) {
            arguments.add("--selector");
            arguments.add(selector);
        }

        if ("xml".equalsIgnoreCase(reportsType)) {
            arguments.add("--xml");
        }

        if (stopOnFirstFailure) {
            arguments.add("--stop");
        }

        if (debug) {
            arguments.add("--debug");
        }

        return arguments;
    }

    private String[] toArray(List<String> args) {
        String[] arguments = new String[args.size()];
        args.toArray(arguments);
        return arguments;
    }
}
