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

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.FactoryMethod;


/**
 * @version $Revision: $ $Date: $
 */
public class IsInstanceExpectation implements Expectation {
    private static final Logger LOG = LoggerFactory.getLogger(IsInstanceExpectation.class);

    private final Class< ? > type;

    @FactoryMethod
    public static IsInstanceExpectation create(String expected) {
        Class< ? > klass = loadClass(expected);
        return klass != null ? new IsInstanceExpectation(klass) : null;
    }

    private static Class< ? > loadClass(String expectation) {
        try {
            return ClassUtils.loadClass(expectation);
        } catch (ClassNotFoundException ex) {
            LOG.trace(LOG_ERROR, ex);
        }
        return null;
    }

    public IsInstanceExpectation(Class< ? > type) {
        this.type = type;
    }

    @Override
    public StringBuilder describeTo(StringBuilder sb) {
        return sb.append("instance of ").append(type.getName());
    }

    @Override
    public boolean meets(Object result) {
        return type.isInstance(result);
    }
}
