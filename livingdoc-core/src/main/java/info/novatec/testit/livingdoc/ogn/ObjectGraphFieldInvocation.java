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

import java.lang.reflect.Field;

import info.novatec.testit.livingdoc.TypeConversion;


public class ObjectGraphFieldInvocation implements ObjectGraphInvocable {
    private final Field field;

    private final boolean getter;

    public ObjectGraphFieldInvocation(Field field, boolean isGetter) {
        this.field = field;
        this.getter = isGetter;
    }

    @Override
    public Object invoke(Object target, String... args) throws IllegalAccessException, IllegalArgumentException {
        if (getter) {
            return field.get(target);
        }

        field.set(target, TypeConversion.parse(args[0], field.getType()));
        return null;
    }
}
