/* Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */

package info.novatec.testit.livingdoc.maven;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;


/**
 * Base class for creating a jar from project classes.
 *
 * @author <a href="evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public abstract class AbstractJarMojo extends AbstractMojo {

    private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

    private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

    /**
     * Directory containing the generated JAR.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Name of the generated JAR.
     *
     * @parameter alias="jarName" expression="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * The Jar archiver.
     *
     * @parameter expression=
     * "${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver jarArchiver;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The maven archive configuration to use.
     *
     * See <a href=
     * "http://maven.apache.org/ref/current/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html"
     * >the Javadocs for MavenArchiveConfiguration</a>.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * Whether creating the archive should be forced.
     *
     * @parameter expression="${jar.forceCreation}" default-value="false"
     */
    private boolean forceCreation;

    /**
     * Return the specific output directory to serve as the root for the
     * archive.
     *
     * @return the class directory
     */
    protected abstract File getClassesDirectory();

    protected final MavenProject getProject() {
        return project;
    }

    /**
     * Overload this to produce a test-jar, for example.
     *
     * @return the classifier
     */
    protected abstract String getClassifier();

    protected static File getJarFile(File basedir, String finalName, String classifier) {

        if (StringUtils.isBlank(classifier)) {
            return new File(basedir, finalName + ".jar");
        }

        String modifiedClassifier = StringUtils.deleteWhitespace(classifier);
        if (modifiedClassifier.charAt(0) != '-') {
            modifiedClassifier = '-' + modifiedClassifier;
        }

        return new File(basedir, finalName + modifiedClassifier + ".jar");
    }

    /**
     * Generates the JAR.
     *
     * @return the created archive file
     * @throws MojoExecutionException if the assembling of the jar fails
     */
    public File createArchive() throws MojoExecutionException {
        File jarFile = getJarFile(outputDirectory, finalName, getClassifier());

        MavenArchiver archiver = new MavenArchiver();

        archiver.setArchiver(jarArchiver);

        archiver.setOutputFile(jarFile);

        archive.setForced(forceCreation);

        try {
            File contentDirectory = getClassesDirectory();
            if ( ! contentDirectory.exists()) {
                getLog().warn("JAR will be empty - no content was marked for inclusion!");
            } else {
                archiver.getArchiver().addDirectory(contentDirectory, DEFAULT_INCLUDES, DEFAULT_EXCLUDES);
            }

            archiver.createArchive(project, archive);

            return jarFile;
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Error assembling JAR", e);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error assembling JAR", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Error assembling JAR", e);
        } catch (ManifestException e) {
            throw new MojoExecutionException("Error assembling JAR", e);
        }
    }

    /**
     * Generates the JAR.
     */
    @Override
    public void execute() throws MojoExecutionException {
        File jarFile = createArchive();

        String classifier = getClassifier();
        if (classifier != null) {
            projectHelper.attachArtifact(getProject(), "jar", classifier, jarFile);
        } else {
            getProject().getArtifact().setFile(jarFile);
        }
    }
}
