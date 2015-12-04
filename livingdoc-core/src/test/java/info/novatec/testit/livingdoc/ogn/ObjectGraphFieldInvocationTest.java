/* Copyright (c) 2007 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.ogn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;


public class ObjectGraphFieldInvocationTest {
    private Field field;
    private Target target;

    @Before
    public void setUp() throws Exception {
        target = new Target();
        field = Target.class.getField("field");
    }

    @Test
    public void testInvoke_isGetterTrue_WithoutArgument() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, true);

        Object received = invocation.invoke(target);
        assertEquals(1, received);
    }

    @Test
    public void testInvoke_isGetterTrue_WithArgument() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, true);

        Object received = invocation.invoke(target, "999");
        assertEquals(1, received);
    }

    @Test
    public void testInvoke_isGetterFalse_WithArgument() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, false);

        Object received = invocation.invoke(target, "999");
        assertNull(received);

        assertEquals(999, target.field);
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterFalse_WithoutArgument() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, false);

        invocation.invoke(target);
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterTrue_WithWrongFieldType_WithoutArgument() throws Exception {

        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, true);

        invocation.invoke(new Object());
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterTrue_WithWrongFieldType_WithArgument() throws Exception {

        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, true);

        invocation.invoke(new Object(), "999");
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterFalse_WithWrongFieldType_WithoutArgument() throws Exception {

        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, false);

        invocation.invoke(new Object());
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterFalse_WithWrongFieldType_WithArgument() throws Exception {

        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, false);

        invocation.invoke(new Object(), "999");
    }

    @Test(expected = Exception.class)
    public void testInvoke_isGetterFalse_WithWrongParameterType() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, false);

        invocation.invoke(target, "abc");
    }

    @Test
    public void testInvoke_isGetterTrue_WithWrongParameterType() throws Exception {
        ObjectGraphFieldInvocation invocation = new ObjectGraphFieldInvocation(field, true);

        Object received = invocation.invoke(target, "abc");
        assertEquals(1, received);
    }

    private static class Target {
        public int field = 1;
    }
}
