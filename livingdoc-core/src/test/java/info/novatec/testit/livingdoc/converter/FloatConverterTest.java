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


/**
 * @author ehardy
 * @version $Revision: $ $Date: $
 */
public class FloatConverterTest extends AbstractTypeConverterTest {
    public FloatConverterTest() {
        super(new FloatConverter());
    }

    @Test
    public void testConversionOfValue() {
        assertEquals(5.755f, converter.parse("5.755", Float.class));
        assertEquals(new Float(5.755), converter.parse(" 5.755 ", Float.class));
        assertEquals( - 5.755f, converter.parse("-5.755", Float.class));
    }

    @Test
    public void testConversionOfValueWithACommaAsDecimalSeparator() {
        assertEquals(5.755f, converter.parse("5,755", Float.class));
        assertEquals(new Float(5.755), converter.parse(" 5,755 ", Float.class));
        assertEquals( - 5.755f, converter.parse("-5,755", Float.class));
    }

    @Test
    public void testSupportsFloatTypes() {
        assertTrue(converter.canConvertTo(Float.class));
        assertTrue(converter.canConvertTo(float.class));
        assertFalse(converter.canConvertTo(Object.class));
    }
}
