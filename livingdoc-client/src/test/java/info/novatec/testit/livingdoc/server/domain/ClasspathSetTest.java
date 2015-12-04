/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;


public class ClasspathSetTest {
    @Test
    public void tempEmptyClasspathsTextShouldReturnEmptySet() {
        assertEquals(0, ClasspathSet.parse(null).size());
        assertEquals(0, ClasspathSet.parse("").size());
        assertEquals(0, ClasspathSet.parse("\n").size());
        assertEquals(0, ClasspathSet.parse("\r\n").size());
    }

    @Test
    public void testOneClasspathTextShouldReturnSetContainingOne() {
        Set<String> expected = expected("foo");

        assertEquals(expected, ClasspathSet.parse("foo"));
        assertEquals(expected, ClasspathSet.parse("foo\n"));
        assertEquals(expected, ClasspathSet.parse("foo\r\n"));
    }

    @Test
    public void multipleClasspathTextShouldReturnSetContainingSameCountOfClasspaths() {
        Set<String> expected = expected("foo", "bar", "baz");

        assertEquals(expected, ClasspathSet.parse("\nfoo\nbar\nbaz\n"));
        assertEquals(expected, ClasspathSet.parse("\r\nfoo\r\nbar\r\nbaz\r\n"));
        assertEquals(expected, ClasspathSet.parse("\rfoo\rbar\rbaz\r"));
    }

    @Test
    public void classpathEntriesMustBeTrimmed() {
        Set<String> expected = expected("foo", "bar", "baz");

        assertEquals(expected, ClasspathSet.parse("\n  foo \nbar      \n     baz\n"));
        assertEquals(expected, ClasspathSet.parse("\r\n  foo \r\nbar      \r\n     baz\r\n"));
        assertEquals(expected, ClasspathSet.parse("\r  foo \rbar      \r     baz\r"));
    }

    private static Set<String> expected(String... classpaths) {
        ClasspathSet expected = new ClasspathSet();

        expected.addAll(Arrays.asList(classpaths));

        return expected;
    }
}
