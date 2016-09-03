package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import info.novatec.testit.livingdoc.reflect.Fixture;


public class CheckThatRow extends CheckRow {
    
    public static boolean matches(String keyword) {
        return "check".equalsIgnoreCase(keyword) || "check that".equalsIgnoreCase(keyword);
    }

    public CheckThatRow(Fixture fixture) {
        super(fixture);
    }

}
