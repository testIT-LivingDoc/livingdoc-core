/**
 *
 */
package info.novatec.testit.livingdoc.interpreter.column;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.ExecutionContext;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.Variables;


public abstract class Column {
    protected ExecutionContext context = new Variables();

    public void bindTo(ExecutionContext executionContext) {
        this.context = executionContext;
    }

    public abstract Statistics doCell(Example cell) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException;

    @Override
    public String toString() {
        return context.getAllVariables().entrySet().toString();
    }
}
