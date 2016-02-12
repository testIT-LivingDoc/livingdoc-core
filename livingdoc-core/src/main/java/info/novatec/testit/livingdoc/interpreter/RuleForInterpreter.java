/* Copyright (c) 2006 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */
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
import java.util.Arrays;

import info.novatec.testit.livingdoc.reflect.*;
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


public class RuleForInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(RuleForInterpreter.class);

    protected final Fixture fixture;
    protected Statistics stats;
    private Column[] columns;
    private Message beforeRowMessage = null;
    private Message beforeFirstExpectationMessage = null;
    private Message afterRowMessage = null;
    private Message beforeTableMessage = null;
    private Message afterTableMessage = null;

    public RuleForInterpreter(Fixture fixture) {
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

    private Column[] parseColumns(Example table) {
        LOG.trace(ENTRY_WITH, table.toString());
        Example headers = table.at(0, 1, 0);
        if (headers == null) {
            Column[] newColumn = new Column[0];
            LOG.trace(EXIT_WITH, newColumn.toString());
            return newColumn;
        }

        Column[] arrayColumn = new Column[headers.remainings()];
        for (int i = 0; i < headers.remainings(); i ++ ) {
            arrayColumn[i] = parseColumn(headers.at(i));
        }

        if (shouldStop(stats)) {
            headers.lastSibling().addSibling().annotate(Annotations.stopped());
        }

        LOG.trace(EXIT_WITH, Arrays.toString(arrayColumn));
        return arrayColumn;
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

            if (i < columns.length) {
                if ( ! beforeFirstExpectationAlreadyCalled) {
                    beforeFirstExpectationAlreadyCalled = checkFirstExpectation(columns[i]);
                }
                doCell(columns[i], cell);
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
        } catch (InvocationTargetException | IllegalAccessException e) {
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
