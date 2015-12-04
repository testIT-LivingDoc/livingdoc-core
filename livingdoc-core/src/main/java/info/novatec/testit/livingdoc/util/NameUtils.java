/* Copyright (c) 2006 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.util;

import java.beans.Introspector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;


public final class NameUtils {
    /**
     * No instance of this class is allowed.
     */
    private NameUtils() {
    }

    /**
     * Tries to convert <code>string</code> to a Java class name by following a
     * few simple steps:
     * <p/>
     * <p/>
     * <ul>
     * <li>First of all, <code>string</code> gets <i>camel cased</i>, the usual
     * Java class naming convention.
     * <li>Secondly, any whitespace, by virtue of
     * <code>Character.isWhitespace()</code> is removed from the camel cased
     * <code>string</code>.
     * <li>Third, all accented characters (diacritis) are replaced by their
     * non-accented equivalent (ex: \u00e9 -> e)</li>
     * <li>Fourth, all non java identifier characters are removed</li>
     * </ul>
     * <p/>
     * The only exception to executing these two steps is when
     * <code>string</code> represents a fully-qualified class name. To check if
     * <code>string</code> represents a fully-qualified class name, a simple
     * validation of the existence of the '.' character is made on
     * <code>string</code>.
     * 
     * @param s The String that may represent a Java class name
     * @return The input string converted by following the documented steps
     */
    public static String toClassName(String s) {
        if (StringUtils.isEmpty(s) || s.contains(".")) {
            return s;
        }
        return removeNonJavaIdentifierCharacters(StringUtils.stripAccents(StringUtils.deleteWhitespace(WordUtils
            .capitalizeFully(s))));
    }

    public static String toLowerCamelCase(String s) {
        if (StringUtils.isBlank(s)) {
            return s.trim();
        }

        return StringUtils.uncapitalize(StringUtils.deleteWhitespace(WordUtils.capitalizeFully(s)));
    }

    public static String humanize(String s) {

        StringBuilder literal = new StringBuilder();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i ++ ) {
            char c = chars[i];
            if (Character.isUpperCase(c) && i > 0) {
                literal.append(" ");
                literal.append(Character.toLowerCase(c));
            } else {
                literal.append(c);
            }
        }
        return literal.toString();
    }

    public static String decapitalize(String s) {
        StringBuilder sb = new StringBuilder();
        String[] parts = s.trim().split("\\s+");

        sb.append(Introspector.decapitalize(parts[0]));
        for (int i = 1; i < parts.length; ++ i) {
            sb.append(StringUtils.capitalize(Introspector.decapitalize(parts[i])));
        }
        return sb.toString();
    }

    public static boolean isJavaIdentifier(String s) {
        if ( ! Character.isJavaIdentifierStart(s.codePointAt(0))) {
            return false;
        }

        for (int i = 1; i < s.length(); i ++ ) {
            if ( ! Character.isJavaIdentifierPart(s.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String toJavaIdentifierForm(String name) {
        return removeNonJavaIdentifierCharacters(StringUtils.stripAccents(toLowerCamelCase(name)));
    }

    public static String removeNonJavaIdentifierCharacters(String name) {
        StringBuilder javaIdentifier = new StringBuilder();

        for (int index = 0; index < name.length(); index ++ ) {
            if (Character.isJavaIdentifierPart(name.codePointAt(index))) {
                javaIdentifier.append(name.charAt(index));
            }
        }

        return javaIdentifier.toString();
    }
}
