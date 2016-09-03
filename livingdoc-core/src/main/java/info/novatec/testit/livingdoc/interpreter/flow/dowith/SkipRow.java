package info.novatec.testit.livingdoc.interpreter.flow.dowith;

import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.interpreter.flow.Row;

@Deprecated
public class SkipRow implements Row {

    @Override
    public void interpret(Specification row) {
        row.nextExample();
    }
}
