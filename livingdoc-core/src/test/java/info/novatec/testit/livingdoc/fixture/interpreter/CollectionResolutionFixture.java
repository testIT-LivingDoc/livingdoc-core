package info.novatec.testit.livingdoc.fixture.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.interpreter.ListOfInterpreter;
import info.novatec.testit.livingdoc.interpreter.collection.CollectionInterpreter;
import info.novatec.testit.livingdoc.reflect.CollectionProvider;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.NameUtils;


@FixtureClass
public class CollectionResolutionFixture {

    private Class< ? > fixture;

    private static Class< ? > getClass(String typename) throws ClassNotFoundException {
        return ClassUtils.loadClass("info.novatec.testit.livingdoc.fixture.interpreter.CollectionResolutionFixture$"
            + NameUtils.toClassName(typename));
    }

    public void setFixture(String typename) throws ClassNotFoundException {
        this.fixture = getClass(typename);
    }

    public String queryValues() throws IllegalArgumentException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        WrappingListOfInterpreter interpreter = new WrappingListOfInterpreter(new PlainOldFixture(fixture.newInstance()));
        return interpreter.getQueryValues();
    }

    public static class FixtureWithACollectionProviderAnnotation extends ArrayList<Object> {
        private static final long serialVersionUID = 1L;

        @CollectionProvider()
        public String[] annotated() {
            return new String[] { "collection" };
        }

        public String[] query() {
            throw new UnsupportedOperationException();
        }
    }

    public static class FixtureThatImplementsCollection extends ArrayList<Object> {
        private static final long serialVersionUID = 1L;

        public FixtureThatImplementsCollection() {
            add("collection");
        }

        public String[] query() {
            throw new UnsupportedOperationException();
        }

    }

    public static class FixtureWithAQueryMethod {
        public String[] query() {
            return new String[] { "collection" };
        }
    }

    public static class FixtureWithoutCollection {
        // No implementation needed.
    }
    
    public static class WrappingListOfInterpreter extends ListOfInterpreter{

        public WrappingListOfInterpreter(Fixture fixture) {
            super(fixture);
        }

       public String getQueryValues() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException{
           return getFixtureList().get(0).getTarget().toString();
       }
        
        
        
    }
}
