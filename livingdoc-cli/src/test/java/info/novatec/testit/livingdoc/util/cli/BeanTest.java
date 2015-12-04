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

package info.novatec.testit.livingdoc.util.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class BeanTest {
    private Target target;
    private Bean bean;
    private Map<String, Object> properties;
    private Object object;

    @Before
    public void setUp() throws Exception {
        properties = new HashMap<String, Object>();
        target = new Target();
        bean = new Bean(target);
        object = new Object();
    }

    @Test
    public void testShouldSetPropertiesUsingSetters() {
        properties.put("string", "text");
        properties.put("integer", 7);
        properties.put("anObject", object);

        bean.setProperties(properties);

        assertEquals("text", target.string);
        assertEquals(7, target.integer);
        assertEquals(object, target.anObject);
    }

    @Test
    public void testShouldIgnoreNonUknownProperties() {
        properties.put("another property", "some value we don't care");
        bean.setProperties(properties);
        assertTrue(true);
    }

    @Test
    public void testShouldCamelCasePropertyNames() {
        properties.put("an object", object);
        bean.setProperties(properties);
        assertEquals(object, target.anObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComplainsIfPropertyHasWrongType() {
        properties.put("string", new Object());
        bean.setProperties(properties);
    }

    public static class Target {

        public String string;
        public int integer;
        public Object anObject;

        public void setString(String string) {
            this.string = string;
        }

        public void setInteger(int integer) {
            this.integer = integer;
        }

        public void setAnObject(Object anObject) {
            this.anObject = anObject;
        }
    }
}
