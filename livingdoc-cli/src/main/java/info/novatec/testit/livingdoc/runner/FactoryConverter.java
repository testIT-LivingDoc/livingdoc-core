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

package info.novatec.testit.livingdoc.runner;

import static info.novatec.testit.livingdoc.util.CollectionUtil.shift;
import static info.novatec.testit.livingdoc.util.CollectionUtil.toList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.cli.Converter;


/**
 * A converter which creates a new instance of an object. This converter expects
 * a semi-colon comma separated list of values. The first value is the class
 * name of the object to instantiate. Following values represent a list of
 * parameters. Any semi-colon special character
 * 
 * <pre>
 * %3B
 * </pre>
 * 
 * found inside an actual parameter will be converted to
 * 
 * <pre>
 * ;
 * </pre>
 * 
 * (way to keep semi-colon as parameter)
 * <p/>
 * The target class must abide to the following rules:
 * <ul>
 * <li>When no parameter is expected, it must provide a default constructor</li>
 * <li>Otherwise, if parameters are expected, it must provide a constructor
 * accepting an array of string.
 * </ul>
 * <p/>
 * Note: it could be interesting to see if we can make
 * {@link info.novatec.testit.livingdoc.util.ClassUtils#findBestTypedConstructor(Class,java.lang.Object...)}
 * smarter and use it.
 */
public class FactoryConverter implements Converter<Object> {
    private Class< ? > expectedType;

    public FactoryConverter() {
        this(null);
    }

    public FactoryConverter(Class< ? > expectedType) {
        this.expectedType = expectedType;
    }

    @Override
    public Object convert(String value) throws IllegalArgumentException, SecurityException, ClassNotFoundException,
        InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        List<String> parameters = toList(ClassUtils.escapeValues(value.split(";")));
        Class< ? > klass = ClassUtils.loadClass(shift(parameters));

        if (expectedType != null && ! expectedType.isAssignableFrom(klass)) {
            throw new IllegalArgumentException("Class " + expectedType.getName() + " is not assignable from" + klass
                .getName());
        }

        if (parameters.size() == 0) {
            return klass.newInstance();
        }

        String[] args = parameters.toArray(new String[parameters.size()]);
        Constructor< ? > constructor = klass.getConstructor(args.getClass());
        return constructor.newInstance(new Object[] { args });

    }
}
