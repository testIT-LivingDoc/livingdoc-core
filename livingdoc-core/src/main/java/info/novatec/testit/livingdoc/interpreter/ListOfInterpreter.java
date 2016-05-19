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
import static info.novatec.testit.livingdoc.annotation.Annotations.ignored;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_THREE;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.interpreter.collection.CollectionInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;


/**
 * <code>Interpreter</code> implementation that checks the properties of a set
 * of arbitrary objects against values of a set of <code>Specification</code>.
 */
public class ListOfInterpreter extends CollectionInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(ListOfInterpreter.class);

    public ListOfInterpreter(Fixture fixture) {
        super(fixture);
    }

    @Override
    public void execute(Example example) {
        LOG.debug(ENTRY_WITH, example.toString());
        try {
            List<Fixture> fixtures = getFixtureList();

            Example headers = example.at(0, 0);

            Iterator<Fixture> it = fixtures.iterator();

            Example row;
            for (row = example.nextSibling(); row != null && it.hasNext() && canContinue(stats); row = row.nextSibling()) {
                processRow(row.firstChild(), headers, it.next());

                if (shouldStop(stats)) {
                    row.addChild().annotate(Annotations.stopped());
                }
            }

            while (row != null && canContinue(stats)) {
                missingRow(row);

                if (shouldStop(stats)) {
                    row.addChild().annotate(Annotations.stopped());
                }

                row = row.nextSibling();
            }

            while (it.hasNext() && canContinue(stats)) {
                Fixture adapter = it.next();
                addSurplusRow(example, headers, adapter);

                if (shouldStop(stats)) {
                    example.lastSibling().addChild().annotate(Annotations.stopped());
                    break;
                }
            }
        } catch (InvocationTargetException e) {
            example.firstChild().annotate(exception(e));
            stats.exception();

            if (shouldStop(stats)) {
                example.addChild().annotate(Annotations.stopped());
            }
        } catch (IllegalAccessException e) {
            example.firstChild().annotate(exception(e));
            stats.exception();

            if (shouldStop(stats)) {
                example.addChild().annotate(Annotations.stopped());
            }
        } catch (NoSuchMessageException e) {
            example.firstChild().annotate(exception(e));
            stats.exception();

            if (shouldStop(stats)) {
                example.addChild().annotate(Annotations.stopped());
            }
        }
        LOG.debug(EXIT);
    }

    protected void processRow(Example valuesRow, Example headers, Fixture rowFixtureAdapter) throws NoSuchMessageException,
        IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        LOG.debug(ENTRY_WITH_THREE, valuesRow.toString(), headers.toString(), rowFixtureAdapter.toString());
        Statistics rowStats = new Statistics();

        for (int i = 0; i != valuesRow.remainings(); ++ i) {
            Example cell = valuesRow.at(i);

            if (i < headers.remainings()) {
                Call call = new Call(rowFixtureAdapter.check(headers.at(i).getContent()));
                if ( ! StringUtils.isBlank(cell.getContent())) {
                    call.expect(cell.getContent());
                }

                call.will(Annotate.withDetails(cell));
                call.will(Compile.statistics(rowStats));
                call.execute();
            } else {
                cell.annotate(ignored(cell.getContent()));
            }
        }
        applyRowStatistic(rowStats);
        LOG.debug(EXIT);
    }
}
