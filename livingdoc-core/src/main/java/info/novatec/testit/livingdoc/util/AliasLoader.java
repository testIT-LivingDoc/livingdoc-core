package info.novatec.testit.livingdoc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.LivingDoc;


/**
 * The {@link AliasLoader} is responsible for fetching aliases from predefined
 * property file locations.
 * 
 * <p>
 * There are two types of aliases which have predefined aliases:
 * </p>
 * <ul>
 * <li>Interpreters: <i>Defined by the interpreter class name in
 * <code>alias_interpreters.properties</code>.</i></li>
 * <li>Keywords: <i>Defined by simple keys in
 * <code>alias_keywords.properties</code>.</i></li>
 * </ul>
 * 
 * <p>
 * All aliases can be extended by placing a property file beside the jar which
 * contains this {@link AliasLoader}. The default aliases will be used as
 * fallback, so you're able to add only specific aliases.
 * </p>
 * 
 * <b>Note:</b> This class does not contain getters, since they are not needed
 * yet. If there is a reason, feel free to add these.
 */
public class AliasLoader {
    private final static String ALIAS_INTERPRETERS = "alias_interpreters";
    private final static String ALIAS_KEYWORDS = "alias_keywords";

    private Properties interpreterAliasesProperties;
    private Properties keywordAliasesProperties;

    /**
     * @see #get()
     */
    private AliasLoader() {
    }

    /**
     * Find the key for a given alias.
     * 
     * <p>
     * This method will stop after the first found key.
     * </p>
     * 
     * @return the key for the given alias or null if none alias is found.
     */
    public String getKeywordForAlias(String alias) {
        this.keywordAliasesProperties = loadAliasProperties(ALIAS_KEYWORDS);
        return getKeyForAlias(keywordAliasesProperties, alias);
    }

    /**
     * Checks whether the given alias relates to the given keyword.
     * 
     * <p>
     * <b>Note:</b> This check ignores case.
     * </p>
     * 
     * @return true if the keyword contains the given alias or if the alias and
     * the keyword are equal
     * 
     * @see #getKeywordForAlias(String)
     */
    public boolean isAliasForKeyword(String alias, String keyword) {
        if (keyword.equalsIgnoreCase(alias)) {
            return true;
        }

        this.keywordAliasesProperties = loadAliasProperties(ALIAS_KEYWORDS);
        Set<String> keywordAliases = getFormattedAliases(keywordAliasesProperties, keyword);
        return containsAlias(keywordAliases, alias);
    }

    /**
     * Returns all interpreter class names which are defined in the associated
     * interpreter properties file.
     */
    public Set<String> getInterpreterClassNames() {
        this.interpreterAliasesProperties = loadAliasProperties(ALIAS_INTERPRETERS);
        return interpreterAliasesProperties.stringPropertyNames();
    }

    /**
     * Returns the aliases (trimmed) for a specific interpreter (using the class
     * name).
     */
    public Set<String> getAliasesForInterpreterClassName(String interpreterClassName) {
        this.interpreterAliasesProperties = loadAliasProperties(ALIAS_INTERPRETERS);
        return getFormattedAliases(interpreterAliasesProperties, interpreterClassName);
    }

    /**
     * Search key for value.
     * 
     * <p>
     * Iterates through the comma separated aliases. Stops by the first found
     * key.
     * </p>
     */
    private String getKeyForAlias(Properties aliasProperties, String alias) {
        for (String key : aliasProperties.stringPropertyNames()) {
            if (key.equalsIgnoreCase(alias)) {
                return key;
            }

            Set<String> keyAliases = getFormattedAliases(aliasProperties, key);

            if (containsAlias(keyAliases, alias)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Strips unneeded spaces.
     */
    private Set<String> getFormattedAliases(Properties aliasProperties, String propertyKey) {
        Set<String> formattedAliases = new HashSet<String>();
        if (aliasProperties.containsKey(propertyKey)) {
            String aliasList = aliasProperties.getProperty(propertyKey);
            String[] aliases = StringUtils.split(aliasList, ",");
            for (String alias : aliases) {
                String formattedAlias = StringUtils.strip(alias);
                formattedAliases.add(formattedAlias);
            }
        }
        return formattedAliases;
    }

    private boolean containsAlias(Set<String> aliases, String alias) {
        for (String aliasToCompare : aliases) {
            if (aliasToCompare.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    private Properties loadAliasProperties(String fileName) {
        Properties empty = new Properties();
        Properties fallback = loadPropertiesFromInternalResource("/" + fileName + ".properties", empty);
        Properties customFallback = loadPropertiesFromInternalResource("/" + fileName + "_custom.properties", fallback);
        Properties aliasProperties = loadPropertiesFromExternalFile("./" + fileName + ".properties", customFallback);
        return aliasProperties;
    }

    /**
     * Loads properties from a external file. If the file does not exist, we
     * return the fallback.
     */
    private Properties loadPropertiesFromExternalFile(String filePath, Properties fallback) {
        Properties properties = new Properties(fallback);
        try {
            FileInputStream propertyFileStream = new FileInputStream(filePath);
            properties.load(propertyFileStream);
            return properties;
        } catch (NullPointerException e) {
            return fallback;
        } catch (FileNotFoundException e) {
            return fallback;
        } catch (IOException e) {
            return fallback;
        }
    }

    /**
     * Loads properties from the current class If the resource does not exist,
     * we return the fallback.
     */
    private Properties loadPropertiesFromInternalResource(String filePath, Properties fallback) {
        Properties properties = new Properties(fallback);
        try {
            InputStream propertyFileStream = LivingDoc.class.getResourceAsStream(filePath);
            properties.load(propertyFileStream);
            return properties;
        } catch (NullPointerException e) {
            return fallback;
        } catch (FileNotFoundException e) {
            return fallback;
        } catch (IOException e) {
            return fallback;
        }
    }

    private static AliasLoader instance;

    /**
     * Thread safe way to access the {@link AliasLoader} singleton instance.
     * 
     * @return a thread safe singleton instance of the {@link AliasLoader}.
     */
    public static synchronized AliasLoader get() {
        if (AliasLoader.instance == null) {
            AliasLoader.instance = new AliasLoader();
        }
        return AliasLoader.instance;
    }

}
