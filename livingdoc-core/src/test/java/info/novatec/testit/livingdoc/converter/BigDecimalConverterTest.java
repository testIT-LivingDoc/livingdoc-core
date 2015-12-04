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

import java.math.BigDecimal;

import org.junit.Test;


public class BigDecimalConverterTest extends AbstractTypeConverterTest {
    public BigDecimalConverterTest() {
        super(new BigDecimalConverter());
    }

    @Test
    public void testConversionOfValue() {
        assertEquals(new BigDecimal(5), converter.parse("5", BigDecimal.class));
        assertEquals(new BigDecimal(5), converter.parse("  5 ", BigDecimal.class));
    }

    @Test
    public void testSupportsIntegerTypes() {
        assertTrue(converter.canConvertTo(BigDecimal.class));
        assertFalse(converter.canConvertTo(Object.class));
    }
}
