package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.report.Report;
import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.runner.*;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.client.XmlRpcClientExecutor;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.client.XmlRpcClientExecutorException;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.client.XmlRpcClientExecutorFactory;
import info.novatec.testit.livingdoc.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.xml.sax.SAXException;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


/**
 * Runner Class. Definition of a Runner.
 * <p/>
 * Copyright (c) 2006-2007 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */

@Entity
@Table(name = "RUNNER")
@SuppressWarnings("serial")
public class Runner extends AbstractVersionedEntity implements Comparable<Runner> {
    private static final String AGENT_HANDLER = "livingdoc-agent1";
    private String name;

    private String serverName;
    private String serverPort;
    private Boolean secured;

    private SortedSet<String> classpaths = new TreeSet<String>();

    public static Runner newInstance(String name) {
        Runner runner = new Runner();
        runner.setName(name);

        return runner;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "SERVER_NAME", nullable = true, length = 255)
    public String getServerName() {
        return serverName;
    }

    @Basic
    @Column(name = "SERVER_PORT", nullable = true, length = 8)
    public String getServerPort() {
        return serverPort;
    }

    @Basic
    @Column(name = "SECURED", nullable = true)
    public boolean isSecured() {
        return secured != null && secured.booleanValue();
    }

    @CollectionOfElements
    @JoinTable(name = "RUNNER_CLASSPATHS", joinColumns = { @JoinColumn(name = "RUNNER_ID") })
    @Column(name = "elt", nullable = true, length = 255)
    @Sort(type = SortType.COMPARATOR, comparator = ClasspathComparator.class)
    public SortedSet<String> getClasspaths() {
        return classpaths;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServerName(String serverName) {
        this.serverName = StringUtils.stripToNull(serverName);
    }

    public void setServerPort(String serverPort) {
        this.serverPort = StringUtils.stripToNull(serverPort);
    }

    public void setSecured(Boolean secured) {
        this.secured = secured != null && secured.booleanValue();
    }

    public void setClasspaths(SortedSet<String> classpaths) {
        this.classpaths = classpaths;
    }

    public Execution execute(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion,
        String sections, String locale) {
        if (isRemote()) {
            return executeRemotely(specification, systemUnderTest, implementedVersion, sections, locale);
        }
        return executeLocally(specification, systemUnderTest, implementedVersion, sections, locale);
    }

    @SuppressWarnings("unchecked")
    private Execution executeRemotely(Specification specification, SystemUnderTest systemUnderTest,
        boolean implementedVersion, String paramSections, String paramLocale) {
        try {
            String sections = ( String )StringUtils.stripToEmpty(paramSections);
            String locale = ( String )StringUtils.stripToEmpty(paramLocale);

            XmlRpcClientExecutor xmlrpc = XmlRpcClientExecutorFactory.newExecutor(agentUrl());

            List< ? > params = CollectionUtil.toVector(marshallize(), systemUnderTest.marshallize(), specification
                .marshallize(), implementedVersion, sections, locale);
            Vector<Object> execParams = ( Vector<Object> ) xmlrpc.execute(AGENT_HANDLER + ".execute", params);

            Execution execution = toExecution(execParams);
            execution.setSystemUnderTest(systemUnderTest);
            execution.setSpecification(specification);
            execution.setRemotelyExecuted();
            return execution;
        } catch (XmlRpcClientExecutorException e) {
            return Execution.error(specification, systemUnderTest, paramSections, ExceptionUtils.stackTrace(e, "<br>", 15));
        }
    }

    private Execution executeLocally(Specification specification, SystemUnderTest systemUnderTest,
        boolean implementedVersion, String sections, String locale) {
        String implemented = implementedVersion ? "" : "?implemented=false";
        String source = URIUtil.raw(specification.getName()) + implemented;
        File outputFile = null;

        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader currentClassLoader = getClass().getClassLoader();
            Collection<String> dependencies = resolveDependentClassPaths(systemUnderTest);
            ClassLoader dependencyCassLoader = ClassUtils.toClassLoader(dependencies, currentClassLoader);
            // Merge the current class loader with external dependencies.
            JoinClassLoader joinClassLoader = new JoinClassLoader(currentClassLoader, dependencyCassLoader);

            // Set the current thread class loader to avoid problems with an
            // OSGI environment
            Thread.currentThread().setContextClassLoader(joinClassLoader);

            // Prepare
            outputFile = File.createTempFile("LivingDocTest", ".tst");
            outputFile.delete();

            Class< ? extends SpecificationRunner> runnerClass = DocumentRunner.class;
            Class< ? extends Report> reportClass = XmlReport.class;

            String[] sectionsArray = sections == null ? new String[] {} : sections.split(",");

            RecorderMonitor recorderMonitor = new RecorderMonitor();
            LoggingMonitor loggingMonitor = new LoggingMonitor();

            SpecificationRunnerBuilder builder = new SpecificationRunnerBuilder(specification.getRepository()
                .asCmdLineOption()).classLoader(joinClassLoader).sections(sectionsArray).report(reportClass.getName())
                    .systemUnderDevelopment(systemUnderTest.fixtureFactoryCmdLineOption()).monitors(recorderMonitor,
                        loggingMonitor).outputDirectory(outputFile.getParentFile());

            SpecificationRunner runner = builder.build(runnerClass);

            // Act
            SpecificationRunnerExecutor executor = new SpecificationRunnerExecutor(runner).locale(new Locale(locale));

            // A specification runner does not have a output file.
            if (runnerClass == DocumentRunner.class) {
                executor.outputFile(outputFile);
            }

            executor.execute(source);

            // Load the report class to avoid a ClassNotFoundException in an
            // OSGI context.
            joinClassLoader.loadClass(XmlReport.class.getName());
            XmlReport report = XmlReport.parse(outputFile);

            // Check
            Execution execution = Execution.newInstance(specification, systemUnderTest, report);

            return execution;
        } catch (IOException | ClassNotFoundException | SAXException e) {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        } finally {
            if (outputFile != null) {
                outputFile.deleteOnExit();
            }
            // Reset the class loader to the previous behavior.
            Thread.currentThread().setContextClassLoader(threadClassLoader);
        }
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(RUNNER_NAME_IDX, name);
        parameters.add(RUNNER_SERVER_NAME_IDX,StringUtils.stripToEmpty(serverName));
        parameters.add(RUNNER_SERVER_PORT_IDX,StringUtils.stripToEmpty(serverPort));
        parameters.add(RUNNER_CLASSPATH_IDX, new Vector<String>(classpaths));
        parameters.add(RUNNER_SECURED_IDX, isSecured());
        return parameters;
    }

