package info.novatec.testit.livingdoc.server;

/**
 * ServerPropertiesManager. For server properties persistence. Copyright (c)
 * 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */
public interface ServerPropertiesManager {
    public final static String URL = "LIVINGDOC_URL";
    public final static String HANDLER = "LIVINGDOC_HANDLER";
    public final static String PROJECT = "LIVINGDOC_PROJECT";
    public final static String SEQUENCE = "livingdoc.";

    /**
     * Retrieves the property for the specified key and identifier.
     * </p>
     * 
     * @param key The key of the property to retrieve
     * @param identifier The identifier of the property to retrieve
     * @return the property for the specified key and params.
     */
    public String getProperty(String key, String identifier);

    /**
     * Saves the property value for the specified key and identifier.
     * </p>
     * 
     * @param key The key of the property to set
     * @param value The value of the property to set
     * @param identifier The identifier of the property to set
     */
    public void setProperty(String key, String value, String identifier);
}
