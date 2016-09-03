package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.interpreter.flow.Row;


public class SkipRow implements Row {

    @Override
    public void interpret(Specification row) {
        row.nextExample();
    }
}