    public String agentUrl() {
        return ( isSecured() ? "https://" : "http://" ) + serverName + ":" + serverPort;
    }

    @Override
    public int compareTo(Runner runner) {
        return this.getName().compareTo(runner.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof Runner )) {
            return false;
        }

        Runner runnerCompared = ( Runner ) o;
        return getName().equals(runnerCompared.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    private Collection<String> resolveDependentClassPaths(SystemUnderTest systemUnderTest) {
        Collection<String> dependencies = new ArrayList<String>();
        dependencies.addAll(resolveClassPathsWildcards(getClasspaths()));
        dependencies.addAll(resolveClassPathsWildcards(systemUnderTest.getFixtureClasspaths()));
        dependencies.addAll(resolveClassPathsWildcards(systemUnderTest.getSutClasspaths()));
        return dependencies;
    }

    public Collection<String> resolveClassPathsWildcards(Collection<String> classPaths) {
        Collection<String> resolvedClassPaths = new ArrayList<String>();

        for (String classPath : classPaths) {
            File filePattern = new File(classPath);

            if (filePattern.exists()) {
                resolvedClassPaths.add(filePattern.getAbsolutePath());
            } else if (filePattern.getParentFile() != null && filePattern.getParentFile().exists()) {
                File fileDirectory = filePattern.getParentFile();
                Collection<File> matchedFiles = FileUtils.listFiles(fileDirectory, new WildcardFileFilter(filePattern
                    .getName()), null);

                for (File matchedFile : matchedFiles) {
                    String resolvedClassPath = matchedFile.getAbsolutePath();
                    resolvedClassPaths.add(resolvedClassPath);
                }
            } else {
                throw new IllegalArgumentException("Class path must exist or contain a parent directory: " + classPath);
            }

        }

        return resolvedClassPaths;
    }

    @Transient
    private boolean isRemote() {
        return ! StringUtils.isEmpty(serverName) && ! StringUtils.isEmpty(serverPort);
    }

}
