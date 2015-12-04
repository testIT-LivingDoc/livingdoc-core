package info.novatec.testit.livingdoc.interpreter.collection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import info.novatec.testit.livingdoc.reflect.CollectionProvider;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;


public class CollectionInterpreterTest {
    CollectionInterpreter collectionInterpreter;
    Collection<Object> collection;

    private CollectionInterpreter interpreterFor(Object target) {
        return new CollectionInterpreter(new PlainOldFixture(target)) {
            // No implementation needed.
        };
    }

    @Test(expected = Exception.class)
    public void testThatATargetWithNoWayToGetACollectionThrowsAnException() throws Exception {
        collectionInterpreter = interpreterFor(this);
        collectionInterpreter.getFixtureList();
    }

    @Test
    public void testThatTheQueryMethodIsCalledForAClass() throws Exception {
        String firstElement = "TargetWithAQueryMethod-FirstElement";
        TargetWithAQueryMethod target = new TargetWithAQueryMethod(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));

    }

    @Test
    public void testThatATargetThatIsACollectionIsUsed() throws Exception {
        String firstElement = "TargetThatIsACollection-FirstElement";

        TargetThatIsACollection target = new TargetThatIsACollection(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));
    }

    @Test
    public void testThatACollectionProviderAnnotationIsThePreferedCall() throws Exception {
        String firstElement = "TargetWithACollectionProviderAnnotation-FirstElement";
        TargetWithACollectionProviderAnnotation target = new TargetWithACollectionProviderAnnotation(firstElement);

        collectionInterpreter = interpreterFor(target);

        assertEquals(firstElement, firstElement(collectionInterpreter.getFixtureList()));
    }

    private String firstElement(List<Fixture> list) {
        return ( String ) list.get(0).getTarget();
    }

    public class TargetWithAQueryMethod {
        Collection<Object> objects = new ArrayList<Object>();

        public TargetWithAQueryMethod(String firstElement) {
            objects.add(firstElement);
        }

        public Collection<Object> query() {
            return objects;
        }
    }

    public class TargetThatIsACollection extends ArrayList<Object> {
        private static final long serialVersionUID = 1L;

        TargetThatIsACollection(String firstElement) {
            super(Arrays.asList(new String[] { firstElement }));
        }

        public Collection< ? > query() {
            throw new UnsupportedOperationException();
        }
    }

    public class TargetWithACollectionProviderAnnotation {
        Collection<Object> objects = new ArrayList<Object>();

        public TargetWithACollectionProviderAnnotation(String firstElement) {
            objects.add(firstElement);
        }

        public Collection<Object> query() {
            throw new UnsupportedOperationException();
        }

        @CollectionProvider
        public Collection<Object> otherMethod() {
            return objects;
        }

    }

}
