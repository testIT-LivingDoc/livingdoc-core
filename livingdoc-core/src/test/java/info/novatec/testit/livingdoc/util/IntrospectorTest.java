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

package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class IntrospectorTest {
    private Introspector introspector;

    @Before
    public void setUp() throws Exception {
        introspector = Introspector.respectingCase(Target.class);
    }

    @Test
    public void testShouldFindFieldsOnTarget() throws Exception {
        Field field = introspector.getField("field");
        assertNotNull(field);
        assertEquals("field", field.getName());
    }

    @Test
    public void testShouldFindSettersOnTarget() throws Exception {
        Method setter = introspector.getSetter("field");
        assertNotNull(setter);
        assertEquals("setField", setter.getName());
    }

    @Test
    public void testShouldFindGettersOnTarget() throws Exception {
        Method setter = introspector.getGetter("field");
        assertNotNull(setter);
        assertEquals("getField", setter.getName());
    }

    @Test
    public void testShouldMethodsOnTarget() throws Exception {
        List<Method> methods = introspector.getMethods("method");
        assertNotNull(methods);
        assertEquals(2, methods.size());
        assertEquals("method", methods.get(0).getName());
        assertEquals("method", methods.get(1).getName());
    }

    public static class Target {

        public String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String method() {
            return field;
        }

        public String method(int oneArity) {
            return field;
        }
    }
}
