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

/**
 * Represents a test expected result. A user can specify various expectations,
 * for example, a scalar value, an exception, a null value, etc. Subclasses of
 * <code>Expectation</code> should represent whatever the user can expect as a
 * test output.
 * 
 * @version $Revision: $ $Date: $
 */
public interface Expectation {
    /**
     * Utility method to create a textual representation of this
     * <code>Expectation</code>.
     * 
     * @param string The Expectation to describe
     * @return StringBuilder The textual representation
     */
    StringBuilder describeTo(StringBuilder string);

    /**
     * Returns a boolean value indicating if <code>result</code> meets this
     * expectation or not.
     * 
     * @param result The test execution result
     * @return true if result meets this Expectation, false otherwise
     */
    boolean meets(Object result);
}
