package info.novatec.testit.livingdoc.interpreter.flow;

import static info.novatec.testit.livingdoc.LivingDoc.canContinue;
import static info.novatec.testit.livingdoc.LivingDoc.shouldStop;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.interpreter.AbstractInterpreter;
import info.novatec.testit.livingdoc.reflect.AfterTable;
import info.novatec.testit.livingdoc.reflect.BeforeTable;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.StaticInvocation;
import info.novatec.testit.livingdoc.util.ExampleUtil;


/**
 * Interprets a Command table specifications.
 * <p/>
 * Process a table containing a series of command. Each line of table correspond
 * to a command and its parameters.
 */
public class AbstractFlowInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFlowInterpreter.class);

    private int startRow;
    private RowSelector rowSelector;
    protected final Fixture fixture;
    private Message beforeTableMessage;
    private Message afterTableMessage;
    protected Statistics stats;

    public AbstractFlowInterpreter(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public void interpret(Specification specification) {
        parseMessages();
        stats = new Statistics();

        skipFirstRowOfNextTable();
        callBeforeTable();
        while (specification.hasMoreExamples() && canContinue(stats)) {
            Example next = specification.nextExample();
            if (indicatesEndOfFlow(next)) {
                callAfterTable();
                specification.exampleDone(new Statistics());
                return;
            }

            Table table = new Table(firstRowOf(next));
            execute(table);
            specification.exampleDone(table.getStatistics());
            stats.tally(table.getStatistics());

            includeFirstRowOfNextTable();
        }
    }

    protected void setRowSelector(RowSelector rowSelector) {
        this.rowSelector = rowSelector;
    }

    protected Example firstRowOf(Example next) {
        return next.at(0, startRow);
    }

    private void execute(Table table) {
        while (table.hasMoreExamples() && canContinue(table.getStatistics())) {
            Example example = table.peek();

            Row row = rowSelector.select(example);
           
            row.interpret(table);
            
            if (shouldStop(table.getStatistics())) {
                example.firstChild().lastSibling().addSibling().annotate(Annotations.stopped());
            }
        }
    }

    private void includeFirstRowOfNextTable() {
        startRow = 0;
    }

    private void skipFirstRowOfNextTable() {
        startRow = 1;
    }

    private boolean indicatesEndOfFlow(Example table) {
        return "end".equalsIgnoreCase(ExampleUtil.contentOf(table.at(0, 0, 0)));
    }

    private void parseMessages() {
        if (fixture != null) {
            for (Method method : fixture.getTarget().getClass().getMethods()) {
                if (method.isAnnotationPresent(BeforeTable.class)) {
                    beforeTableMessage = new StaticInvocation(fixture.getTarget(), method);
                }

                if (method.isAnnotationPresent(AfterTable.class)) {
                    afterTableMessage = new StaticInvocation(fixture.getTarget(), method);
                }
            }
        }
    }

    private void callBeforeTable() {
        if (beforeTableMessage != null) {
            LOG.debug("Calling @BeforeMethod "+ beforeTableMessage);
            callMessage(beforeTableMessage);
        }else{
            LOG.debug("No @BeforeMethod defined");
        }
    }

    private void callAfterTable() {
        if (afterTableMessage != null) {
            LOG.debug("Calling @AftereMethod "+ afterTableMessage);
            callMessage(afterTableMessage);
        }else{
            LOG.debug("No @AfterMethod defined");
        }
    }

    private void callMessage(Message message) {
        try {
            Call call = new Call(message);
            call.will(Compile.statistics(stats)).when(ResultIs.exception());
            call.execute();
        } catch (InvocationTargetException e) {
            LOG.error(LOG_ERROR, e);
            stats.exception();
        } catch (IllegalAccessException e) {
            LOG.error(LOG_ERROR, e);
            stats.exception();
        }
    }
}
