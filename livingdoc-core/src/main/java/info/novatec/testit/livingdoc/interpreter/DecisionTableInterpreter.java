package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.LivingDoc.canContinue;
import static info.novatec.testit.livingdoc.LivingDoc.shouldStop;
import static info.novatec.testit.livingdoc.annotation.Annotations.exception;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_TWO;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.ExecutionContext;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;
import info.novatec.testit.livingdoc.reflect.AfterRow;
import info.novatec.testit.livingdoc.reflect.AfterTable;
import info.novatec.testit.livingdoc.reflect.BeforeFirstExpectation;
import info.novatec.testit.livingdoc.reflect.BeforeRow;
import info.novatec.testit.livingdoc.reflect.BeforeTable;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.StaticInvocation;


public class DecisionTableInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(DecisionTableInterpreter.class);

    protected final Fixture fixture;
    protected Statistics stats;
    private List<Column> columns;
    private Message beforeRowMessage = null;
    private Message beforeFirstExpectationMessage = null;
    private Message afterRowMessage = null;
    private Message beforeTableMessage = null;
    private Message afterTableMessage = null;

    public DecisionTableInterpreter(Fixture fixture) {
        super();
        this.fixture = fixture;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        stats = new Statistics();
        Example table = specification.nextExample();
        columns = parseColumns(table);
        bindColumnsTo(specification);
        parseMessages();

        callBeforeTable();
        for (Example row = table.at(0, 2); row != null && canContinue(stats); row = row.nextSibling()) {
            doRow(row);

            if (shouldStop(stats)) {
                row.firstChild().lastSibling().addSibling().annotate(Annotations.stopped());
            }
        }
        callAfterTable();
        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }

    private void bindColumnsTo(ExecutionContext context) {
        LOG.trace(ENTRY_WITH, context.toString());
        for (Column column : columns) {
            column.bindTo(context);
        }
        LOG.trace(EXIT);
    }

    private List<Column> parseColumns(Example table) {
        LOG.trace(ENTRY_WITH, table.toString());
        Example headers = table.at(0, 1, 0);
        List<Column> columns;
        if (headers == null) {
            columns = new ArrayList<Column>(0);
        } else {
            columns = new ArrayList<Column>(headers.remainings());
            for (int i = 0; i < headers.remainings(); i ++ ) {
                columns.add(parseColumn(headers.at(i)));
            }

            if (shouldStop(stats)) {
                headers.lastSibling().addSibling().annotate(Annotations.stopped());
            }
        }
        LOG.trace(EXIT_WITH, columns);
        return columns;
    }

    private Column parseColumn(Example header) {
        LOG.trace(ENTRY_WITH, header.toString());
        try {
            Column parsedColumn = HeaderForm.parse(header.getContent()).selectColumn(fixture);
            LOG.trace(EXIT_WITH, parsedColumn);
            return parsedColumn;
        } catch (NoSuchMessageException e) {
            header.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
            Column nullColumn = new NullColumn();
            LOG.trace(EXIT_WITH, nullColumn);
            return nullColumn;
        }
    }

    private void doRow(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        if ( ! row.hasChild()) {
            LOG.trace(EXIT + " - " + row.toString() + " has no child.");
            return;
        }

        boolean beforeFirstExpectationAlreadyCalled = false;
        callBeforeRow();

        Example cells = row.firstChild();
        for (int i = 0; i < cells.remainings(); i ++ ) {
            final Example cell = cells.at(i);

            if (i < columns.size()) {
                if ( ! beforeFirstExpectationAlreadyCalled) {
                    beforeFirstExpectationAlreadyCalled = checkFirstExpectation(columns.get(i));
                }
                doCell(columns.get(i), cell);
            }
        }
        callAfterRow();
        LOG.trace(EXIT);
    }

    private boolean checkFirstExpectation(Column column) {
        LOG.trace(ENTRY_WITH, column.toString());
        if (isExpectation(column)) {
            callBeforeFirstExpectation();
            LOG.trace(EXIT_WITH, true);
            return true;
        }

        LOG.trace(EXIT_WITH, false);
        return false;
    }

    private boolean isExpectation(Column column) {
        LOG.trace(ENTRY_WITH, column.toString());
        boolean isExpectedColumn = column instanceof ExpectedColumn;
        LOG.trace(EXIT_WITH, isExpectedColumn);
        return isExpectedColumn;
    }

    private void callBeforeRow() {
        LOG.trace(ENTRY);
        if (beforeRowMessage != null) {
            callMessage(beforeRowMessage);
        }
        LOG.trace(EXIT);
    }

    private void callBeforeFirstExpectation() {
        LOG.trace(ENTRY);
        if (beforeFirstExpectationMessage != null) {
            callMessage(beforeFirstExpectationMessage);
        }
        LOG.trace(EXIT);
    }

    private void callAfterRow() {
        LOG.trace(ENTRY);
        if (afterRowMessage != null) {
            callMessage(afterRowMessage);
        }
        LOG.trace(EXIT);
    }

    private void callBeforeTable() {
        if (beforeTableMessage != null) {
            callMessage(beforeTableMessage);
        }
    }

    private void callAfterTable() {
        if (afterTableMessage != null) {
            callMessage(afterTableMessage);
        }
    }

    private void callMessage(Message message) {
        LOG.trace(ENTRY_WITH, message.toString());
        try {
            Call call = new Call(message);
            call.will(Compile.statistics(stats)).when(ResultIs.exception());
            call.execute();
        } catch (InvocationTargetException e) {
            stats.exception();
            LOG.error(LOG_ERROR, e);
        } catch (IllegalAccessException e) {
            stats.exception();
            LOG.error(LOG_ERROR, e);
        }
        LOG.trace(EXIT);
    }

    private void parseMessages() {
        LOG.trace(ENTRY);
        if (fixture != null) {
            for (Method method : fixture.getTarget().getClass().getMethods()) {
                if (method.isAnnotationPresent(BeforeTable.class)) {
                    beforeTableMessage = new StaticInvocation(fixture.getTarget(), method);
                }

                if (method.isAnnotationPresent(AfterTable.class)) {
                    afterTableMessage = new StaticInvocation(fixture.getTarget(), method);
                }

                if (method.isAnnotationPresent(BeforeRow.class)) {
                    beforeRowMessage = new StaticInvocation(fixture.getTarget(), method);
                }

                if (method.isAnnotationPresent(BeforeFirstExpectation.class)) {
                    beforeFirstExpectationMessage = new StaticInvocation(fixture.getTarget(), method);
                }

                if (method.isAnnotationPresent(AfterRow.class)) {
                    afterRowMessage = new StaticInvocation(fixture.getTarget(), method);
                }
            }
        }
        LOG.trace(EXIT);
    }

    protected void doCell(Column column, Example cell) {
        LOG.debug(ENTRY_WITH_TWO, column.toString(), cell.toString());
        try {
            stats.tally(column.doCell(cell));
        } catch (IllegalArgumentException e) {
            cell.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
        } catch (InvocationTargetException e) {
            cell.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
        } catch (IllegalAccessException e) {
            cell.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
        }
        LOG.debug(EXIT);
    }
}
