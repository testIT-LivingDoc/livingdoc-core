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

package info.novatec.testit.livingdoc.runner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;


public class FactoryConverterTest {
    private FactoryConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new FactoryConverter();
    }

    @Test
    public void testCreatesObjectFromClassName() throws Exception {
        Object convertedObject = converter.convert(String.class.getName());
        assertTrue(convertedObject instanceof String);
    }

    @Test
    public void testClassConstructorCanHaveParameters() throws Exception {
        Object object = converter.convert(ClassToCreate.class.getName() + ";a;b;c");
        assertArrayEquals(new String[] { "a", "b", "c" }, ( ( ClassToCreate ) object ).params);
    }

    @Test
    public void testSeparatorWithSpecialCharacter() throws Exception {
        Object object = converter.convert(ClassToCreate.class.getName() + ";a;b;my%3Bpassword");
        assertArrayEquals(new String[] { "a", "b", "my;password" }, ( ( ClassToCreate ) object ).params);
    }

    @Test
    public void testThatWhenAClassTypeIsSpecifiedThenExceptionIfNotGoodType() throws Exception {
        FactoryConverter converterWithClass = new FactoryConverter(DefaultSystemUnderDevelopment.class);

        assertNotNull(converter.convert(DefaultSystemUnderDevelopment.class.getName()));

        try {
            converterWithClass.convert(ClassToCreate.class.getName() + ";a;b;c");
            fail();
        } catch (Exception ex) {
            assertTrue(true); // ok, this should trhow
        }

        assertNotNull(converterWithClass.convert(DefaultSystemUnderDevelopment.class.getName()));
    }

    public static class ClassToCreate {
        private final String[] params;

        public ClassToCreate(String... params) {
            this.params = params;
        }
    }
}
