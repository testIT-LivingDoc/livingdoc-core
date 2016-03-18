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

import static info.novatec.testit.livingdoc.util.CollectionUtil.shift;
import static info.novatec.testit.livingdoc.util.CollectionUtil.toList;

import java.io.File;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import org.apache.commons.lang3.StringUtils;


public final class ClassUtils {
    public static Class< ? > loadClass(String className) throws ClassNotFoundException {
        // Instead of the Thread.currentThread().getContextClassLoader() we are
        // using the current class classLoader, to be compatible with a OSGI
        // environment (mainly for the confluence plugin). For more information
        // see: https://wiki.eclipse.org/Context_Class_Loader_Enhancements or
        // http://njbartlett.name/2010/08/30/osgi-readiness-loading-classes.html
        return loadClass(ClassUtils.class.getClassLoader(), className);
    }

    public static Class< ? > loadClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return classLoader.loadClass(className);
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findBestTypedConstructor(Class<T> klass, Object... args) throws NoSuchMethodException {
        for (Constructor< ? > constructor : klass.getConstructors()) {
            if (typesMatch(constructor, args)) {
                return ( Constructor<T> ) constructor;
            }
        }

        throw noSuitableConstructorException(klass, args);
    }

    private static <T> NoSuchMethodException noSuitableConstructorException(Class<T> klass, Object... args) {
        return new NoSuchMethodException(klass.getName() + ".<init>(" + toString(args) + ")");
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findPossibleConstructor(Class<T> klass, Object... args) throws NoSuchMethodException {
        for (Constructor< ? > constructor : klass.getConstructors()) {
            if (arityMatches(constructor, args)) {
                return ( Constructor<T> ) constructor;
            }
        }

        throw noSuitableConstructorException(klass, args);
    }

    private static boolean arityMatches(Constructor< ? > constructor, Object... args) {
        return constructor.getParameterTypes().length == args.length;
    }

    private static boolean typesMatch(Constructor< ? > constructor, Object[] args) {
        if ( ! arityMatches(constructor, args)) {
            return false;
        }

        Class< ? >[] parameterTypes = constructor.getParameterTypes();
        parameterTypes = changePrimitivesTypesToNonOnes(parameterTypes);

        for (int i = 0; i < args.length; i ++ ) {
            if ( ! parameterTypes[i].isInstance(args[i])) {
                return false;
            }
        }

        return true;
    }

    private static Class< ? >[] changePrimitivesTypesToNonOnes(Class< ? >[] parameterTypes) {
        Class< ? >[] types = new Class[parameterTypes.length];
        int cursor = 0;

        for (Class< ? > type : parameterTypes) {
            types[cursor] = getEquivalentNonPrimitiveType(type);
            cursor ++ ;
        }

        return types;
    }

    private static Class< ? > getEquivalentNonPrimitiveType(Class< ? > type) {
        if (type == null) {
            return null;
        }

        if (type.isPrimitive()) {
            Class< ? > resultType = null;
            if (type == int.class) {
                resultType = Integer.class;
            } else if (type == float.class) {
                resultType = Float.class;
            } else if (type == boolean.class) {
                resultType = Boolean.class;
            } else if (type == double.class) {
                resultType = Double.class;
            } else if (type == long.class) {
                resultType = Long.class;
            } else if (type == char.class) {
                resultType = Character.class;
            } else if (type == byte.class) {
                resultType = Byte.class;
            } else if (type == short.class) {
                resultType = Short.class;
            }
            return resultType;
        }
        return type;
    }

    public static <T> T invoke(Constructor<T> constructor, Object... args) throws Throwable {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException e) {
            throw e.getCause();
        }
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    private static String toString(Object... args) {
        if (args.length == 0) {
            return "";
        }
        return "[" + CollectionUtil.joinAsString(args, ", ") + "]";
    }

    public static ClassLoader toClassLoaderWithDefaultParent(Collection<String> classPaths) throws MalformedURLException {
        Set<URL> dependencies = new HashSet<URL>();
        for (String classPath : classPaths) {
            File dependency = new File(classPath);
            dependencies.add(dependency.toURI().toURL());
        }
        ClassLoader classLoader = URLClassLoader.newInstance(dependencies.toArray(new URL[dependencies.size()]));
        return classLoader;
    }

    public static ClassLoader toClassLoader(Collection<String> classPaths) throws MalformedURLException {
        return toClassLoader(classPaths, ClassUtils.class.getClassLoader());
    }

    public static ClassLoader toClassLoader(Collection<String> classPaths, ClassLoader parent) throws MalformedURLException {
        Set<URL> dependencies = new HashSet<URL>();
        for (String classPath : classPaths) {
            File dependency = new File(classPath);
            dependencies.add(dependency.toURI().toURL());
        }
        ClassLoader classLoader = URLClassLoader.newInstance(dependencies.toArray(new URL[dependencies.size()]), parent);
        return classLoader;
    }

    /**
     * @throws UndeclaredThrowableException if any exception occurs.
     */
    public static <C> C createInstanceFromClassNameWithArguments(ClassLoader classLoader, String classWithArguments,
        Class<C> expectedType) throws UndeclaredThrowableException {
        try {
            List<String> parameters = toList(escapeValues(classWithArguments.split(";")));
            Class< ? > klass = ClassUtils.loadClass(classLoader, shift(parameters));

            if ( ! expectedType.isAssignableFrom(klass)) {
                throw new IllegalArgumentException("Class " + expectedType.getName() + " is not assignable from " + klass
                    .getName());
            }

            if (parameters.size() == 0) {
                return expectedType.cast(klass.newInstance());
            }

            String[] args = parameters.toArray(new String[parameters.size()]);
            Constructor< ? > constructor = klass.getConstructor(args.getClass());
            return expectedType.cast(constructor.newInstance(new Object[] { args }));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public static String[] escapeValues(String[] values) {
        for (int index = 0; index < values.length; index ++ ) {
            values[index] = StringUtils.replaceEach(values[index], new String[] { "%3B" }, new String[] { ";" });
        }

        return values;
    }

    private ClassUtils() {
    }
}
