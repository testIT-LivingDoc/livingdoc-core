package info.novatec.testit.livingdoc.document;

import java.util.Map;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.Variables;
import info.novatec.testit.livingdoc.util.ExampleWrapper;


public abstract class AbstractSpecification implements Specification {
    protected final Variables variables = new Variables();
    protected Example cursor;

    protected void setStart(Example example) {
        this.cursor = before(example);
    }

    private Example before(Example example) {
        return ExampleWrapper.empty(example);
    }

    @Override
    public Example nextExample() {
        cursor = peek();
        return cursor;
    }

    @Override
    public Map<String, Object> getAllVariables() {
        return variables.getAllVariables();
    }

    protected abstract Example peek();

    @Override
    public boolean hasMoreExamples() {
        return peek() != null;
    }

    @Override
    public abstract void exampleDone(Statistics statistics);

    @Override
    public Object getVariable(String symbol) {
        return variables.getVariable(symbol);
    }

    @Override
    public void setVariable(String symbol, Object value) {
        variables.setVariable(symbol, value);
    }
}
