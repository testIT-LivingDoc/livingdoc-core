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

import info.novatec.testit.livingdoc.util.AliasLoader;
import info.novatec.testit.livingdoc.util.FactoryMethod;


/**
 * @version $Revision: $ $Date: $
 */
public class ErrorExpectation implements Expectation {
    private static final String ERROR = "error";

    @FactoryMethod
    public static ErrorExpectation create(String expected) {
        return AliasLoader.get().isAliasForKeyword(expected, ERROR) ? new ErrorExpectation() : null;
    }

    @Override
    public StringBuilder describeTo(StringBuilder sb) {
        return sb.append(ERROR);
    }

    @Override
    public boolean meets(Object result) {
        return Throwable.class.isInstance(result);
    }
}
