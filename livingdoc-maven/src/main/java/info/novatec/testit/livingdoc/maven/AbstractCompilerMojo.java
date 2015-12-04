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

package info.novatec.testit.livingdoc.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.CompilationFailureException;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.Compiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerError;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.compiler.manager.CompilerManager;
import org.codehaus.plexus.compiler.manager.NoSuchCompilerException;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;


/**
 * @author others
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id: StaleSourceScannerTest.java 2393 2005-08-08 22:32:59Z kenney $
 */
public abstract class AbstractCompilerMojo extends AbstractMojo {
    // ----------------------------------------------------------------------
    // Configurables
    // ----------------------------------------------------------------------

    /**
     * Whether to include debugging information in the compiled class files. The
     * default value is true.
     * 
     * @parameter expression="${maven.compiler.debug}" default-value="true"
     * @deprecated see verbose
     */
    @Deprecated
    private boolean debug;

    /**
     * Whether to output messages about what the compiler is doing
     * 
     * @parameter default-value="false"
     */
    private boolean verbose;

    /**
     * Output source locations where deprecated APIs are used
     * 
     * @parameter
     */
    private boolean showDeprecation;

    /**
     * Optimize compiled code using the compiler's optimization methods
     * 
     * @parameter default-value="false"
     */
    private boolean optimize;

    /**
     * Output warnings
     * 
     * @parameter
     */
    private boolean showWarnings;

    /**
     * The -source argument for the Java compiler
     * 
     * @parameter
     */
    private String source;

    /**
     * The -target argument for the Java compiler
     * 
     * @parameter
     */
    private String target;

    /**
     * The -encoding argument for the Java compiler
     * 
     * @parameter
     */
    private String encoding;

    /**
     * The granularity in milliseconds of the last modification date for testing
     * whether a source needs recompilation
     * 
     * @parameter expression="${lastModGranularityMs}" default-value="0"
     */
    private int staleMillis;

    /**
     * The compiler id of the compiler to use.
     * 
     * @parameter default-value="javac"
     */
    private String compilerId;

    /**
     * Version of the compiler to use, ex. "1.3", "1.5"
     * 
     * @parameter
     */
    private String compilerVersion;

    /**
     * Runs the compiler in a separate process.
     * <p/>
     * If not set the compiler will default to a executable.
     * 
     * @parameter default-value="false"
     */
    private boolean fork;

    /**
     * The executable of the compiler to use.
     * 
     * @parameter
     */
    private String executable;

    /**
     * Arguments to be passed to the compiler if fork is set to true.
     * <p/>
     * This is because the list of valid arguments passed to a Java compiler
     * varies based on the compiler version.
     * 
     * @parameter
     */
    @SuppressWarnings("rawtypes")
    private Map compilerArguments;

    /**
     * Used to control the name of the output file when compiling a set of
     * sources to a single file.
     * 
     * @parameter expression="${project.build.finalName}"
     */
    private String outputFileName;

    // ----------------------------------------------------------------------
    // Read-only parameters
    // ----------------------------------------------------------------------

    /**
     * The directory to run the compiler from if fork is true.
     * 
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    private File basedir;

    /**
     * The target directory of the compiler if fork is true.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private File buildDirectory;

    /**
     * Plexus compiler manager.
     * 
     * @component
     */
    private CompilerManager compilerManager;

    protected abstract SourceInclusionScanner getSourceInclusionScanner(int scannerStaleMillis);

    protected abstract SourceInclusionScanner getSourceInclusionScanner(String inputFileEnding);

    protected abstract List<String> getClasspathElements();

    protected abstract List<String> getCompileSourceRoots();

