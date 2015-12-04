/**
 * 
 */
package info.novatec.testit.livingdoc.interpreter.column;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.Message;


public class ExpectedColumn extends Column {
    private Message check;

    public ExpectedColumn(Message check) {
        this.check = check;
    }

    @Override
    public Statistics doCell(Example cell) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        Statistics stats = new Statistics();
        Call call = new Call(check);
        if ( ! StringUtils.isBlank(cell.getContent())) {
            call.expect(ShouldBe.literal(cell.getContent()));
        }

        call.will(Annotate.withDetails(cell));
        call.will(Compile.statistics(stats));
        call.execute();
        return stats;
    }
}
