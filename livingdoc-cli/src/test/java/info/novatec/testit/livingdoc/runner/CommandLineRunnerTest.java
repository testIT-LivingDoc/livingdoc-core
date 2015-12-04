/* Copyright (c) 2006 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. /* Copyright (c) 2006 Pyxis Technologies inc.
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

package info.novatec.testit.livingdoc.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.AlternateCalculator;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ExceptionUtils;


public class CommandLineRunnerTest {
    private File outputDir;

    @Before
    public void setUp() throws Exception {
        createOutputDirectory();
    }

    @After
    public void tearDown() throws Exception {
        deleteOutputDirectory();
    }

    @Test
    public void testCanRunASingleSpecificationAndProduceAReportFile() throws Exception {
        String input = getResourcePath("/specs/ABankSample.html");
        File outputFile = outputFile("report.html");

        new CommandLineRunner().run(input, outputFile.getAbsolutePath());

        assertFile(outputFile);
    }

    @Test
    public void testCanGenerateAUniqueReportFileNameFromSpecificationName() throws Exception {
        String input = getResourcePath("/specs/ABankSample.html");
        File expectedOutputFile = outputFile("ABankSample.html.xml");

        new CommandLineRunner().run("--xml", "-o", outputDir.getAbsolutePath(), input);
        assertFile(expectedOutputFile);
    }

    @Test
    public void testCanRunASuiteOfSpecificationsAndGenerateReports() throws Exception {
        String input = getResourcePath("/specs");
        new CommandLineRunner().run("-s", input, outputDir.getAbsolutePath());

        assertTrue(outputDir.isDirectory());
        assertEquals(3, outputDir.listFiles().length);
    }

    @Test
    public void testThatFixtureFactoryCanBeSpecifiedAlongWithArguments() throws Exception {
        File reportFile = outputFile("report.xml");
        String[] args = new String[] { "-f", CustomSystemUnderDevelopment.class.getName() + ";param1;param2", "--xml",
                getResourcePath("/WithACustomFixtureFactory.html"), reportFile.getAbsolutePath() };

        new CommandLineRunner().run(args);
        XmlReport parser = XmlReport.parse(new FileReader(reportFile));
        assertEquals(1, parser.getSuccess(0));
        assertEquals(1, parser.getFailure(0));
    }

    @Test
    public void testThatDebugModeAllowToSeeWholeStackTrace() throws Exception {

        String input = getResourcePath("/specs/ABankSample.html");
        File outputFile = outputFile("report.html");

        CommandLineRunner commandLineRunner = new CommandLineRunner();
        commandLineRunner.run("--debug", input, outputFile.getAbsolutePath());

        try {
            throw new Exception(new Throwable(""));
        } catch (Exception e) {
            assertTrue(countLines(ExceptionUtils.stackTrace(e.getCause(), "\n", 2)) > 3);
        }

    }

    private int countLines(String val) {
        return StringUtils.countMatches(val, "\n");
    }

    private void createOutputDirectory() {
        outputDir = new File(System.getProperty("java.io.tmpdir"), "specs");
    }

    private void deleteOutputDirectory() throws IOException {
        if (outputDir != null) {
            FileUtils.deleteDirectory(outputDir);
        }
    }

    private void assertFile(File file) {
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    private File outputFile(String fileName) {
        return new File(outputDir, fileName);
    }

    private String getResourcePath(String name) {
        return CommandLineRunnerTest.class.getResource(name).getPath();
    }

    public static class CustomSystemUnderDevelopment extends DefaultSystemUnderDevelopment {

        @SuppressWarnings("unused")
        public CustomSystemUnderDevelopment(String... params) {
            // No implementation needed.
        }

        @Override
        public Fixture getFixture(String name, String... params) throws Exception {
            return new PlainOldFixture(classForName(name).newInstance());
        }

        @Override
        public void addImport(String packageName) {
            // No implementation needed.
        }

        public Class< ? > classForName(String fixtureName) throws Exception {
            if (fixtureName.equals("MyFixture")) {
                return AlternateCalculator.class;
            }
            throw new Exception();
        }

    }
}
