package info.novatec.testit.livingdoc.interpreter;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class CommentInterpreter extends SkipInterpreter {
    public CommentInterpreter(SystemUnderDevelopment sud) {
        this();
    }

    public CommentInterpreter() {
        super(new Statistics(0, 0, 0, 1));
    }
}
