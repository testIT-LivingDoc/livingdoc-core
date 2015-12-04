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

package info.novatec.testit.livingdoc.interpreter.collection;

import static info.novatec.testit.livingdoc.LivingDoc.canContinue;
import static info.novatec.testit.livingdoc.LivingDoc.shouldStop;
import static info.novatec.testit.livingdoc.annotation.Annotations.exception;
import static info.novatec.testit.livingdoc.annotation.Annotations.ignored;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_THREE;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_TWO;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH_NULL;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Compile;
import info.novatec.testit.livingdoc.interpreter.AbstractInterpreter;
import info.novatec.testit.livingdoc.interpreter.HeaderForm;
import info.novatec.testit.livingdoc.reflect.CollectionProvider;
import info.novatec.testit.livingdoc.reflect.Fixture;


// TODO: STATS compile stats here and in derived classes ( no test for that yet
// )
public abstract class CollectionInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(CollectionInterpreter.class);

    private final Fixture fixture;
    protected final Statistics stats = new Statistics();

    public Statistics statistics() {
        return this.stats;
    }

    protected CollectionInterpreter(Fixture fixture) {
        this.fixture = fixture;
    }

    protected List< ? > toList(Object results) {
        LOG.debug(ENTRY_WITH, results.toString());
        if (results instanceof Object[]) {
            List< ? > resultsAsList = Arrays.asList(( Object[] ) results);
            LOG.debug(EXIT_WITH, resultsAsList.toString());
            return resultsAsList;
        }
        if (results instanceof Collection) {
            List< ? > newArrayList = new ArrayList<Object>(( Collection< ? > ) results);
            LOG.debug(EXIT_WITH, newArrayList.toString());
            return newArrayList;
        }
        return null;
    }

    protected void executeRow(Example valuesRow, Example headers, Fixture rowFixtureAdapter) {
        LOG.debug(ENTRY_WITH_THREE, valuesRow.toString(), headers.toString(), rowFixtureAdapter.toString());
        stats.right();
        valuesRow.annotate(Annotations.right());

        for (int i = 0; i != valuesRow.remainings(); ++ i) {
            Example cell = valuesRow.at(i);

            if (i < headers.remainings()) {
                try {
                    Call call = new Call(rowFixtureAdapter.check(headers.at(i).getContent()));

                    if ( ! StringUtils.isBlank(cell.getContent())) {
                        call.expect(cell.getContent());
                    }
                    call.will(Annotate.withDetails(cell));

                    if (HeaderForm.parse(headers.at(i).getContent()).isExpected()) {
                        call.will(Compile.statistics(stats));
                    }

                    call.execute();
                } catch (Exception e) {
                    cell.annotate(exception(e));
                    stats.exception();
                    LOG.error(LOG_ERROR, e);
                }
            } else {
                cell.annotate(ignored(cell.getContent()));
            }
        }
        LOG.debug(EXIT);
    }

    protected void addSurplusRow(Example example, Example headers, Fixture rowFixtureAdapter) {
        LOG.debug(ENTRY_WITH_THREE, example.toString(), headers.toString(), rowFixtureAdapter.toString());
        Example row = example.addSibling();

        for (int i = 0; i < headers.remainings(); i ++ ) {
            Example cell = row.addChild();
            try {
                Call call = new Call(rowFixtureAdapter.check(headers.at(i).getContent()));

                Object actual = call.execute();

                cell.setContent(TypeConversion.toString(actual));
                cell.annotate(Annotations.surplus());
                if (i == 0) // Notify test listener on first cell only
                {
                    stats.wrong();
                }
            } catch (Exception e) {
                // TODO: STATS count stats?
                cell.annotate(ignored(e));
                LOG.error(LOG_ERROR, e);
            }
        }
        LOG.debug(EXIT);
    }

    protected void missingRow(Example row) {
        LOG.debug(ENTRY_WITH, row.toString());
        Example firstChild = row.firstChild();

        firstChild.annotate(Annotations.missing());
        stats.wrong();

        if (firstChild.hasSibling()) {
            for (Example cell : firstChild.nextSibling()) {
                cell.annotate(Annotations.missing());
            }
        }
        LOG.debug(EXIT);
    }

    private List< ? > getCollectionProvider() {
        LOG.trace(ENTRY);
        Object target = fixture.getTarget();

        for (Method method : target.getClass().getMethods()) {
            if (method.isAnnotationPresent(CollectionProvider.class)) {
                List< ? > result = toList(invoke(target, method));
                LOG.trace(EXIT_WITH, result.toString());
                return result;
            }
        }
        LOG.trace(EXIT_WITH_NULL);
        return null;
    }

    private Object invoke(Object target, Method method) {
        LOG.trace(ENTRY_WITH_TWO, target.toString(), method.toString());
        try {
            Object result = method.invoke(target);
            LOG.trace(EXIT_WITH, result.toString());
            return result;
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            LOG.trace(EXIT_WITH_NULL);
            return null;
        }
    }

    public List<Fixture> getFixtureList() throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        LOG.debug(ENTRY);
        List< ? > results = getCollectionProvider();

        if (results == null) {
            results = toList(fixture.getTarget());
        }

        if (results == null) {
            Call query = new Call(fixture.check("query"));
            results = toList(query.execute());
        }

        if (results == null) {
            throw new IllegalArgumentException("results parameter is neither an Object[] nor a Collection");
        }

        List<Fixture> fixtures = new ArrayList<Fixture>();
        for (Object object : results) {
            fixtures.add(fixture.fixtureFor(object));
        }

        LOG.debug(EXIT_WITH, fixtures.toString());
        return fixtures;
    }

    protected boolean mustProcessMissing() {
        LOG.debug(ENTRY);
        LOG.debug(EXIT_WITH, false);
        return false;
    }

    protected boolean mustProcessSurplus() {
        LOG.debug(ENTRY);
        LOG.debug(EXIT_WITH, false);
        return false;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        Example table = specification.nextExample();
        execute(table.at(0, 1));
        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }

    public void execute(Example example) {
        LOG.debug(ENTRY_WITH, example.toString());
        try {
            List<Fixture> fixtures = getFixtureList();

            Example headers = example.at(0, 0);

            RowFixtureSplitter splitter = new RowFixtureSplitter();

            splitter.split(example.at(1), fixtures, headers);

            for (RowFixture rowFixture : splitter.getMatch()) {
                Example row = rowFixture.getRow();
                executeRow(row.firstChild(), headers, rowFixture.getAdapter());

                if (shouldStop(stats)) {
                    row.addChild().annotate(Annotations.stopped());
                    break;
                }
            }

            if (mustProcessMissing() && canContinue(stats)) {
                for (Example row : splitter.getMissing()) {
                    missingRow(row);

                    if (shouldStop(stats)) {
                        row.addChild().annotate(Annotations.stopped());
                        break;
                    }
                }
            }

            if (mustProcessSurplus() && canContinue(stats)) {
                for (Fixture adapter : splitter.getSurplus()) {
                    addSurplusRow(example, headers, adapter);

                    if (shouldStop(stats)) {
                        example.lastSibling().addChild().annotate(Annotations.stopped());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            stats.exception();
            example.firstChild().annotate(exception(e));

            if (shouldStop(stats)) {
                example.addChild().annotate(Annotations.stopped());
            }
            LOG.error(LOG_ERROR, e);
        }
    }

    protected void applyRowStatistic(Statistics rowStats) {
        LOG.debug(ENTRY_WITH, rowStats.toString());
        if (rowStats.exceptionCount() > 0) {
            stats.exception();
        } else if (rowStats.wrongCount() > 0) {
            stats.wrong();
        } else if (rowStats.rightCount() > 0) {
            stats.right();
        } else {
            stats.ignored();
        }
        LOG.debug(EXIT);
    }
}
