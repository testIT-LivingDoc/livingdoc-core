package info.novatec.testit.livingdoc.util.cmdline;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.novatec.testit.livingdoc.client.cli.CommandLineBuilder;


public class CommandLineBuilderTest {
    @Test
    public void testThatParametersAreSetterdInTheCommandLineTemplate() {
        CommandLineBuilder cmd = new CommandLineBuilder(
            "java -mx252m -cp ${classpaths} ${mainClass} ${inputPath} ${outputPath} -l ${locale} -r ${repository} -f ${fixtureFactory} -pdd ${projectDependencyDescriptor} --xml");
        List<String> dependencies = new ArrayList<String>();
        dependencies.add("DEPENDENCY-1");
        dependencies.add("DEPENDENCY-2");
        cmd.setDependencies(dependencies);
        cmd.setMainClass("MAINCLASS");
        cmd.setInputPath("INPUT");
        cmd.setOutpuPath("OUTPUT");
        cmd.setLocale("EN");
        cmd.setRepository("REPOSITORY");
        cmd.setFixtureFactory("FIXTURECLASS;FIXTUREARGS");
        cmd.setProjectDependencyDescriptor("PROJECT-DEPENDENCY-DESCRIPTOR");

        String[] cmdLine = cmd.getCmdLine();
        assertEquals("java", cmdLine[0]);
        assertEquals("-mx252m", cmdLine[1]);
        assertEquals("-cp", cmdLine[2]);
        assertEquals("DEPENDENCY-1" + File.pathSeparator + "DEPENDENCY-2" + File.pathSeparator, cmdLine[3]);
        assertEquals("MAINCLASS", cmdLine[4]);
        assertEquals("INPUT", cmdLine[5]);
        assertEquals("OUTPUT", cmdLine[6]);
        assertEquals("-l", cmdLine[7]);
        assertEquals("EN", cmdLine[8]);
        assertEquals("-r", cmdLine[9]);
        assertEquals("REPOSITORY", cmdLine[10]);
        assertEquals("-f", cmdLine[11]);
        assertEquals("FIXTURECLASS;FIXTUREARGS", cmdLine[12]);
        assertEquals("-pdd", cmdLine[13]);
        assertEquals("PROJECT-DEPENDENCY-DESCRIPTOR", cmdLine[14]);
        assertEquals("--xml", cmdLine[15]);
    }

    @Test
    public void testThatWeCanAddSectionsAtTheEndOfTheCmdLine() {
        CommandLineBuilder cmd = new CommandLineBuilder("java class");
        cmd.setSections("SECTIONS");

        String[] cmdLine = cmd.getCmdLine();
        assertEquals("-t", cmdLine[2]);
        assertEquals("SECTIONS", cmdLine[3]);
    }

    @Test
    public void testThatSectionsAreNotAddedAtTheEndOfTheCmdLineIfEmpty() {
        CommandLineBuilder cmd = new CommandLineBuilder("java class");

        cmd.setSections(null);
        assertEquals(2, cmd.getCmdLine().length);
        cmd.setSections("");
        assertEquals(2, cmd.getCmdLine().length);
    }
}
