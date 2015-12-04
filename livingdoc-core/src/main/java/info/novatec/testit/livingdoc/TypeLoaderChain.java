package info.novatec.testit.livingdoc;

import info.novatec.testit.livingdoc.reflect.AnnotationTypeLoader;
import info.novatec.testit.livingdoc.reflect.CamelCaseTypeLoader;
import info.novatec.testit.livingdoc.reflect.JavaTypeLoader;
import info.novatec.testit.livingdoc.reflect.PackageTypeLoader;
import info.novatec.testit.livingdoc.reflect.SuffixTypeLoader;
import info.novatec.testit.livingdoc.reflect.Type;
import info.novatec.testit.livingdoc.reflect.TypeLoader;


public class TypeLoaderChain<T> implements TypeLoader<T> {
    private final TypeLoader<T> typeLoader;

    public TypeLoaderChain(Class< ? extends T> type) {
        this(type, TypeLoaderChain.class.getClassLoader());
    }

    public TypeLoaderChain(Class< ? extends T> type, ClassLoader classLoader) {
        TypeLoader<T> typeLoaderAdapter = new JavaTypeLoader<T>(type, classLoader);
        PackageTypeLoader<T> packageClassLoader = new PackageTypeLoader<T>(typeLoaderAdapter);
        AnnotationTypeLoader<T> aliasClassLoader = new AnnotationTypeLoader<T>(packageClassLoader, classLoader);
        SuffixTypeLoader<T> suffixClassLoader = new SuffixTypeLoader<T>(aliasClassLoader);
        typeLoader = new CamelCaseTypeLoader<T>(suffixClassLoader);
    }

    @Override
    public void searchPackage(String prefix) {
        typeLoader.searchPackage(prefix);
    }

    @Override
    public void addSuffix(String suffix) {
        typeLoader.addSuffix(suffix);
    }

    @Override
    public Type<T> loadType(String name) {
        return typeLoader.loadType(name);
    }
}
