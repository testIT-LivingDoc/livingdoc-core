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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AgentConfiguration {

    private final static int DEFAULT_PORT = 56000;

    private final static String DEFAULT_CONFIG_FILE = "remoteagent.properties";

    private final static String AGENT_KEY_NAME = "livingdoc.remoteagent.";
    private final static String PORT_PROPERTY_NAME = AGENT_KEY_NAME + "port";
    private final static String SECURED_PROPERTY_NAME = AGENT_KEY_NAME + "secured";
    private final static String KEYSTORE_PROPERTY_NAME = AGENT_KEY_NAME + "keystore.file";
    private final static String KEYSTORE_PASSWORD_PROPERTY_NAME = AGENT_KEY_NAME + "keystore.password";

    private static final Logger log = LogManager.getLogger(AgentConfiguration.class);

    private final int port;
    private final boolean secured;
    private final String keyStore;
    private final String keyStorePassword;

    public AgentConfiguration(String... args) throws IOException {
        ComandLineHelper commandLineHelper = new ComandLineHelper(args);

        Properties properties = loadProperties(commandLineHelper.getConfig());

        this.port = getPort(properties, commandLineHelper.getPort(DEFAULT_PORT));
        this.secured = isSecured(properties, commandLineHelper.isSecured());
        this.keyStore = getKeyStore(properties, commandLineHelper.getKeyStore());
        this.keyStorePassword = getKeyStorePassword(properties);
    }

    public int getPort() {
        return port;
    }

    public boolean isSecured() {
        return secured;
    }

    public String getKeyStore() throws IOException {
        if ( ! isSecured()) {
            return null;
        }

        if (keyStore == null) {
            throw new IllegalArgumentException("You must specify keystore file location");
        }

        final File keyStoreFile = new File(keyStore);

        if ( ! keyStoreFile.exists()) {
            throw new FileNotFoundException(String.format("KeyStore file '%s' does not exists", keyStoreFile
                .getAbsolutePath()));
        }

        return keyStore;
    }

    public String getKeyStorePassword() {
        if ( ! isSecured()) {
            return null;
        }

        return keyStorePassword;
    }

    private Properties loadProperties(String config) throws IOException {
        Properties properties = new Properties();

        if (config == null) {
            File configFile = new File(DEFAULT_CONFIG_FILE);

            if (configFile.exists()) {
                loadProperties(properties, configFile);
            } else {
                log.info("Default configuration file not found.  Skip.");
            }
        } else {
            File configFile = new File(config);

            if ( ! configFile.exists()) {
                throw new FileNotFoundException(String.format("Configuration file '%s' does not exist", configFile
                    .getAbsolutePath()));
            }

            loadProperties(properties, configFile);
        }

        return properties;
    }

    private void loadProperties(Properties properties, File filename) throws IOException {
        log.info(String.format("Reading configuration file '%s'...", filename.getAbsolutePath()));

        FileInputStream fis = new FileInputStream(filename);
        properties.load(fis);
        fis.close();
    }

    private int getPort(Properties properties, int defaultValue) {
        return Integer.parseInt(properties.getProperty(PORT_PROPERTY_NAME, String.valueOf(defaultValue)));
    }

    private boolean isSecured(Properties properties, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(SECURED_PROPERTY_NAME, String.valueOf(defaultValue)));
    }

    private String getKeyStore(Properties properties, String defaultValue) {
        return StringUtils.stripToNull(properties.getProperty(KEYSTORE_PROPERTY_NAME, defaultValue));
    }

    private String getKeyStorePassword(Properties properties) {
        return StringUtils.stripToNull(properties.getProperty(KEYSTORE_PASSWORD_PROPERTY_NAME, null));
    }
}
