package info.novatec.testit.livingdoc.reflect;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import info.novatec.testit.livingdoc.util.JoinClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.NameUtils;


/**
 * This Class handles loading of fixture classes via the {@link FixtureClass}
 * annotation.
 * 
 * Get the singleton instance with the static variable {@link #INSTANCE}. The
 * classpath is determined by calling {@link #addLoader(ClassLoader)} where you
 * pass the current {@link ClassLoader}.
 * 
 * Calling the {@link #getAnnotatedFixture(String)} method searches all found
 * annotated fixture classes for a match to the passed {@link String} either by
 * name or by annotation. The match will then be passed to
 * {@link #getClassType(String)} which is responsible for loading the fixture
 * class. In case of more than one match the
 * {@link DuplicateAnnotatedFixturesFoundException} is thrown.
 * 
 * @param <T> allows us to specify the object return type which can be loaded
 * using {@link #getClassType(String)}
 */
public class AnnotationLoader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotationLoader.class);

    private ConfigurationBuilder builder;
    private Set<Class< ? >> annotatedFixtureClasses = new HashSet<Class< ? >>();

    @SuppressWarnings("rawtypes")
    public static AnnotationLoader INSTANCE = new AnnotationLoader();

    public Type<T> getAnnotatedFixture(String name) {

        String fixturename = "(?i)" + NameUtils.toClassName(name) + "(Fixture)?";

        List<Class< ? >> matchingAnnotatedClasses = findMatchingAnnotatedFixtures(fixturename);

        List<Class< ? >> matchingAliases = null;
        if (matchingAnnotatedClasses.size() != 1) {
            matchingAliases = findFixtureByAlias(name);
        }

        if (matchingAliases != null && matchingAliases.size() == 1) {
            return getClassType(matchingAliases.get(0).getName());
        }
        if (matchingAnnotatedClasses.size() == 1) {
            return getClassType(matchingAnnotatedClasses.get(0).getName());
        }
        if (matchingAnnotatedClasses.size() > 1 || matchingAliases.size() > 1) {
            throw new DuplicateAnnotatedFixturesFoundException(matchingAnnotatedClasses, matchingAliases);
        }
        return null;
    }

    private List<Class< ? >> findFixtureByAlias(String alias) {
        List<Class< ? >> matchingAliases = new ArrayList<Class< ? >>();
        for (Class< ? > annotatedClass : annotatedFixtureClasses) {
            FixtureClass aliasAnnotation = annotatedClass.getAnnotation(FixtureClass.class);
            if (hasAlias(aliasAnnotation, alias)) {
                matchingAliases.add(annotatedClass);
            }
        }
        return matchingAliases;
    }

    private List<Class< ? >> findMatchingAnnotatedFixtures(String fixturename) {
        List<Class< ? >> matchingAnnotatedClasses = new ArrayList<Class< ? >>();
        String discernInnerClass;
        for (Class< ? > in : annotatedFixtureClasses) {
            discernInnerClass = in.toString().substring(StringUtils.indexOf(in.toString(), "$") + 1);
            discernInnerClass = discernInnerClass.substring(StringUtils.lastIndexOf(discernInnerClass, ".") + 1);
            if (Pattern.matches(fixturename, discernInnerClass)) {
                matchingAnnotatedClasses.add(in);
            }
        }
        return matchingAnnotatedClasses;
    }

    public void addLoader(ClassLoader loader) {
        if (builder == null) {
            this.builder = new ConfigurationBuilder();
            builder.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());
        }
        builder.addClassLoader(loader);
        builder.addUrls(ClasspathHelper.forClassLoader(loader));

        if(loader instanceof JoinClassLoader) {
            // When the object "reflections" is created in the method scanClassPath(), are scanned the URLs so
            // it is necessary to add the URLs from the enclosing class loader in the JoinClassLoader that it
            // contains the fixture classpath (see org.reflections.Reflections.scan()).
            builder.addUrls(ClasspathHelper.forClassLoader(((JoinClassLoader) loader).getEnclosingClassLoader()));
        }

        scanClassPath(builder);
    }

    private void scanClassPath(ConfigurationBuilder configBuilder) {
        Reflections reflections = new Reflections(configBuilder);
        annotatedFixtureClasses = reflections.getTypesAnnotatedWith(FixtureClass.class);
    }

    @SuppressWarnings("unchecked")
    private Type<T> getClassType(String name) {
        Class< ? > clazz = loadClass(name, builder);
        return clazz != null ? new Type<T>(( Class< ? extends T> ) clazz) : null;
    }

    private Class< ? > loadClass(String name, ConfigurationBuilder configBuilder) {
        for (ClassLoader classLoader : configBuilder.getClassLoaders()) {
            Class< ? > loadedClass = null;
            try {
                loadedClass = classLoader.loadClass(name);
            } catch (ClassNotFoundException notFound) {
                LOG.trace(LOG_ERROR, notFound);
            } catch (NoClassDefFoundError notFound) {
                LOG.trace(LOG_ERROR, notFound);
            }
            if (loadedClass != null) {
                return loadedClass;
            }
        }
        return null;
    }

    private boolean hasAlias(FixtureClass aliasAnnotation, String alias) {
        if (alias != null) {
            for (String aliasvalue : aliasAnnotation.value()) {
                if (alias.equalsIgnoreCase(aliasvalue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
