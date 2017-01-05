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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.CompilationFailureException;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;

import info.novatec.testit.livingdoc.maven.AbstractCompilerMojo;


/**
 * &#64;goal compile
 * &#64;phase pre-integration-test
 * &#64;requiresDependencyResolution compile
 * &#64;description Compiles fixture test sources
 */
public class FixtureCompilerMojo extends AbstractCompilerMojo {
    /**
     * Set this to 'true' to bypass livingdoc fixture compilation process
     * entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.livingdoc.test.skip}"
     * default-value="false"
     */
    private boolean skip;

    /**
     * The source directory containing the fixture sources to be compiled.
     * 
     * @parameter
     * @required
     */
    private File fixtureSourceDirectory;

    /**
     * Project fixture classpath.
     * 
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    /**
     * The directory where compiled fixture classes go.
     * 
     * @parameter expression="${project.build.directory}/fixture-test-classes"
     * @required
     */
    private File fixtureOutputDirectory;

    /**
     * A list of inclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set<String> testIncludes = new HashSet<String>();

    /**
     * A list of exclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set< ? > testExcludes = new HashSet<Object>();

    @Override
    public void execute() throws MojoExecutionException, CompilationFailureException {
        if (skip) {
            getLog().info("Not compiling fixture sources.");
        } else {
            getLog().info("Compiling fixture sources at " + fixtureSourceDirectory.getAbsolutePath());
            getLog().info("to directory " + fixtureOutputDirectory.getAbsolutePath());
            super.execute();
        }
    }

    @Override
    protected List<String> getCompileSourceRoots() {
        List<String> roots = new ArrayList<String>();
        roots.add(fixtureSourceDirectory.getAbsolutePath());
        return roots;
    }

    @Override
    protected List<String> getClasspathElements() {
        return classpathElements;
    }

    @Override
    protected File getOutputDirectory() {
        return fixtureOutputDirectory;
    }

    @Override
    protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
        SourceInclusionScanner scanner = null;

        if (testIncludes.isEmpty() && testExcludes.isEmpty()) {
            scanner = new StaleSourceScanner(staleMillis);
        } else {
            if (testIncludes.isEmpty()) {
                testIncludes.add("**/*.java");
            }
            scanner = new StaleSourceScanner(staleMillis, testIncludes, testExcludes);
        }

        return scanner;
    }

    @Override
    protected SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding) {
        SourceInclusionScanner scanner = null;

        if (testIncludes.isEmpty() && testExcludes.isEmpty()) {
            testIncludes = Collections.singleton("**/*." + inputFileEnding);
            scanner = new SimpleSourceInclusionScanner(testIncludes, Collections.EMPTY_SET);
        } else {
            if (testIncludes.isEmpty()) {
                testIncludes.add("**/*." + inputFileEnding);
            }
            scanner = new SimpleSourceInclusionScanner(testIncludes, testExcludes);
        }

        return scanner;
    }
}
