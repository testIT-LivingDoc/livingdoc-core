package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import info.novatec.testit.livingdoc.reflect.Fixture;

public class AndRow extends GivenRow {

    public AndRow(Fixture fixture) {
        super(fixture);
    }

    public static boolean matches(String keyword) {
        return "and".equalsIgnoreCase(keyword);
    }
}
