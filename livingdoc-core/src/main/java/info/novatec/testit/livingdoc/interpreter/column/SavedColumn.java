package info.novatec.testit.livingdoc.interpreter.column;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.Result;
import info.novatec.testit.livingdoc.call.Stub;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class SavedColumn extends Column {
    private final Message message;

    public SavedColumn(Message message) {
        this.message = message;
    }

    @Override
    public Statistics doCell(final Example cell) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        final String symbol = ExampleUtil.contentOf(cell);
        Statistics stats = new Statistics();
        Call call = new Call(message);
        call.will(Annotate.ignored(cell));
        call.will(Compile.statistics(stats));
        call.will(new SaveResultAs(symbol));
        call.execute();

        return stats;
    }

    private class SaveResultAs implements Stub {
        private final String symbol;

        public SaveResultAs(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public void call(Result result) {
            context.setVariable(symbol, result.getActual());
        }
    }
}
