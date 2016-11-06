package info.novatec.testit.livingdoc.interpreter;

import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class AnInterpreter extends DecisionTableInterpreter {
    public AnInterpreter(SystemUnderDevelopment sud) {
        this(new PlainOldFixture(new NullFixture()));
    }

    public AnInterpreter(Fixture fixture) {
        super(fixture);
    }

    public static class NullFixture {
        public String fixtureName() {
            return null;
        }

        public String fixtureParameters() {
            return null;
        }
    }
}
