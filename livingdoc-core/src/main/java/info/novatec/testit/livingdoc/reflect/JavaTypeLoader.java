package info.novatec.testit.livingdoc.reflect;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaTypeLoader<T> implements TypeLoader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(JavaTypeLoader.class);

    private final Class< ? extends T> type;
    private final java.lang.ClassLoader classLoader;

    public JavaTypeLoader(Class< ? extends T> type) {
        this(type, JavaTypeLoader.class.getClassLoader());
    }

    public JavaTypeLoader(Class< ? extends T> type, java.lang.ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    @Override
    public void searchPackage(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSuffix(String suffix) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<T> loadType(String name) {
        Class< ? > clazz = loadClass(name);
        Class< ? extends T> klass = ( Class< ? extends T> ) loadClass(name);
        return classLoaded(clazz) && typeMatches(clazz) ? new Type<T>(klass) : null;
    }

    private boolean classLoaded(Class< ? > clazz) {
        return clazz != null;
    }

    private boolean typeMatches(Class< ? > clazz) {
        return type.isAssignableFrom(clazz);
    }

    public Class< ? > loadClass(String name) {
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException notFound) {
            LOG.trace(LOG_ERROR, notFound);
            return null;
        } catch (NoClassDefFoundError notFound) {
            LOG.trace(LOG_ERROR, notFound);
            return null;
        }
    }
}
