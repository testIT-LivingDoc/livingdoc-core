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

public class BooleanConverter extends AbstractTypeConverter {
    private static final String YES = "yes";
    private static final String NO = "no";

    @Override
    protected Object doConvert(String value) {
        if (YES.equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }

        if (NO.equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }

        return Boolean.valueOf(value);
    }

    @Override
    public boolean canConvertTo(Class< ? > type) {
        return Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type);
    }
}
