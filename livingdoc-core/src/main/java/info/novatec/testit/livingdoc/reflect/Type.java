package info.novatec.testit.livingdoc.reflect;

import java.lang.reflect.Constructor;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.util.ClassUtils;


public class Type<T> {
    private final Class< ? extends T> klass;

    public Type(Class< ? extends T> klass) {
        if (klass == null) {
            throw new IllegalArgumentException("the parameter is null");
        }
        this.klass = klass;
    }

    public Class< ? extends T> getUnderlyingClass() {
        return klass;
    }

    public T newInstance(Object... args) throws Throwable {
        Constructor< ? extends T> constructor = ClassUtils.findBestTypedConstructor(klass, args);
        return ClassUtils.invoke(constructor, args);
    }

    public T newInstanceUsingCoercion(String... args) throws Throwable {
        Constructor< ? extends T> constructor = ClassUtils.findPossibleConstructor(klass, ( Object[] ) args);
        Class< ? >[] types = constructor.getParameterTypes();
        return newInstance(TypeConversion.convert(args, types));
    }
}
