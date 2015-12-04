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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.reflect.Message;


public class ObjectGraphNavigationTest {
    private ObjectGraphNavigation objectGraphNavigation;
    private List<ObjectGraphNavigationInfo> received;
    private int counter = 0;

    @Before
    public void setUp() throws Exception {
        received = new LinkedList<ObjectGraphNavigationInfo>();

        objectGraphNavigation = new ObjectGraphNavigation(false, new ObjectGraphNavigationMessageResolver() {
            @Override
            public Message resolve(ObjectGraphNavigationInfo info) {
                received.add(info);
                return null;
            }
        });
    }

    @Test
    public void testWithNoNavigation() {
        objectGraphNavigation.resolveMessage("Name");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(new ObjectGraphNavigationInfo("Name"));

        assertGraphStrict(expected, received);
    }

    @Test
    public void testThatWeStartWithTheFirstLevelGraph() {
        objectGraphNavigation.resolveMessage("Employee Name First");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(new ObjectGraphNavigationInfo("EmployeeNameFirst"));

        assertGraphHead(expected, received);
    }

    @Test
    public void testThatWeSplitMethodWithOneDepth() {
        objectGraphNavigation.resolveMessage("Employee Name");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(new ObjectGraphNavigationInfo("EmployeeName"),
            new ObjectGraphNavigationInfo("Employee", "Name"));

        assertGraphStrict(expected, received);
    }

    @Test
    public void testThatWeSplitMethodFromRightToLeftWithOneDepth() {
        objectGraphNavigation.resolveMessage("a b c");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(new ObjectGraphNavigationInfo("abc"),
            new ObjectGraphNavigationInfo("ab", "c"), new ObjectGraphNavigationInfo("a", "bc"),
            new ObjectGraphNavigationInfo("a.b", "c"));

        assertGraphHead(expected, received);

        received.clear();
        objectGraphNavigation.resolveMessage("Employee Name First");

        expected = Arrays.asList(new ObjectGraphNavigationInfo("EmployeeNameFirst"), new ObjectGraphNavigationInfo(
            "EmployeeName", "First"), new ObjectGraphNavigationInfo("Employee", "NameFirst"), new ObjectGraphNavigationInfo(
                "Employee.Name", "First"));

        assertGraphHead(expected, received);
    }

    @Test
    public void testThatWeCanSplitTwoDepth() {
        objectGraphNavigation.resolveMessage("a b c d");

        List<ObjectGraphNavigationInfo> expected = Arrays.asList(new ObjectGraphNavigationInfo("abcd"),
            new ObjectGraphNavigationInfo("abc", "d"), new ObjectGraphNavigationInfo("ab", "cd"),
            new ObjectGraphNavigationInfo("ab.c", "d"), new ObjectGraphNavigationInfo("a", "bcd"),
            new ObjectGraphNavigationInfo("a.bc", "d"), new ObjectGraphNavigationInfo("a.b", "cd"),
            new ObjectGraphNavigationInfo("a.b.c", "d"));

        assertGraphHead(expected, received);
    }

    @Test
    public void testWithManyWordsToDetectOutOfMemory() {
        objectGraphNavigation = new ObjectGraphNavigation(false, new ObjectGraphNavigationMessageResolver() {
            @Override
            public Message resolve(ObjectGraphNavigationInfo info) {
                counter ++ ;
                return null;
            }
        });

        objectGraphNavigation.resolveMessage("a b c d e f g h i j k l m n o p");
        assertEquals(32768, counter);
    }

    private void assertGraphStrict(Collection<ObjectGraphNavigationInfo> expected,
        Collection<ObjectGraphNavigationInfo> paramReceived) {
        assertEquals("Size mismatch", expected.size(), paramReceived.size());
        assertGraphHead(expected, paramReceived);
    }

    private void assertGraphHead(Collection<ObjectGraphNavigationInfo> expected,
        Collection<ObjectGraphNavigationInfo> paramReceived) {
        assertTrue("Size mismatch", paramReceived.size() >= expected.size());

        Iterator<ObjectGraphNavigationInfo> itrExpected = expected.iterator();
        Iterator<ObjectGraphNavigationInfo> itrReceived = paramReceived.iterator();

        while (itrExpected.hasNext()) {
            ObjectGraphNavigationInfo exp = itrExpected.next();
            ObjectGraphNavigationInfo rec = itrReceived.next();
            assertEquals(exp.getTarget(), rec.getTarget());
            assertEquals(exp.getMethodName(), rec.getMethodName());
        }
    }
}
