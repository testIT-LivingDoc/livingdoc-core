package info.novatec.testit.livingdoc.testing;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class TestingInterpreter implements Interpreter {
    @SuppressWarnings("unused")
    public TestingInterpreter(Fixture fixture) {
        // No implementation needed.
    }

    // So that maven does not complain. It seems to consider this
    // class as a test, maybe because of the Testing fixture
    public TestingInterpreter() {
    }

    @Override
    public void interpret(Specification specification) {
        // No implementation needed.
    }
}
