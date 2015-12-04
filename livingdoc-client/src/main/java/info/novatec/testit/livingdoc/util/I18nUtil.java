package info.novatec.testit.livingdoc.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Util class for internalisation. JIRA dependend.
 * <p/>
 * Copyright (c) 2005 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author jchuet
 */
public final class I18nUtil {
    private I18nUtil() {
    }

    /**
     * Custom I18n. Based on WebWork i18n.
     * 
     * @param key The key of the i18n message to retrieve
     * @param bundle The bundle of the i18n message to retrieve
     * @return the i18n message. If none found or no bundle is given key is
     * returned.
     */
    public static String getText(String key, ResourceBundle bundle) {
        if (key == null || bundle == null || bundle.containsKey(key) == false) {
            return key;
        }
        return bundle.getString(key);
    }

    public static String getText(String key, ResourceBundle bundle, Object... arguments) {
        String message = getText(key, bundle);
        return MessageFormat.format(message, arguments);
    }

    public static ResourceBundle getResourceBundle(String bundleName, Locale locale) {
        return ResourceBundle.getBundle(bundleName, locale);
    }
}
