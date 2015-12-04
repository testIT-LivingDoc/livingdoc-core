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
package info.novatec.testit.livingdoc.converter;

import org.apache.commons.lang3.StringUtils;


/**
 * Abstract base class for <code>TypeConverter</code> implementations that takes
 * care handling null or empty string values. Subclasses are guaranteed to
 * receive a non-null and non-empty string in their <code>doConvert()</code>
 * implementations.
 * 
 * @version $Revision: $ $Date: $
 */
public abstract class AbstractTypeConverter implements TypeConverter {
    /**
     * Template implementation of the <code>convert()</code> method. Takes care
     * of handling null and empty string values. Once these basic operations are
     * handled, will delegate to the <code>doConvert()</code> method of
     * subclasses.
     * 
     * @param value The string value to convert
     * @param type The type to convert to
     * @return The converted value, or null
     */
    @Override
    public Object parse(String value, Class< ? > type) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return doConvert(value.trim(), type);
    }

    /**
     * Basic implementation of the <code>toString()</code> method. Takes care of
     * handling null values otherwise call the doToString of the object.
     * 
     * @param value The value to stringnified
     * @return The string representation of the value
     */
    @Override
    public String toString(Object value) {
        if (value == null) {
            return "";
        }
        return doToString(value);
    }

    /**
     * Subclass must implement this method to do the actual type conversion.
     * Subclass implementations are garanteed to receive a non-null and
     * non-empty string.
     * 
     * @param value The string value to convert
     * @return The converted value, or null
     */
    protected abstract Object doConvert(String value);

    /**
     * Do the conversion of the given value into the given type.
     * 
     * @param value The string value to convert
     * @param type The type to convert to
     * @return The converted value, or null
     */
    protected Object doConvert(String value, Class< ? > type) {
        return doConvert(value);
    }

    /**
     * Subclass must implement this method to overide the default call to the
     * toString method of the value. Subclass implementations are garanteed to
     * receive a non-null object.
     * 
     * @param value The string value to convert
     * @return The converted value, or null
     */
    protected String doToString(Object value) {
        return String.valueOf(value);
    }
}
