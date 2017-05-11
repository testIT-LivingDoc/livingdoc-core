package info.novatec.testit.livingdoc.server.rpc.runner;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.util.cli.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static org.junit.Assert.*;


public class CommandLineRemoteRunnerTest {

    private CommandLineRemoteRunner cli;

    @Before
    public void setUp() {
        cli = new CommandLineRemoteRunner();
    }

    @Test
    public void testSetCredentialsFromPropertiesFileIsOK() throws IOException, LivingDocServerException {

        String randomName = RandomStringUtils.randomAlphanumeric(10);
        Properties prop = new Properties();
        OutputStream output = null;
        File file = File.createTempFile(randomName, ".properties");
        try {
            output = new FileOutputStream(file);
            file.deleteOnExit();
            prop.setProperty("livingdoc.confluence.user", "user");
            prop.setProperty("livingdoc.confluence.password", "password");
            prop.store(output, null);

            cli.setCredentialsFromFile(file.getAbsolutePath());

            assertEquals(cli.getUser(), "user");
            assertEquals(cli.getPassword(), "password");

        } finally{
            output.close();
        }
    }


    @Test(expected = InvalidOptionException.class)
    public void testComplainsOfInvalidOptions() throws ParseException, IOException, LivingDocServerException {
        cli.run("-us");
    }

    @Test(expected = WrongOptionUsageException.class)
    public void testComplainsOfWrongOptions() throws ParseException, IOException, LivingDocServerException {
        cli.run("--user");
    }

    @Test(expected = ArgumentMissingException.class)
    public void testComplainsOfMissingOptions() throws ParseException, IOException, LivingDocServerException {
        cli.run("");
    }

}