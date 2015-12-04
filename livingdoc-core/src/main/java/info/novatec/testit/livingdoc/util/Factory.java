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

package info.novatec.testit.livingdoc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Factory<T> {
    private final Class< ? extends T> type;
    private final Method factoryMethod;

    public Factory(Class< ? extends T> type) {
        this.type = type;
        this.factoryMethod = factoryMethod();
        if (factoryMethod == null) {
            throw new IllegalArgumentException(type.getName());
        }
    }

    public T newInstance(Object... args) {
        try {
            return type.cast(factoryMethod.invoke(null, args));
        } catch (IllegalAccessException e) {
            throw ExceptionImposter.imposterize(e);
        } catch (InvocationTargetException e) {
            throw ExceptionImposter.imposterize(e.getCause());
        }
    }

    private Method factoryMethod() {
        for (Method method : type.getMethods()) {
            if (ClassUtils.isPublic(method) && ClassUtils.isStatic(method) && type.isAssignableFrom(method.getReturnType())
                && ( method.getName().equals("newInstance") || method.getAnnotation(FactoryMethod.class) != null )) {
                return method;
            }
        }

        return null;
    }
}
