package info.novatec.testit.livingdoc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.runner.SpecificationRunnerBuilder;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerExecutor;


/**
 * A class loader that combines multiple class loaders into one.<br>
 * The classes loaded by this class loader are associated with this class
 * loader, i.e. Class.getClassLoader() points to this class loader.
 * <p>
 * 
 * <p>
 * In the case of LivingDoc, you may need this mechanism if you want to include
 * external class paths to use your own components (since LivingDoc itself is
 * build extremely extensible). This component is also mainly used by the
 * {@link SpecificationRunnerBuilder#classLoader(ClassLoader)
 * SpecificationRunnerBuilder} and the
 * {@link SpecificationRunnerExecutor#classLoader(ClassLoader)
 * SpecificationRunnerExecutor} to inject classes from external jars into the
 * executing class loader.
 * </p>
 * 
 * <p>
 * Original code author: Christian d'Heureuse, Inventec Informatik AG, Zurich,
 * Switzerland, www.source-code.biz<br>
 * http://www.source-code.biz/snippets/java/12.htm<br>
 * Modified version: Frantisek Mantlik <frantisek at mantlik.cz><br>
 * https://code.google.com/p/swingbox-javahelp-viewer/source/browse/src/main/
 * java/org/mantlik/swingboxjh/JoinClassLoader.java
 * </p>
 * 
 * Note: This classloader should be improved using different sources like
 * https://github.com/nurkiewicz/spring-js/blob/master/src/main/java/org/
 * springframework/instrument/classloading/ShadowingClassLoader.java or
 * http://yiyujia.blogspot.de/2011/10/java-class-loader-and-static-variable.html
 */
public class JoinClassLoader extends ClassLoader {

    private final ClassLoader enclosingClassLoader;

    private final Map<String, Class< ? >> classCache = new HashMap<String, Class< ? >>();

    public JoinClassLoader(ClassLoader parent, ClassLoader enclosingClassLoader) {
        super(parent);
        this.enclosingClassLoader = enclosingClassLoader;
    }

    public ClassLoader getEnclosingClassLoader() {
        return this.enclosingClassLoader;
    }

    @Override
    protected Class< ? > findClass(String name) throws ClassNotFoundException {
        Class< ? > cls = this.classCache.get(name);

        if (cls == null) {
            return doLoadClass(name);
        }
        return cls;
    }

    @Override
    protected URL findResource(String internalName) {
        return this.enclosingClassLoader.getResource(internalName);
    }

    @Override
    protected Enumeration<URL> findResources(String internalName) throws IOException {
        return this.enclosingClassLoader.getResources(internalName);
    }

    private Class< ? > doLoadClass(String name) throws ClassNotFoundException {
        String path = StringUtils.replaceChars(name, '.', '/') + ".class";
        URL url = findResource(path);
        if (url == null) {
            throw new ClassNotFoundException(name);
        }

        try {
            InputStream stream = url.openStream();
            byte[] bytes = IOUtils.toByteArray(stream);
            Class< ? > cls = defineClass(name, bytes, 0, bytes.length);
            // Additional check for defining the package, if not defined yet.
            if (cls.getPackage() == null) {
                int packageSeparator = name.lastIndexOf('.');
                if (packageSeparator != - 1) {
                    String packageName = name.substring(0, packageSeparator);
                    definePackage(packageName, null, null, null, null, null, null, null);
                }
            }
            this.classCache.put(name, cls);
            return cls;
        } catch (IOException ex) {
            throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
        }
    }

}
