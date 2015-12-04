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

import java.util.Arrays;

import org.junit.Test;


public class PrimitiveArrayConverterTest {

    @Test
    public void testShouldConvertPrimitiveBoolArrays() {
        PrimitiveBoolArrayConverter converter = new PrimitiveBoolArrayConverter();
        assertTrue(converter.canConvertTo(boolean[].class));
        assertTrue(Arrays.equals(new boolean[] { true, false, true }, ( boolean[] ) converter.parse("true, false, true",
            boolean[].class)));
        assertFalse(converter.canConvertTo(Boolean[].class));

        assertEquals("true, false", converter.toString(new boolean[] { true, false }));
    }

    @Test
    public void testShouldConvertPrimitiveIntArrays() {
        PrimitiveIntArrayConverter converter = new PrimitiveIntArrayConverter();
        assertTrue(converter.canConvertTo(int[].class));
        assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, ( int[] ) converter.parse("1, 2, 3", int[].class)));
        assertFalse(converter.canConvertTo(Integer[].class));

        assertEquals("3, 4", converter.toString(new int[] { 3, 4 }));
    }

    @Test
    public void testShouldConvertPrimitiveLongArrays() {
        PrimitiveLongArrayConverter converter = new PrimitiveLongArrayConverter();
        assertTrue(converter.canConvertTo(long[].class));
        assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, ( long[] ) converter.parse("1, 2, 3", long[].class)));
        assertFalse(converter.canConvertTo(Long[].class));

        assertEquals("3, 4", converter.toString(new long[] { 3, 4 }));
    }

    @Test
    public void testShouldConvertPrimitiveFloatArrays() {
        PrimitiveFloatArrayConverter converter = new PrimitiveFloatArrayConverter();
        assertTrue(converter.canConvertTo(float[].class));
        assertTrue(Arrays.equals(new float[] { 1, 2, 3 }, ( float[] ) converter.parse("1, 2, 3", float[].class)));
        assertFalse(converter.canConvertTo(Float[].class));

        assertEquals("3.0, 4.5", converter.toString(new float[] { 3.0f, 4.5f }));
    }

    @Test
    public void testShouldConvertPrimitiveDoubleArrays() {
        PrimitiveDoubleArrayConverter converter = new PrimitiveDoubleArrayConverter();
        assertTrue(converter.canConvertTo(double[].class));
        assertTrue(Arrays.equals(new double[] { 1.51, 2.23, 3 }, ( double[] ) converter.parse("1.51, 2.23, 3",
            double[].class)));
        assertFalse(converter.canConvertTo(Double[].class));

        assertEquals("3.0, 4.2", converter.toString(new double[] { 3.0d, 4.2d }));
    }
}
