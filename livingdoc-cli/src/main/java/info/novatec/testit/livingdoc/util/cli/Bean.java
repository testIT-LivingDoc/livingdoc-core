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

package info.novatec.testit.livingdoc.util.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;

import info.novatec.testit.livingdoc.util.Introspector;
import info.novatec.testit.livingdoc.util.NameUtils;


public class Bean {
    private final Object target;

    public Bean(Object target) {
        this.target = target;
    }

    public void setProperties(Map<String, Object> properties) {
        Introspector introspector = new Introspector(target.getClass(), new PropertyComparator());
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            Method setter = introspector.getSetter(property.getKey());
            if (setter != null) {
                invoke(setter, property.getValue());
            }
        }
    }

    private void invoke(Method method, Object value) {
        try {
            method.invoke(target, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("%s to method %s", value, method.getName()), e);
        }
    }

    public static class PropertyComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return NameUtils.decapitalize(s1).compareTo(NameUtils.decapitalize(s2));
        }

    }
}
