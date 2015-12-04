package info.novatec.testit.livingdoc.interpreter.column;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class RecalledColumn extends Column {
    private final Message message;

    public RecalledColumn(Message message) {
        this.message = message;
    }

    @Override
    public Statistics doCell(Example cell) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        String symbol = ExampleUtil.contentOf(cell);
        Object variable = context.getVariable(symbol);
        cell.setContent(TypeConversion.toString(variable));

        Statistics stats = new Statistics();
        Call call = new Call(message);
        call.will(Annotate.exception(cell)).when(ResultIs.exception());
        call.will(Compile.statistics(stats)).when(ResultIs.exception());

        call.execute(TypeConversion.toString(variable));

        return stats;
    }
}
