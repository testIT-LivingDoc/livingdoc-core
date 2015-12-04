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

package info.novatec.testit.livingdoc.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public final class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> boolean isEmpty(T... objects) {
        return objects.length == 0;
    }

    public static <T> T[] toArray(T... objects) {
        return objects;
    }

    public static <T> Vector<T> toVector(T... objects) {
        return new Vector<T>(Arrays.asList(objects));
    }

    public static <T> List<T> toList(T... objects) {
        List<T> list = new ArrayList<T>(objects.length);
        for (T t : objects) {
            list.add(t);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List< ? > list, Class<T> type) {
        T[] array = ( T[] ) Array.newInstance(type, list.size());
        return list.toArray(array);
    }

    public static String[] toArray(List<String> list) {
        return toArray(list, String.class);
    }

    public static <T> List<T> even(Iterable<T> objects) {
        return split(objects, true);
    }

    public static <T> List<T> odd(Iterable<T> objects) {
        return split(objects, false);
    }

    public static String joinAsString(Object[] objects, String separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < objects.length; i ++ ) {
            Object element = objects[i];
            result.append(String.valueOf(element));
            if (i < objects.length - 1) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    public static <T> List<T> filter(Iterable<T> collection, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<T>();
        for (T element : collection) {
            if (predicate.isVerifiedBy(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public interface Predicate<T> {
        boolean isVerifiedBy(T element);
    }

    public static <T> List<T> split(Iterable<T> objects, boolean even) {

        boolean splitEven = even;

        List<T> parts = new ArrayList<T>();
        for (T element : objects) {
            if (splitEven) {
                parts.add(element);
            }
            splitEven = ! splitEven;
        }

        return parts;

    }

    public static <T> T shift(List<T> list) {
        return remove(list, 0);
    }

    public static <T> T removeLast(List<T> list) {
        return remove(list, list.size() - 1);
    }

    private static <T> T remove(List<T> list, int position) {
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(position);
    }

    public static <T> T first(List<T> list) {
        return get(list, 0);
    }

    public static <T> T last(List<T> list) {
        return get(list, list.size() - 1);
    }

    private static <T> T get(List<T> list, int index) {
        if (list.size() <= index) {
            return null;
        }
        return list.get(index);
    }

    public static Object toPrimitiveIntArray(List< ? > values) {
        int[] array = new int[values.size()];
        int cursor = 0;

        for (Object o : values) {
            array[cursor] = ( Integer ) o;
            cursor ++ ;
        }
        return array;
    }

    public static Object toPrimitiveFloatArray(List< ? > values) {
        float[] array = new float[values.size()];
        int cursor = 0;

        for (Object o : values) {
            array[cursor] = ( Float ) o;
            cursor ++ ;
        }
        return array;
    }

    public static Object toPrimitiveLongArray(List< ? > values) {
        long[] array = new long[values.size()];
        int cursor = 0;

        for (Object o : values) {
            array[cursor] = ( Long ) o;
            cursor ++ ;
        }
        return array;
    }

    public static Object toPrimitiveDoubleArray(List< ? > values) {
        double[] array = new double[values.size()];
        int cursor = 0;

        for (Object o : values) {
            array[cursor] = ( Double ) o;
            cursor ++ ;
        }
        return array;
    }

    public static Object toPrimitiveBoolArray(List< ? > values) {
        boolean[] array = new boolean[values.size()];
        int cursor = 0;

        for (Object o : values) {
            array[cursor] = ( Boolean ) o;
            cursor ++ ;
        }
        return array;
    }

}
