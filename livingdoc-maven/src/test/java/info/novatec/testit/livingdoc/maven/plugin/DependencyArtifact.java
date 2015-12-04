package info.novatec.testit.livingdoc.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.testing.stubs.ArtifactStub;


public class DependencyArtifact extends ArtifactStub {

    private String artifactId;
    private File file;

    public DependencyArtifact(String artifactId, File file) {
        this.artifactId = artifactId;
        this.file = file;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getType() {
        return "jar";
    }

    @Override
    public File getFile() {
        return file;
    }
}
