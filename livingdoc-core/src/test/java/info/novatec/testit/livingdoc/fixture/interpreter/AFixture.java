package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class AFixture {
    private List<String> parameters;

    public AFixture() {
    }

    public AFixture(String parameter) {
        this();
        parameters = new ArrayList<String>();
        parameters.add(parameter);
    }

    public AFixture(String parameter1, String parameter2, String parameter3) {
        this(parameter1);
        parameters.add(parameter2);
        parameters.add(parameter3);
    }

    public String fixtureName() {
        return this.getClass().getSimpleName();
    }

    public String[] fixtureParameters() {
        return parameters == null ? null : parameters.toArray(new String[0]);
    }
}
