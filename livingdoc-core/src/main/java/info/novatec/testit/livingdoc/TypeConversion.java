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
package info.novatec.testit.livingdoc;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.converter.ArrayConverter;
import info.novatec.testit.livingdoc.converter.BigDecimalConverter;
import info.novatec.testit.livingdoc.converter.BooleanConverter;
import info.novatec.testit.livingdoc.converter.DateConverter;
import info.novatec.testit.livingdoc.converter.DoubleConverter;
import info.novatec.testit.livingdoc.converter.EnumConverter;
import info.novatec.testit.livingdoc.converter.FloatConverter;
import info.novatec.testit.livingdoc.converter.IntegerConverter;
import info.novatec.testit.livingdoc.converter.LongConverter;
import info.novatec.testit.livingdoc.converter.PrimitiveBoolArrayConverter;
import info.novatec.testit.livingdoc.converter.PrimitiveDoubleArrayConverter;
import info.novatec.testit.livingdoc.converter.PrimitiveFloatArrayConverter;
import info.novatec.testit.livingdoc.converter.PrimitiveIntArrayConverter;
import info.novatec.testit.livingdoc.converter.PrimitiveLongArrayConverter;
import info.novatec.testit.livingdoc.converter.StringConverter;
import info.novatec.testit.livingdoc.converter.TypeConverter;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.ExpectationTypeConverter;
import info.novatec.testit.livingdoc.util.ClassUtils;


public final class TypeConversion {
    private static final Logger LOG = LoggerFactory.getLogger(TypeConversion.class);

    private static final List<TypeConverter> converters = new LinkedList<TypeConverter>();
    private static final Deque<TypeConverter> customConverters = new LinkedList<TypeConverter>();
    // private static final Map<Class<? extends TypeConverter>

    static {
        converters.add(new ExpectationTypeConverter());
        converters.add(new EnumConverter());
        converters.add(new IntegerConverter());
        converters.add(new BigDecimalConverter());
        converters.add(new LongConverter());
        converters.add(new FloatConverter());
        converters.add(new DoubleConverter());
        converters.add(new DateConverter());
        converters.add(new BooleanConverter());
        converters.add(new ArrayConverter());
        converters.add(new PrimitiveIntArrayConverter());
        converters.add(new PrimitiveLongArrayConverter());
        converters.add(new PrimitiveDoubleArrayConverter());
        converters.add(new PrimitiveFloatArrayConverter());
        converters.add(new PrimitiveBoolArrayConverter());
        converters.add(new StringConverter());
    }

    private TypeConversion() {
    }

    public static void register(TypeConverter converter) {
        if ( ! converters.contains(converter)) {
            customConverters.push(converter);
        }
    }

    public static Deque<TypeConverter> getCustomConverters() {
        return customConverters;
    }

    public static void unregisterAllCustomConverters() {
        customConverters.clear();
    }

    public static void unregisterLastAddedCustomConverter() {
        if ( ! customConverters.isEmpty()) {
            customConverters.pop();
        }
    }

    public static boolean supports(Class< ? extends Object> type) {
        return converterRegisteredFor(type) || canSelfConvert("parse", type) || canSelfConvert("valueOf", type);
    }

    private static boolean converterRegisteredFor(Class< ? extends Object> type) {
        return converterForType(type) != null;
    }

    private static boolean canSelfConvert(String parsingMethod, Class< ? extends Object> type) {
        try {
            Method method = type.getMethod(parsingMethod, String.class);
            return type.isAssignableFrom(method.getReturnType()) && ClassUtils.isPublic(method) && ClassUtils.isStatic(
                method);
        } catch (Exception e) {
            LOG.trace(LOG_ERROR, e);
            return false;
        }
    }

    /**
     * Converts <code>value</code> to the object type of <code>type</code> by
     * using the appropriate <code>TypeConverter</code>.
     * 
     * @param value The string value to convert
     * @param type The type to convert to
     * @return The converted value
     */
    public static Object parse(String value, Class< ? extends Object> type) {
        if (canSelfConvert("parse", type)) {
            return selfConvert("parse", value, type);
        }
        if (converterRegisteredFor(type)) {
            return converterForType(type).parse(value, type);
        }
        if (canSelfConvert("valueOf", type)) {
            return selfConvert("valueOf", value, type);
        }

        throw new UnsupportedOperationException("No converter registered for: " + type.getName());
    }

    /**
     * SelfConversion implies that if a class has the given static method named
     * that receive a String and that returns a instance of the class, then it
     * can serve for conversion purpose.
     * 
     * @param parsingMethod The parsing method to use
     * @param value The string value to convert
     * @param type The type to convert to
     * @return The converted value
     */
    private static Object selfConvert(String parsingMethod, String value, Class< ? extends Object> type) {
        try {
            Method method = type.getMethod(parsingMethod, String.class);
            return method.invoke(null, value);
        } catch (InvocationTargetException e) {
            LOG.error(LOG_ERROR, e);
            throw new IllegalArgumentException("Can't convert " + value + " to " + type.getName(), e.getCause());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            throw new IllegalArgumentException("Can't convert " + value + " to " + type.getName(), e);
        }
    }

    /**
     * Converts <code>value</code> to a String by using the appropriate
     * <code>TypeConverter</code>.
     * 
     * @param value The object value to convert
     * @return The string value
     */
    public static String toString(Object value) {
        if (value == null) {
            return "";
        }

        Class< ? extends Object> type = value.getClass();

        if (canSelfRevert(type)) {
            return selfRevert(value);
        }
        if (converterRegisteredFor(type)) {
            return converterForType(type).toString(value);
        }

        return String.valueOf(value);
    }

    private static boolean canSelfRevert(Class< ? extends Object> type) {
        try {
            Method method = type.getMethod("toString", type);
            return String.class.isAssignableFrom(method.getReturnType()) && ClassUtils.isPublic(method) && ClassUtils
                .isStatic(method);
        } catch (Exception e) {
            LOG.trace(LOG_ERROR, e);
            return false;
        }
    }

    private static String selfRevert(Object value) {
        Class< ? extends Object> type = value.getClass();
        try {
            Method method = type.getMethod("toString", type);
            return ( String ) method.invoke(null, value);
        } catch (InvocationTargetException e) {
            LOG.error(LOG_ERROR, e);
            throw new IllegalArgumentException("Can't get a string for " + value + " of to " + type.getName(), e.getCause());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            throw new IllegalArgumentException("Can't get a string for " + value + " of to " + type.getName(), e);
        }
    }

    public static TypeConverter converterForType(Class< ? extends Object> type) {
        TypeConverter converter = getConverterForType(customConverters, type);
        if (converter == null) {
            converter = getConverterForType(converters, type);
        }
        return converter;
    }

    private static TypeConverter getConverterForType(Collection<TypeConverter> typeConverters,
        Class< ? extends Object> type) {
        for (TypeConverter converter : typeConverters) {
            if (converter.canConvertTo(type)) {
                return converter;
            }
        }

        return null;
    }

    public static Object[] convert(String[] values, Class< ? extends Object>[] toTypes) {
        Object[] converted = new Object[values.length];
        for (int i = 0; i < values.length; i ++ ) {
            converted[i] = parse(values[i], toTypes[i]);
        }
        return converted;
    }
}