    protected abstract File getOutputDirectory();

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute() throws MojoExecutionException, CompilationFailureException {
        // ----------------------------------------------------------------------
        // Look up the compiler. This is done before other code than can
        // cause the mojo to return before the lookup is done possibly resulting
        // in misconfigured POMs still building.
        // ----------------------------------------------------------------------

        Compiler compiler;

        getLog().debug("Using compiler '" + compilerId + "'.");

        try {
            compiler = compilerManager.getCompiler(compilerId);
        } catch (NoSuchCompilerException e) {
            throw new MojoExecutionException("No such compiler '" + e.getCompilerId() + "'.");
        }

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        List<String> compileSourceRoots = removeEmptyCompileSourceRoots(getCompileSourceRoots());

        if (compileSourceRoots.isEmpty()) {
            getLog().info("No sources to compile");

            return;
        }

        if (getLog().isDebugEnabled()) {
            getLog().debug("Source directories: " + compileSourceRoots.toString().replace(',', '\n'));
            getLog().debug("Classpath: " + getClasspathElements().toString().replace(',', '\n'));
            getLog().debug("Output directory: " + getOutputDirectory());
        }

        // ----------------------------------------------------------------------
        // Create the compiler configuration
        // ----------------------------------------------------------------------

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();

        compilerConfiguration.setOutputLocation(getOutputDirectory().getAbsolutePath());

        compilerConfiguration.setClasspathEntries(getClasspathElements());

        compilerConfiguration.setSourceLocations(compileSourceRoots);

        compilerConfiguration.setOptimize(optimize);

        compilerConfiguration.setDebug(debug);

        compilerConfiguration.setVerbose(verbose);

        compilerConfiguration.setShowWarnings(showWarnings);

        compilerConfiguration.setShowDeprecation(showDeprecation);

        compilerConfiguration.setSourceVersion(source);

        compilerConfiguration.setTargetVersion(target);

        compilerConfiguration.setSourceEncoding(encoding);

        if (compilerArguments != null) {
            LinkedHashMap<String, Object> cplrArgsCopy = new LinkedHashMap<String, Object>();
            for (Iterator<Map.Entry> i = compilerArguments.entrySet().iterator(); i.hasNext();) {
                Map.Entry me = i.next();
                String key = ( String ) me.getKey();
                if ( ! key.startsWith("-")) {
                    key = "-" + key;
                }
                cplrArgsCopy.put(key, me.getValue());
            }
            compilerConfiguration.setCustomCompilerArguments(cplrArgsCopy);
        }

        compilerConfiguration.setFork(fork);

        compilerConfiguration.setExecutable(executable);

        compilerConfiguration.setWorkingDirectory(basedir);

        compilerConfiguration.setCompilerVersion(compilerVersion);

        compilerConfiguration.setBuildDirectory(buildDirectory);

        compilerConfiguration.setOutputFileName(outputFileName);

        Set<String> staleSources;

        boolean canUpdateTarget;

        try {
            staleSources = computeStaleSources(compilerConfiguration, compiler, getSourceInclusionScanner(staleMillis));

            canUpdateTarget = compiler.canUpdateTarget(compilerConfiguration);

            if (compiler.getCompilerOutputStyle().equals(CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES)
                && ! canUpdateTarget) {
                getLog().info("RESCANNING!");
                String inputFileEnding = compiler.getInputFileEnding(compilerConfiguration);

                Set<String> sources = computeStaleSources(compilerConfiguration, compiler, getSourceInclusionScanner(
                    inputFileEnding));

                compilerConfiguration.setSourceFiles(sources);
            } else {
                compilerConfiguration.setSourceFiles(staleSources);
            }
        } catch (CompilerException e) {
            throw new MojoExecutionException("Error while computing stale sources.", e);
        }

        if (staleSources.isEmpty()) {
            getLog().info("Nothing to compile - all classes are up to date");

            return;
        }

        // ----------------------------------------------------------------------
        // Dump configuration
        // ----------------------------------------------------------------------

        if (getLog().isDebugEnabled()) {
            getLog().debug("Classpath:");

            for (Iterator<String> it = getClasspathElements().iterator(); it.hasNext();) {
                String s = it.next();

                getLog().debug(" " + s);
            }

            getLog().debug("Source roots:");

            for (Iterator<String> it = getCompileSourceRoots().iterator(); it.hasNext();) {
                String root = it.next();

                getLog().debug(" " + root);
            }

            if (fork) {
                try {
                    String[] cl = compiler.createCommandLine(compilerConfiguration);
                    if (cl != null && cl.length > 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(cl[0]);
                        for (int i = 1; i < cl.length; i ++ ) {
                            sb.append(" ");
                            sb.append(cl[i]);
                        }
                        getLog().debug("Command line options:");
                        getLog().debug(sb);
                    }
                } catch (CompilerException ce) {
                    getLog().debug(ce);
                }
            }
        }

        // ----------------------------------------------------------------------
        // Compile!
        // ----------------------------------------------------------------------

        List<CompilerError> messages;

        try {
            messages = compiler.compile(compilerConfiguration);
        } catch (Exception e) {
            throw new MojoExecutionException("Fatal error compiling", e);
        }

        boolean compilationError = false;

        for (Iterator<CompilerError> i = messages.iterator(); i.hasNext();) {
            CompilerError message = i.next();

            if (message.isError()) {
                compilationError = true;
            }
        }

        if (compilationError) {
            throw new CompilationFailureException(messages);
        }
        for (Iterator<CompilerError> i = messages.iterator(); i.hasNext();) {
            CompilerError message = i.next();

            getLog().warn(message.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> computeStaleSources(CompilerConfiguration compilerConfiguration, Compiler compiler,
        SourceInclusionScanner scanner) throws MojoExecutionException, CompilerException {
        CompilerOutputStyle outputStyle = compiler.getCompilerOutputStyle();

        SourceMapping mapping;

        File outputDirectory;

        if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE) {
            mapping = new SuffixMapping(compiler.getInputFileEnding(compilerConfiguration), compiler.getOutputFileEnding(
                compilerConfiguration));

            outputDirectory = getOutputDirectory();
        } else if (outputStyle == CompilerOutputStyle.ONE_OUTPUT_FILE_FOR_ALL_INPUT_FILES) {
            mapping = new SingleTargetSourceMapping(compiler.getInputFileEnding(compilerConfiguration), compiler
                .getOutputFile(compilerConfiguration));

            outputDirectory = buildDirectory;
        } else {
            throw new MojoExecutionException("Unknown compiler output style: '" + outputStyle + "'.");
        }

        scanner.addSourceMapping(mapping);

        Set<String> staleSources = new HashSet<String>();

        for (Iterator<String> it = getCompileSourceRoots().iterator(); it.hasNext();) {
            String sourceRoot = it.next();

            File rootFile = new File(sourceRoot);

            if ( ! rootFile.isDirectory()) {
                continue;
            }

            try {
                staleSources.addAll(scanner.getIncludedSources(rootFile, outputDirectory));
            } catch (InclusionScanException e) {
                throw new MojoExecutionException("Error scanning source root: \'" + sourceRoot + "\' "
                    + "for stale files to recompile.", e);
            }
        }

        return staleSources;
    }

    private static List<String> removeEmptyCompileSourceRoots(List<String> compileSourceRootsList) {
        List<String> newCompileSourceRootsList = new ArrayList<String>();
        if (compileSourceRootsList != null) {
            // copy as I may be modifying it
            for (Iterator<String> i = compileSourceRootsList.iterator(); i.hasNext();) {
                String srcDir = i.next();
                if ( ! newCompileSourceRootsList.contains(srcDir) && new File(srcDir).exists()) {
                    newCompileSourceRootsList.add(srcDir);
                }
            }
        }
        return newCompileSourceRootsList;
    }
}
