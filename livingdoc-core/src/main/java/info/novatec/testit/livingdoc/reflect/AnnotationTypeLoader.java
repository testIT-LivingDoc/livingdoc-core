package info.novatec.testit.livingdoc.reflect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


/**
 * This type loader can load classes by an alias.
 * 
 * Pass a package using {@link #searchPackage(String)} and call
 * {@link #loadType(String)}.
 * 
 * All classes under the previous specified packages will be scanned for
 * {@link Alias} annotations. Each annotated class will then be checked for the
 * alias which can be passed as string via the {@link #loadType(String)} method.
 * 
 * @param <T> allows us to specify the object return type which can be loaded
 * using {@link #loadType(String)}
 */
public class AnnotationTypeLoader<T> implements TypeLoader<T> {

    private final TypeLoader<T> parent;
    private final ClassLoader classLoader;
    private final ConfigurationBuilder builder;

    private Set<Class< ? >> annotatedAliasClasses = new HashSet<Class< ? >>();
    private boolean isAnnotationScanningNecessary = false;

    public AnnotationTypeLoader(TypeLoader<T> parent, ClassLoader classLoader) {
        this.parent = parent;
        this.classLoader = classLoader;
        builder = new ConfigurationBuilder();
        builder.addClassLoader(classLoader);
    }

    @Override
    public void searchPackage(String prefix) {
        parent.searchPackage(prefix);
        builder.addUrls(ClasspathHelper.forPackage(prefix));
        isAnnotationScanningNecessary = true;
    }

    @Override
    public void addSuffix(String suffix) {
        parent.addSuffix(suffix);
    }

    @Override
    public Type<T> loadType(String name) {
        Type<T> type = parent.loadType(name);
        if (type == null) {
            if (isAnnotationScanningNecessary) {
                scanForAliasAnnotatedTypes(builder);
                isAnnotationScanningNecessary = false;
            }
            type = parent.loadType(getClassNameForAlias(name));
        }
        return type;
    }

    private void scanForAliasAnnotatedTypes(ConfigurationBuilder confBuilder) {
        Reflections reflections = new Reflections(confBuilder);
        annotatedAliasClasses.addAll(reflections.getTypesAnnotatedWith(FixtureClass.class));
    }

    /**
     * @param alias The alias
     * @return the name of the class represented by this alias or null if no
     * represented class can be found.
     */
    private String getClassNameForAlias(String alias) {
        List<Class< ? >> matchingAliases = new ArrayList<Class< ? >>();
        for (Class< ? > annotatedClass : annotatedAliasClasses) {
            FixtureClass aliasAnnotation = annotatedClass.getAnnotation(FixtureClass.class);
            if (hasAlias(aliasAnnotation, alias)) {
                matchingAliases.add(annotatedClass);
            }
        }
        if (matchingAliases.size() == 1) {
            return matchingAliases.get(0).getName();
        }
        if (matchingAliases.size() > 1) {
            throw new DuplicateAnnotatedFixturesFoundException(matchingAliases);
        }
        return null;
    }

    private boolean hasAlias(FixtureClass aliasAnnotation, String alias) {
        if (alias != null && ! alias.isEmpty()) {
            for (String aliasInAnnotation : aliasAnnotation.value()) {
                if (aliasInAnnotation != null && alias.equals(aliasInAnnotation)) {
                    return true;
                }
            }
        }
        return false;
    }
}
