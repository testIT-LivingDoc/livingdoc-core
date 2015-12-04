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
 * Implementation of <code>TypeConverter</code> that converts strings to
 * <code>Long</code> objects.
 * 
 * @version $Revision: $ $Date: $
 */
public class LongConverter extends AbstractTypeConverter {
    @Override
    protected Object doConvert(String value) {
        return Long.valueOf(value);
    }

    @Override
    public boolean canConvertTo(Class< ? > type) {
        return Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type);
    }
}
