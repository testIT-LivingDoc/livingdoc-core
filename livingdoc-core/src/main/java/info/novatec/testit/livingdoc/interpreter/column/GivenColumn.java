/**
 * 
 */
package info.novatec.testit.livingdoc.interpreter.column;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.reflect.Message;


public class GivenColumn extends Column {
    private Message send;

    public GivenColumn(Message send) {
        this.send = send;
    }

    @Override
    public Statistics doCell(Example cell) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        Statistics stats = new Statistics();

        Call call = new Call(send);
        call.will(Annotate.exception(cell)).when(ResultIs.exception());
        call.will(Compile.statistics(stats)).when(ResultIs.exception());
        call.execute(cell.getContent());

        return stats;
    }
}
