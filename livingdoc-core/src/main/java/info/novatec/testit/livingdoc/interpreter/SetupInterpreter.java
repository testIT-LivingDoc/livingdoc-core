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
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_TWO;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import info.novatec.testit.livingdoc.reflect.*;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.AnnotateSetup;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;


public class SetupInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(SetupInterpreter.class);

    private final Fixture fixture;
    private Column[] columns;
    private Message enterRowMessage;
    private Statistics stats;

    public SetupInterpreter(Fixture fixture) {
        super();
        this.fixture = fixture;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        stats = new Statistics();
        Example table = specification.nextExample();
        columns = parseColumns(table);
        enterRowMessage = parseEnterRowMessage(table);

        for (Example row = table.at(0, 2); row != null && canContinue(stats); row = row.nextSibling()) {
            doRow(row);

            if (shouldStop(stats)) {
                row.firstChild().lastSibling().addSibling().annotate(Annotations.stopped());
            }
        }

        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }

    private Message parseEnterRowMessage(Example table) {
        LOG.trace(ENTRY_WITH, table.toString());

        for (Method method : fixture.getTarget().getClass().getMethods()) {
            if (method.isAnnotationPresent(EnterRow.class)) {
                Message staticInvocation = new StaticInvocation(fixture.getTarget(), method);
                LOG.trace(EXIT_WITH, staticInvocation);
                return staticInvocation;
            }
        }

        try {
            Message checkEnterRow = fixture.check("enterRow");
            LOG.trace(EXIT_WITH, checkEnterRow.toString());
            return checkEnterRow;
        } catch (NoSuchMessageException e) {
            table.at(0, 1, 0).annotate(exception(e));
            stats.exception();
            LOG.trace(EXIT + " with null");
            return null;
        }
    }

    private Column[] parseColumns(Example table) {
        LOG.trace(ENTRY_WITH, table.toString());
        Example headers = table.at(0, 1, 0);
        if (headers == null) {
            Column[] nullHeaderColumn = new Column[0];
            LOG.trace(EXIT_WITH, nullHeaderColumn.toString());
            return nullHeaderColumn;
        }

        Column[] arrayColumn = new Column[headers.remainings()];
        for (int i = 0; i < headers.remainings(); i ++ ) {
            arrayColumn[i] = parseColumn(headers.at(i));
        }

        if (shouldStop(stats)) {
            headers.lastSibling().addSibling().annotate(Annotations.stopped());
        }

        LOG.trace(EXIT_WITH, arrayColumn.toString());
        return arrayColumn;
    }

    private Column parseColumn(Example header) {
        LOG.trace(ENTRY_WITH, header.toString());
        try {
            Column parsedColumn = HeaderForm.parse(header.getContent()).selectColumn(fixture);
            LOG.trace(EXIT_WITH, parsedColumn.toString());
            return parsedColumn;
        } catch (NoSuchMessageException e) {
            header.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
            Column nullColumn = new NullColumn();
            LOG.trace(EXIT_WITH, nullColumn.toString());
            return nullColumn;
        }
    }

    private void doRow(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        if ( ! row.hasChild()) {
            LOG.trace(EXIT + " - " + row.toString() + " has no child.");
            return;
        }

        Example cells = row.firstChild();

        for (int i = 0; i < cells.remainings() && canContinue(stats); i ++ ) {
            final Example cell = cells.at(i);

            if (i < columns.length) {
                doCell(columns[i], cell);
            }
        }

        if (canContinue(stats)) {
            callEnterRow(row);
        }
        LOG.trace(EXIT);
    }

    private void callEnterRow(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        try {
            Call call = new Call(enterRowMessage);
            call.will(new AnnotateSetup(row));
            call.will(Compile.statistics(stats)).when(ResultIs.exception());
            call.execute();
        } catch (InvocationTargetException | IllegalAccessException | NullPointerException e) {
            stats.exception();
            LOG.error(LOG_ERROR, e);
        }
        LOG.trace(EXIT);
    }

    private void doCell(Column column, Example cell) {
        LOG.trace(ENTRY_WITH_TWO, column.toString(), cell.toString());
        try {
            stats.tally(column.doCell(cell));
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            cell.annotate(exception(e));
            stats.exception();
            LOG.error(LOG_ERROR, e);
        }
        LOG.trace(EXIT);
    }
}
