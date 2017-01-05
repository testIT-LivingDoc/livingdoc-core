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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.plugin.resources.ReflectionProperties;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * Copy application resources.
 * 
 * &#64;goal resources
 * &#64;phase pre-integration-test
 */
public class FixtureResourcesMojo extends AbstractMojo {

    /**
     * Set this to 'true' to bypass livingdoc fixture resources process
     * entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.livingdoc.test.skip}"
     * default-value="false"
     */
    private boolean skip;

    /**
     * @parameter
     */
    private String encoding;

    /**
     * The output directory into which to copy the resources.
     * 
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    private String fixtureOutputDirectory;

    /**
     * The list of resources we want to transfer.
     * 
     * @parameter
     * @required
     */
    private List<Resource> resources;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    private Properties filterProperties;

    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String[] DEFAULT_INCLUDES = { "**/**" };

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List<String> filters;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Not copying fixture resources.");
        } else {
            copyResources(resources, fixtureOutputDirectory);
        }
    }

    @SuppressWarnings("unchecked")
    protected void copyResources(List<Resource> copyResources, String outputDirectory) throws MojoExecutionException {
        initializeFiltering();

        if (encoding == null || encoding.length() < 1) {
            getLog().info("Using default encoding to copy filtered resources.");
        } else {
            getLog().info("Using encoding: \'" + encoding + "\' to copy filtered resources.");
        }

        for (Iterator<Resource> i = copyResources.iterator(); i.hasNext();) {
            Resource resource = i.next();

            String targetPath = resource.getTargetPath();

            File resourceDirectory = new File(resource.getDirectory());

            if ( ! resourceDirectory.exists()) {
                continue;
            }

            DirectoryScanner scanner = new DirectoryScanner();

            scanner.setBasedir(resource.getDirectory());
            if (resource.getIncludes() != null && ! resource.getIncludes().isEmpty()) {
                scanner.setIncludes(( String[] ) resource.getIncludes().toArray(EMPTY_STRING_ARRAY));
            } else {
                scanner.setIncludes(DEFAULT_INCLUDES);
            }
            if (resource.getExcludes() != null && ! resource.getExcludes().isEmpty()) {
                scanner.setExcludes(( String[] ) resource.getExcludes().toArray(EMPTY_STRING_ARRAY));
            }

            scanner.addDefaultExcludes();
            scanner.scan();

            List<String> includedFiles = Arrays.asList(scanner.getIncludedFiles());
            for (Iterator<String> j = includedFiles.iterator(); j.hasNext();) {
                String name = j.next();

                String destination = name;

                if (targetPath != null) {
                    destination = targetPath + "/" + name;
                }

                File source = new File(resource.getDirectory(), name);

                File destinationFile = new File(outputDirectory, destination);

                if ( ! destinationFile.getParentFile().exists()) {
                    destinationFile.getParentFile().mkdirs();
                }

                try {
                    copyFile(source, destinationFile, resource.isFiltering());
                } catch (IOException e) {
                    throw new MojoExecutionException("Error copying resources", e);
                }
            }
        }
    }

    private void initializeFiltering() throws MojoExecutionException {
        // System properties
        filterProperties = new Properties(System.getProperties());

        // Project properties
        filterProperties.putAll(project.getProperties());

        for (Iterator<String> i = filters.iterator(); i.hasNext();) {
            String filtersfile = i.next();

            try {
                Properties properties = PropertyUtils.loadPropertyFile(new File(filtersfile), true, true);

                filterProperties.putAll(properties);
            } catch (IOException e) {
                throw new MojoExecutionException("Error loading property file '" + filtersfile + "'", e);
            }
        }
    }

    private void copyFile(File from, File to, boolean filtering) throws IOException {
        if ( ! filtering) {
            if (to.lastModified() < from.lastModified()) {
                FileUtils.copyFile(from, to);
            }
        } else {
            // buffer so it isn't reading a byte at a time!
            Reader fileReader = null;
            Writer fileWriter = null;
            try {
                if (encoding == null || encoding.length() < 1) {
                    fileReader = new BufferedReader(new FileReader(from));
                    fileWriter = new FileWriter(to);
                } else {
                    FileInputStream instream = new FileInputStream(from);

                    FileOutputStream outstream = new FileOutputStream(to);

                    fileReader = new BufferedReader(new InputStreamReader(instream, encoding));

                    fileWriter = new OutputStreamWriter(outstream, encoding);
                }

                // support ${token}
                Reader reader = new InterpolationFilterReader(fileReader, filterProperties, "${", "}");

                // support @token@
                reader = new InterpolationFilterReader(reader, filterProperties, "@", "@");

                reader = new InterpolationFilterReader(reader, new ReflectionProperties(project), "${", "}");

                IOUtil.copy(reader, fileWriter);
            } finally {
                IOUtil.close(fileReader);
                IOUtil.close(fileWriter);
            }
        }
    }
}
