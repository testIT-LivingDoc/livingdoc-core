package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class ClassUtilsTest {

    @Test
    public void shouldInstantiateClassFromArguments() throws Exception {
        String className = MySytemUnderDevelopment.class.getName();
        String classNameWithArguments = className + ";arg1;arg2;arg3";

        SystemUnderDevelopment sud = ClassUtils.createInstanceFromClassNameWithArguments(Thread.currentThread()
            .getContextClassLoader(), classNameWithArguments, SystemUnderDevelopment.class);

        assertTrue(sud instanceof MySytemUnderDevelopment);

        MySytemUnderDevelopment mySud = ( MySytemUnderDevelopment ) sud;

        assertEquals(mySud.getParam(0), "arg1");
        assertEquals(mySud.getParam(1), "arg2");
        assertEquals(mySud.getParam(2), "arg3");
    }

    public static class MySytemUnderDevelopment implements SystemUnderDevelopment {

        private String[] params;

        public MySytemUnderDevelopment(String... params) {
            this.params = params;
        }

        public String getParam(int index) {
            return params[index];
        }

        @Override
        public Fixture getFixture(String name, String... parameters) throws Throwable {
            return null;
        }

        @Override
        public void addImport(String packageName) {
            // No implementation needed.
        }

        @Override
        public void onStartDocument(Document document) {
            // No implementation needed.
        }

        @Override
        public void onEndDocument(Document document) {
            // No implementation needed.
        }

        @Override
        public void setClassLoader(ClassLoader classLoader) {
            // No implementation needed.
        }

    }

}
