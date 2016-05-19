/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.agent.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;


public class AgentConfigurationTest {

    @Test(expected = FileNotFoundException.class)
    public void testReadingConfigurationWithUnexistingConfigurationFile() throws Exception {
        new AgentConfiguration("-config", "UnexistingConfigurationFile");
    }

    @Test
    public void testReadingDefaultConfigurationFile() throws Exception {
        AgentConfiguration configuration = new AgentConfiguration();

        assertEquals(7777, configuration.getPort());
        assertFalse(configuration.isSecured());
        assertNull(configuration.getKeyStore());
        assertNull(configuration.getKeyStorePassword());
    }

    @Test
    public void testReadingConfigurationUnsecured() throws Exception {
        AgentConfiguration configuration = getConfiguration("/testReadingConfigurationUnsecured.properties");

        assertEquals(9876, configuration.getPort());
        assertFalse(configuration.isSecured());
        assertNull(configuration.getKeyStore());
        assertNull(configuration.getKeyStorePassword());
    }

    @Test
    public void testReadingConfigurationSecuredWithValidKeyStoreProperties() throws Exception {
        AgentConfiguration configuration = getConfiguration(
            "/testReadingConfigurationSecuredWithValidKeyStoreProperties.properties");

        assertEquals(9876, configuration.getPort());
        assertTrue(configuration.isSecured());
        assertEquals("src/test/resources/test.keystore", configuration.getKeyStore());
        assertEquals("gr33np3pp3r", configuration.getKeyStorePassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadingConfigurationSecuredButWithNoKeyStore() throws Exception {
        AgentConfiguration configuration = getConfiguration("/testReadingConfigurationSecuredButWithNoKeyStore.properties");

        assertEquals(9876, configuration.getPort());
        assertTrue(configuration.isSecured());
        assertNull(configuration.getKeyStorePassword());

        configuration.getKeyStore();
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingConfigurationSecuredButWithBadKeyStoreFileLocation() throws Exception {
        AgentConfiguration configuration = getConfiguration(
            "/testReadingConfigurationSecuredButWithBadKeyStoreFileLocation.properties");

        assertEquals(9876, configuration.getPort());
        assertTrue(configuration.isSecured());
        assertNull(configuration.getKeyStorePassword());

        configuration.getKeyStore();
    }

    @Test
    public void testReadingConfigurationUnecuredShouldReturnNullForKeyStoreProperties() throws Exception {
        AgentConfiguration configuration = getConfiguration(
            "/testReadingConfigurationUnecuredShouldReturnNullForKeyStoreProperties.properties");

        assertEquals(9876, configuration.getPort());
        assertFalse(configuration.isSecured());
        assertNull(configuration.getKeyStore());
        assertNull(configuration.getKeyStorePassword());
    }

    private AgentConfiguration getConfiguration(String file) throws IOException {
        File testFile = new File(AgentConfigurationTest.class.getResource(file).getPath());

        return new AgentConfiguration("-config", testFile.getAbsolutePath());
    }
}
