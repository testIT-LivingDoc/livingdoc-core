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
package info.novatec.testit.livingdoc.expectation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * @version $Revision: $ $Date: $
 */
public class ShouldBeTest {

    @Test
    public void testCreateNullExpectation() {
        Expectation expectation = ShouldBe.literal("null");
        assertTrue(expectation instanceof NullExpectation);
        expectation = ShouldBe.literal("NULL");
        assertTrue(expectation instanceof NullExpectation);
    }

    @Test
    public void testCreateIsInstanceExpectation() {
        Expectation expectation = ShouldBe.literal("java.lang.NumberFormatException");

        assertTrue(expectation instanceof IsInstanceExpectation);
    }
}
