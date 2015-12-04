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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;


public class ArrayConverterTest {
    private ArrayConverter converter;

    public ArrayConverterTest() {
        converter = new ArrayConverter(",");
    }

    @Test
    public void testShouldConvertCommaSeparatedListOfValuesToArrayOfAnyType() {
        assertTrue(Arrays.equals(new String[] { "a", "b", "c" }, ( Object[] ) converter.parse("a, b, c", String[].class)));
        assertTrue(Arrays.equals(new Integer[] { 1, 2, 3 }, ( Object[] ) converter.parse("1, 2, 3", Integer[].class)));
    }

    @Test
    public void testValuesCanBeWrappedInSquareBrackets() {
        assertTrue(Arrays.equals(new String[] { "a", "b", "c" }, ( Object[] ) converter.parse("[a, b, c]", String[].class)));
        assertTrue(Arrays.equals(new String[] {}, ( Object[] ) converter.parse("[]", String[].class)));
    }

    @Test
    public void testConvertsOnlyArraysWhoseComponentTypeIsSupported() {
        assertTrue(converter.canConvertTo(String[].class));
        assertFalse(converter.canConvertTo(Object.class));
        assertFalse(converter.canConvertTo(Object[].class));
    }

    @Test
    public void testCantConvertPrimitiveArraysType() {
        assertFalse(converter.canConvertTo(int.class));
        assertFalse(converter.canConvertTo(int[].class));
    }
}
