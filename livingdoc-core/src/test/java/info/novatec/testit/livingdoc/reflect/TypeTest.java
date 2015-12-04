package info.novatec.testit.livingdoc.reflect;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.Specification;


public class TypeTest {
    private Type<Interpreter> type;

    @Before
    public void setUp() {
        type = new Type<Interpreter>(TypeTest.MyInterpreter.class);
    }

    @Test
    public void testInstanciatesObjectsFromTheirClassNames() throws Throwable {
        Interpreter interpreter = type.newInstanceUsingCoercion();
        assertThat(interpreter, is(instanceOf(TypeTest.MyInterpreter.class)));
    }

    @Test
    public void testWillLookForConstructorThatBestMatchesTypeParameters() throws Throwable {
        TypeTest.MyInterpreter interpreter = ( TypeTest.MyInterpreter ) type.newInstance(new TypeTest.MyFixture());
        assertThat(interpreter.fixture, is(instanceOf(TypeTest.MyFixture.class)));
    }

    @Test
    @Ignore
    public void testConstructorCanHavePrimitiveTypes() throws Throwable {
        TypeTest.MyInterpreter interpreter = ( TypeTest.MyInterpreter ) type.newInstance(5);
        assertEquals(5, interpreter.i);
    }

    @Test
    public void testParametersWillBeConvertedToConstructorTypesWhenUsingStringForm() throws Throwable {
        TypeTest.MyInterpreter interpreter = ( TypeTest.MyInterpreter ) type.newInstanceUsingCoercion("a string", "5");
        assertEquals("a string", interpreter.s);
        assertEquals(5, interpreter.i);
    }

    public static class MyFixture {
        // No implementation needed.
    }

    public static class MyInterpreter implements Interpreter {

        public Object fixture;
        public String s;
        public int i;

        public MyInterpreter() {
        }

        public MyInterpreter(int val) {
            i = val;
        }

        public MyInterpreter(String s, Integer i) {
            this.s = s;
            this.i = i;
        }

        public MyInterpreter(Object fixture) {
            this.fixture = fixture;
        }

        @Override
        public void interpret(Specification specification) {
            // No implementation needed.
        }

    }
}
