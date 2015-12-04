package info.novatec.testit.livingdoc.systemunderdevelopment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.reflect.Type;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


public class FixtureTypeLoaderChainTest {

    private FixtureTypeLoaderChain loader;

    @Before
    public void setup() {
        loader = new FixtureTypeLoaderChain(Thread.currentThread().getContextClassLoader());
        loader.searchPackage(getClass().getPackage().getName());
        // We are using inner classes, so we have also to add the class name as
        // package.
        loader.searchPackage(getClass().getName());
        loader.addSuffix("LivingDoc");
    }

    @Test
    public void shouldFindTypeByCamelCase() {
        Type<Object> type = loader.loadType("camel case");
        assertNotNull(type);
        assertTrue(CamelCase.class.isAssignableFrom(type.getUnderlyingClass()));
    }

    @Test
    public void shouldFindTypeBySuffix() {
        Type<Object> type = loader.loadType("Some");
        assertNotNull(type);
        assertTrue(SomeLivingDoc.class.isAssignableFrom(type.getUnderlyingClass()));
    }

    @Test
    public void shouldFindTypeByAlias() {
        Type<Object> type = loader.loadType("Fixture Alias Example");
        assertNotNull(type);
        assertTrue(AliasExample.class.isAssignableFrom(type.getUnderlyingClass()));
    }

    @Test
    public void shouldFindTypeByAliasAndSuffixAndCamelCase() {
        Type<Object> typeFromAlias = loader.loadType("My Test Example");
        Type<Object> typeFromSuffix = loader.loadType("TestExample");
        Type<Object> typeFromCamelCase = loader.loadType("test example living doc");
        Type<Object> typeFromSuffixAndCamelCase = loader.loadType("test example");

        assertNotNull(typeFromAlias);
        assertTrue(TestExampleLivingDoc.class.isAssignableFrom(typeFromAlias.getUnderlyingClass()));

        assertNotNull(typeFromSuffix);
        assertTrue(TestExampleLivingDoc.class.isAssignableFrom(typeFromSuffix.getUnderlyingClass()));

        assertNotNull(typeFromCamelCase);
        assertTrue(TestExampleLivingDoc.class.isAssignableFrom(typeFromCamelCase.getUnderlyingClass()));

        assertNotNull(typeFromSuffixAndCamelCase);
        assertTrue(TestExampleLivingDoc.class.isAssignableFrom(typeFromSuffixAndCamelCase.getUnderlyingClass()));
    }

    public static class CamelCase {
        // No implementation needed.
    }

    public static class SomeLivingDoc {
        // No implementation needed.
    }

    @FixtureClass("Fixture Alias Example")
    public static class AliasExample {
        // No implementation needed.
    }

    @FixtureClass("My Test Example")
    public static class TestExampleLivingDoc {
        // No implementation needed.
    }

}
