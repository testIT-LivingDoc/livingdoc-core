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

package info.novatec.testit.livingdoc.reflect;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


public class AliasTypeLoaderTest {
    private AnnotationTypeLoader< ? > loader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        JavaTypeLoader<Object> javaTypeLoader = new JavaTypeLoader<Object>(Object.class, classLoader);
        PackageTypeLoader<Object> packageTypeLoader = new PackageTypeLoader<Object>(javaTypeLoader);
        loader = new AnnotationTypeLoader<Object>(packageTypeLoader, classLoader);
        String packageName = getClass().getPackage().getName();
        loader.searchPackage(packageName);
    }

    @Test
    public void testBuildsClassFromOneAlias() throws Exception {
        Type< ? > type = loader.loadType("Example Alias");
        Class< ? > underlyingClass = type.getUnderlyingClass();
        Class< ? > expectedClass = OneAliasExample.class;

        assertNotNull(underlyingClass);
        assertTrue(expectedClass.isAssignableFrom(underlyingClass));
    }

    @Test
    public void testBuildsClassFromMultipleAliases() throws Exception {
        Type< ? > type = loader.loadType("Awesome Alias");
        Type< ? > anotherType = loader.loadType("Another Alias");
        Class< ? > expectedClass = MultipleAliasExample.class;

        assertNotNull(type.getUnderlyingClass());
        assertNotNull(anotherType.getUnderlyingClass());
        assertTrue(expectedClass.isAssignableFrom(type.getUnderlyingClass()));
        assertTrue(expectedClass.isAssignableFrom(anotherType.getUnderlyingClass()));
    }

    @Test
    public void testShouldNotBuildClassFromUnkownAliases() throws Exception {
        Type< ? > type = loader.loadType("An alias which does not exist.");
        Type< ? > anotherType = loader.loadType("Another Alias who does not exist");

        assertNull(type);
        assertNull(anotherType);
    }

    @Test
    public void testBuildsClassFromSpecialCharacterAndUmlautAliases() throws Exception {
        Type< ? > type = loader.loadType("E%amp(e $ Al$a§");
        Type< ? > anotherType = loader.loadType("Anöther Äliüs");
        Class< ? > expectedClass = UmlautAndSpecialCharAliasExample.class;

        assertNotNull(type.getUnderlyingClass());
        assertNotNull(anotherType.getUnderlyingClass());
        assertTrue(expectedClass.isAssignableFrom(type.getUnderlyingClass()));
        assertTrue(expectedClass.isAssignableFrom(anotherType.getUnderlyingClass()));
    }

    @Test
    public void testThrowsExceptionWithDuplicateAliases() {
        thrown.expect(DuplicateAnnotatedFixturesFoundException.class);
        thrown.expectMessage("Following aliases");
        @SuppressWarnings("unused")
        Type< ? > type = loader.loadType("Double Alias");
    }

    @FixtureClass("Example Alias")
    public static class OneAliasExample {
        // No implementation needed.
    }

    @FixtureClass({ "Awesome Alias", "Another Alias" })
    public static class MultipleAliasExample {
        // No implementation needed.
    }

    @FixtureClass({ "E%amp(e $ Al$a§", "Anöther Äliüs" })
    public static class UmlautAndSpecialCharAliasExample {
        // No implementation needed.
    }

    @FixtureClass("Double Alias")
    public static class DoubleAliasExample {
        // No implementation needed.
    }

    @FixtureClass("Double Alias")
    public static class DoubleAliasExample2 {
        // No implementation needed.
    }

}
