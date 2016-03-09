package info.novatec.testit.livingdoc.maven.plugin;

import static info.novatec.testit.livingdoc.util.CollectionUtil.toVector;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.xmlrpc.WebServer;
import org.jmock.Mock;
import org.jmock.core.Constraint;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.stub.ReturnStub;

import info.novatec.testit.livingdoc.repository.AtlassianRepository;
import info.novatec.testit.livingdoc.repository.FileSystemRepository;
import info.novatec.testit.livingdoc.util.URIUtil;


public class SpecificationDownloaderMojoTest extends AbstractMojoTestCase {

    private SpecificationDownloaderMojo mojo;
    private WebServer ws;
    private Mock handler;

    @Override
    protected void tearDown() throws Exception {
        stopWebServer();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        URL pomPath = SpecificationDownloaderMojoTest.class.getResource("pom-downloader.xml");
        mojo = ( SpecificationDownloaderMojo ) lookupMojo("freeze", URIUtil.decoded(pomPath.getPath()));

        mojo.pluginDependencies = new ArrayList<Artifact>();
        mojo.pluginDependencies.add(new DependencyArtifact("commons-codec", dependency("commons-codec-1.3.jar")));
        mojo.pluginDependencies.add(new DependencyArtifact("xmlrpc", dependency("xmlrpc-2.0.1.jar")));
    }

    private Repository createLocalRepository(String name) {
        Repository repository = new Repository();
        repository.setName(name);
        repository.setType(FileSystemRepository.class.getName());
        repository.setRoot(localPath());
        mojo.addRepository(repository);
        return repository;
    }

    private String localPath() {
        return localDir().getAbsolutePath();
    }

    private File localDir() {
        return spec("spec.html").getParentFile();
    }

    private File dependency(String name) {
        return new File(classesOutputDir(), name);
    }

    private File classesOutputDir() {
        return localDir().getParentFile().getParentFile().getParentFile().getParentFile();
    }

    public void testCanDownloadASingleSpecification() throws Exception {
        createLocalRepository("repo").addTest("spec.html");
        mojo.execute();

        assertDownloaded("repo", "spec.html");
    }

    public void testCanDownloadASuiteOfSpecifications() throws Exception {
        createLocalRepository("repo").addSuite("/");
        try {
            mojo.execute();
        } catch (MojoFailureException ignored) {
            // No implementation needed.
        }
        assertDownloaded("repo", "spec.html");
        assertDownloaded("repo", "right.html");
        assertDownloaded("repo", "wrong.html");
        assertDownloaded("repo", "guice.html");
    }

    public void testSupportsMultipleRepositories() throws Exception {
        createLocalRepository("first").addTest("right.html");
        createLocalRepository("second").addTest("wrong.html");
        try {
            mojo.execute();
        } catch (MojoFailureException ignored) {
            // No implementation needed.
        }
        assertDownloaded("first", "right.html");
        assertDownloaded("second", "wrong.html");
    }

    @SuppressWarnings("unchecked")
    public void testShouldSupportCustomRepositoriesSuchAsAtlassian() throws Exception {
        startWebServer();
        List< ? > expected = toVector("SPACE", "PAGE", Boolean.TRUE, Boolean.TRUE);
        String right = FileUtils.readFileToString(spec("spec.html"), "UTF-8");
        handler.expects(new InvokeOnceMatcher()).method("getRenderedSpecification").with(eq(""), eq(""), eq(expected)).will(
            new ReturnStub(right));

        createAtlassianRepository("repo").addTest("PAGE");
        mojo.execute();

        handler.verify();
        assertDownloaded("repo", "PAGE.html");
    }

    private Repository createAtlassianRepository(String name) {
        Repository repository = new Repository();
        repository.setName(name);
        repository.setType(AtlassianRepository.class.getName());
        repository.setRoot("http://localhost:19005/rpc/xmlrpc?includeStyle=true&handler=livingdoc1#SPACE");
        mojo.addRepository(repository);
        return repository;
    }

    private Constraint eq(Object o) {
        return new IsEqual(o);
    }

    public void testShouldMakeBuildFailIfSomeSpecsCouldNotBeDownloaded() throws Exception {
        Repository repository = createLocalRepository("repo");
        repository.addTest("no_such_file.html");
        mojo.addRepository(repository);
        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException expected) {
            assertTrue(true);
        }
    }

    private File output(String repo, String name) {
        return new File(new File(mojo.specsDirectory, repo), URIUtil.flatten(name));
    }

    private File spec(String name) {
        return new File(URIUtil.decoded(SpecificationRunnerMojoTest.class.getResource(name).getPath()));
    }

    private void assertDownloaded(String repo, String name) {
        File out = output(repo, name);
        assertTrue(out.exists());
        long length = out.length();
        out.delete();
        assertTrue(length > 0);
    }

    private void startWebServer() {
        ws = new WebServer(19005);
        handler = new Mock(Handler.class);
        ws.addHandler("livingdoc1", handler.proxy());
        ws.start();
    }

    private void stopWebServer() {
        if (ws != null) {
            ws.shutdown();
        }
    }

    public static interface Handler {

        String getRenderedSpecification(String username, String password, Vector<Object> args);
    }
}
