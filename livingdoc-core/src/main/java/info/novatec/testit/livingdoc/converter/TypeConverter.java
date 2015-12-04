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

/**
 * Interface defining a method to convert a <code>String</code> value to an
 * object of another type.
 * 
 * @version $Revision: $ $Date: $
 */
public interface TypeConverter {
    boolean canConvertTo(Class< ? > type);

    /**
     * Converts <code>value</code> to an object of the type handled by the
     * subclass implementation.
     * 
     * @param value Value to be converted
     * @param type The type to convert to
     * @return Converted value
     */
    Object parse(String value, Class< ? > type);

    /**
     * Convert value to a string representation.
     * 
     * @param value Value to be converted
     * @return the string representation of value
     */
    String toString(Object value);
}
