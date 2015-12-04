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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import info.novatec.testit.livingdoc.TypeConversion;


public class ObjectGraphMethodInvocation implements ObjectGraphInvocable {
    private final Method method;

    private final boolean getter;

    public ObjectGraphMethodInvocation(Method method, boolean isGetter) {
        this.method = method;
        this.getter = isGetter;
    }

    @Override
    public Object invoke(Object target, String... args) throws IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        if (getter || method.getParameterTypes().length == 0) {
            return method.invoke(target);
        }
        Object[] values = TypeConversion.convert(args, method.getParameterTypes());

        return method.invoke(target, values);
    }
}
