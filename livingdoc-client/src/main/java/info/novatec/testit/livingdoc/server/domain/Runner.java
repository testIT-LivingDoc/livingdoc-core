package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.report.*;
import info.novatec.testit.livingdoc.runner.*;
import info.novatec.testit.livingdoc.server.*;
import info.novatec.testit.livingdoc.server.rest.requests.*;
import info.novatec.testit.livingdoc.server.rest.responses.*;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang3.*;
import org.springframework.http.*;
import org.springframework.web.client.*;
import org.xml.sax.*;

import javax.persistence.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


/**
 * Runner Class. Definition of a Runner.
 * <p>
 * Copyright (c) 2006-2007 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */

@Entity
@Table(name = "RUNNER")
@SuppressWarnings("serial")
public class Runner extends AbstractVersionedEntity implements Comparable<Runner> {
    private static final String AGENT_HANDLER = "livingdoc-agent1";
    private static final String AGENT_METHOD = "/execute";
    private String name;

    private String serverName;
    private String serverPort;
    private Boolean secured;

    private Set<String> classpaths = new HashSet<String>();

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

    @ElementCollection
    @JoinTable(name = "RUNNER_CLASSPATHS", joinColumns = {@JoinColumn(name = "RUNNER_ID")})
    @Column(name = "elt", nullable = true, length = 255)
    public Set<String> getClasspaths() {
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

    public void setClasspaths(Set<String> classpaths) {
        this.classpaths = classpaths;
    }

    public Execution execute(Specification specification, SystemUnderTest systemUnderTest, boolean implementedVersion,
                             String sections, String locale) {
        if (isRemote()) {
            ExecutionResponse executionResponse = executeRemotely(specification, systemUnderTest, implementedVersion, sections, locale);
            return executionResponse.execution;
        }
        return executeLocally(specification, systemUnderTest, implementedVersion, sections, locale);
    }

    @SuppressWarnings("unchecked")
    private ExecutionResponse executeRemotely(Specification specification, SystemUnderTest systemUnderTest,
                                              boolean implementedVersion, String paramSections, String paramLocale) {
        try {

            SystemUnderTest mySystemUnderTest = systemUnderTest.marshallizeRest();
            Specification mySpecification = specification.marshallizeRest();

            ExecutionRequest executionRequest = new ExecutionRequest(this, mySpecification, mySystemUnderTest, implementedVersion, paramSections, paramLocale);

            RestTemplate client = new RestTemplate();

            RequestEntity<ExecutionRequest> requestEntity;
            RequestEntity.BodyBuilder bodyBuilder = RequestEntity.post(new URI(agentUrl() + AGENT_METHOD));
            bodyBuilder.contentType(MediaType.APPLICATION_JSON);

            requestEntity = bodyBuilder.body(executionRequest);

            ResponseEntity<ExecutionResponse> responseEntity = client.exchange(requestEntity, ExecutionResponse.class);

            responseEntity.getBody().execution.getSystemUnderTest().getProject().setRepositories(systemUnderTest.getProject().getRepositories());
            responseEntity.getBody().execution.getSystemUnderTest().getProject().setSystemUnderTests(systemUnderTest.getProject().getSystemUnderTests());

            HttpStatus statusCode = responseEntity.getStatusCode();
            if (!HttpStatus.OK.equals(statusCode)) {
                throw new LivingDocServerException(LivingDocServerErrorKey.CALL_FAILED,
                        "call was not successful, status: " + statusCode);
            }
            return responseEntity.getBody();
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
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

            Class<? extends SpecificationRunner> runnerClass = DocumentRunner.class;
            Class<? extends Report> reportClass = XmlReport.class;

            String[] sectionsArray = sections == null ? new String[]{} : sections.split(",");

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
        } catch (IOException e) {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        } catch (ClassNotFoundException e) {
            return Execution.error(specification, systemUnderTest, sections, ExceptionUtils.stackTrace(e, "<br>", 15));
        } catch (SAXException e) {
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
    public List<Object> marshallize() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(RUNNER_NAME_IDX, name);
        parameters.add(RUNNER_SERVER_NAME_IDX, StringUtils.stripToEmpty(serverName));
        parameters.add(RUNNER_SERVER_PORT_IDX, StringUtils.stripToEmpty(serverPort));
        parameters.add(RUNNER_CLASSPATH_IDX, new ArrayList<String>(classpaths));
        parameters.add(RUNNER_SECURED_IDX, isSecured());
        return parameters;
    }

    public String agentUrl() {
        return (isSecured() ? "https://" : "http://") + serverName + ":" + serverPort;
    }

    @Override
    public int compareTo(Runner runner) {
        return this.getName().compareTo(runner.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Runner)) {
            return false;
        }

        Runner runnerCompared = (Runner) o;
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
        return !StringUtils.isEmpty(serverName) && !StringUtils.isEmpty(serverPort);
    }

}
