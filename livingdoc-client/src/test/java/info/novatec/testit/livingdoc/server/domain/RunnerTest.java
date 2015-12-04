package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import info.novatec.testit.livingdoc.util.URIUtil;


public class RunnerTest {
    private static String EN = Locale.ENGLISH.getLanguage();
    private static File input;
    private static File classpathDirectory;

    @BeforeClass
    public static void beforeClass() throws URISyntaxException {
        // Using from livingdoc-core test jar.
        URI specsUri = Object.class.getResource("/ACalculatorSample.html").toURI();
        input = new File(specsUri);

        URI classpathUri = Object.class.getResource("/classpath").toURI();
        classpathDirectory = new File(classpathUri);
    }

    @Test
    public void testThatRunnerIsProperlyMarshalized() {
        Runner runner = Runner.newInstance("RUNNER-1");
        runner.setServerName("SERVER_NAME");
        runner.setServerPort("SERVER_PORT");
        ClasspathSet classPaths = new ClasspathSet();
        classPaths.add("CLASSPATH-1");
        classPaths.add("CLASSPATH-2");
        runner.setClasspaths(classPaths);
        runner.setSecured(true);

        Vector<Object> params = new Vector<Object>();
        params.add(XmlRpcDataMarshaller.RUNNER_NAME_IDX, "RUNNER-1");
        Vector<Object> envType = new Vector<Object>();
        envType.add(0, "ENVTYPE-1");
        params.add(XmlRpcDataMarshaller.RUNNER_SERVER_NAME_IDX, "SERVER_NAME");
        params.add(XmlRpcDataMarshaller.RUNNER_SERVER_PORT_IDX, "SERVER_PORT");
        Vector<Object> cp = new Vector<Object>();
        cp.add(0, "CLASSPATH-1");
        cp.add(1, "CLASSPATH-2");
        params.add(XmlRpcDataMarshaller.RUNNER_CLASSPATH_IDX, cp);
        params.add(XmlRpcDataMarshaller.RUNNER_SECURED_IDX, true);

        assertEquals(params, runner.marshallize());
    }

    @Test
    public void testThatWeCanExecuteASpecificationForAGivenSut() {
        Runner runner = Runner.newInstance("RUNNER");
        ClasspathSet classPaths = new ClasspathSet();
        classPaths.add(getRootPath());
        runner.setClasspaths(classPaths);

        Execution exe = runner.execute(getSpecification(), getSystemUnderTest(), true, null, EN);
        assertEquals(4, exe.getSuccess());
        assertEquals(1, exe.getFailures());
        assertEquals(1, exe.getErrors());
    }

    @Test
    public void testThatAnExecutionErrorIsRecievedIfAnErrorOccuresInTheExecution() {
        Runner runner = Runner.newInstance("RUNNER");
        ClasspathSet classPaths = new ClasspathSet();
        classPaths.add(getRootPath());
        runner.setClasspaths(classPaths);

        Execution exe = runner.execute(getInvalidSpecification(), getSystemUnderTest(), true, null, EN);
        assertTrue( ! StringUtils.isEmpty(exe.getExecutionErrorId()));
        assertTrue(exe.getExecutionErrorId().indexOf("Document not found") > 0);
    }

    @Test
    public void testAgentUrlIsBuildCorrectly() {
        Runner runner = Runner.newInstance("RUNNER");

        runner.setServerName("localhost");
        runner.setServerPort("port");
        assertEquals("http://localhost:port", runner.agentUrl());

        runner.setSecured(true);
        assertEquals("https://localhost:port", runner.agentUrl());
    }

    @Test
    public void testClassPathWithWildcardIsBuildCorrectlyUsingAllFiles() {
        String wildcard = classpathDirectory.getAbsolutePath() + "/*";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        Collection<String> resolvedClassPaths = runner.resolveClassPathsWildcards(runner.getClasspaths());

        assertEquals(5, resolvedClassPaths.size());
    }

    @Test
    public void testClassPathWithWildcardIsBuildCorrectlyUsingJarFiles() {
        String wildcard = classpathDirectory.getAbsolutePath() + "/*.jar";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        Collection<String> resolvedClassPaths = runner.resolveClassPathsWildcards(runner.getClasspaths());

        assertEquals(2, resolvedClassPaths.size());
    }

    @Test
    public void testClassPathWithWildcardIsBuildCorrectlyUsingSimiliarEnding() {
        String wildcard = classpathDirectory.getAbsolutePath() + "/*.htm";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        Collection<String> resolvedClassPaths = runner.resolveClassPathsWildcards(runner.getClasspaths());

        assertEquals(1, resolvedClassPaths.size());
    }

    @Test
    public void testClassPathWithWildcardIsBuildCorrectlyUsingRelativePath() {
        String wildcard = classpathDirectory.getAbsolutePath() + "/*.html";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        Collection<String> resolvedClassPaths = runner.resolveClassPathsWildcards(runner.getClasspaths());

        assertEquals(2, resolvedClassPaths.size());
    }

    @Test
    public void testClassPathWithWildcardIsNotBuildUsingNonExistingDirectory() {
        String wildcard = "test/*.html";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        try {
            runner.resolveClassPathsWildcards(runner.getClasspaths());
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testClassPathWithWildcardIsNotBuildUsingNonParentDirectory() {
        String wildcard = "*.html";

        Runner runner = Runner.newInstance("RUNNER");
        runner.getClasspaths().add(wildcard);

        try {
            runner.resolveClassPathsWildcards(runner.getClasspaths());
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    private String getRootPath() {
        String path = input.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile()
            .getAbsolutePath();
        return URIUtil.decoded(path);
    }

    private Specification getSpecification() {
        Specification spec = Specification.newInstance("ACalculatorSample.html");
        spec.setRepository(getRepository());

        return spec;
    }

    private Specification getInvalidSpecification() {
        Specification spec = Specification.newInstance("AInvalidCalculatorSample.html");
        spec.setRepository(getRepository());

        return spec;
    }

    private Repository getRepository() {
        Repository repo = Repository.newInstance("REPOSITORY");
        repo.setType(getFileType());
        repo.setBaseTestUrl(input.getParentFile().getAbsolutePath());
        repo.setUid("UID");

        return repo;
    }

    private RepositoryType getFileType() {
        RepositoryType type = RepositoryType.newInstance("FILE");
        type.setTestUrlFormat("%s%s");
        type.setClassName(FileSystemRepository.class.getName());
        return type;
    }

    private SystemUnderTest getSystemUnderTest() {
        return SystemUnderTest.newInstance("SUT");
    }

}
