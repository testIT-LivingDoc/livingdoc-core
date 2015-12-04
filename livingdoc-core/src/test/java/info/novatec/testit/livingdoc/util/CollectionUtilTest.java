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

package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


public class CollectionUtilTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testCanConvertArraysToVectors() {
        Vector< ? extends Object> vec = CollectionUtil.toVector("1", null, new Date());
        assertEquals(3, vec.size());
        assertTrue(vec.elementAt(0) instanceof String);
        assertTrue(vec.elementAt(1) == null);
        assertTrue(vec.elementAt(2) instanceof Date);
    }

    @Test
    public void testJoinsElementsOfArrayToFormAStringRepresentation() {
        String[] values = { "This", "is", "a", "string" };
        assertEquals("This is a string", CollectionUtil.joinAsString(values, " "));
    }

    @Test
    public void testCanFilterCollections() {
        List<String> values = Arrays.asList("", "start", "and", "with a blank", " ");
        Collection<String> nonBlanks = CollectionUtil.filter(values, new CollectionUtil.Predicate<String>() {
            @Override
            public boolean isVerifiedBy(String element) {
                return ! StringUtils.isBlank(element);
            }
        });
        assertEquals(Arrays.asList("start", "and", "with a blank"), nonBlanks);
    }

}
