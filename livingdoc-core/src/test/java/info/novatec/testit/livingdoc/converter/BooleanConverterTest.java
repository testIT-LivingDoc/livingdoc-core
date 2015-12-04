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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class BooleanConverterTest extends AbstractTypeConverterTest {
    public BooleanConverterTest() {
        super(new BooleanConverter());
    }

    @Test
    public void testConversionOfValue() {
        assertEquals(Boolean.TRUE, converter.parse("true", Boolean.class));
        assertEquals(Boolean.FALSE, converter.parse("false", Boolean.class));
        assertEquals(Boolean.TRUE, converter.parse(" true ", Boolean.class));
        assertEquals(Boolean.FALSE, converter.parse(" false   ", Boolean.class));

        assertEquals(Boolean.TRUE, converter.parse("TRUE", Boolean.class));
        assertEquals(Boolean.TRUE, converter.parse("True", Boolean.class));
        assertEquals(Boolean.FALSE, converter.parse("FALSE", Boolean.class));
        assertEquals(Boolean.FALSE, converter.parse("False", Boolean.class));

        assertEquals(true, converter.parse("yes", Boolean.class));
        assertEquals(false, converter.parse("no", Boolean.class));
        assertEquals(true, converter.parse("Yes", Boolean.class));
        assertEquals(false, converter.parse("No", Boolean.class));
    }

    @Test
    public void testSupportsBooleanTypes() {
        assertTrue(converter.canConvertTo(Boolean.class));
        assertTrue(converter.canConvertTo(boolean.class));
        assertFalse(converter.canConvertTo(Object.class));
    }
}
