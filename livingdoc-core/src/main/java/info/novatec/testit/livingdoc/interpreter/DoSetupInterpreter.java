/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.expectation.ShouldBe.FALSE;
import static info.novatec.testit.livingdoc.expectation.ShouldBe.instanceOf;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_TWO;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.annotation.EnteredAnnotation;
import info.novatec.testit.livingdoc.annotation.SkippedAnnotation;
import info.novatec.testit.livingdoc.call.Result;
import info.novatec.testit.livingdoc.call.Stub;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.interpreter.flow.dowith.Action;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class DoSetupInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(DoSetupInterpreter.class);

    private final Fixture fixture;
    private Statistics stats;
    private boolean skip;

    public DoSetupInterpreter(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        stats = new Statistics();
        Example table = specification.nextExample();

        for (Example row = table.at(0, 1); row != null; row = row.nextSibling()) {
            if (skip) {
                annotateSkipped(row);
            } else if ( ! doRow(row)) {
                skip = true;
            }
        }

        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }

    private boolean doRow(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        Action action = Action.parse(actionCells(row));

        try {
            Call call = action.checkAgainst(fixture);
            call.expect(ShouldBe.either(FALSE).or(instanceOf(Exception.class)).negate());
            call.will(new AnnotateDoSetup(row));
            call.execute();

            boolean doRowWasRight = call.wasRight();
            LOG.trace(EXIT_WITH, doRowWasRight);
            return doRowWasRight;
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMessageException e) {
            LOG.error(LOG_ERROR, e);
            stats.exception();
            annotateException(row, e);
        }

        LOG.trace(EXIT_WITH, false);
        return false;
    }

    private void annotateException(Example row, Exception e) {
        LOG.trace(ENTRY_WITH_TWO, row.toString(), e.toString());
        Example newLastCell = row.addChild();
        newLastCell.annotate(Annotations.exception(e));
        LOG.trace(EXIT);
    }

    private void annotateEntered(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        Example newLastCell = row.addChild();
        newLastCell.annotate(new EnteredAnnotation());
        LOG.trace(EXIT);
    }

    private void annotateSkipped(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        Example newLastCell = row.addChild();
        newLastCell.annotate(new SkippedAnnotation());
        LOG.trace(EXIT);
    }

    private List<Example> actionCells(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());

        LOG.trace(EXIT_WITH, ExampleUtil.asList(row.firstChild()).toString());
        return ExampleUtil.asList(row.firstChild());
    }

    private class AnnotateDoSetup implements Stub {
        private final Example row;

        public AnnotateDoSetup(Example row) {
            this.row = row;
        }

        @Override
        public void call(Result result) {
            LOG.trace(ENTRY_WITH, result.toString());
            if (result.isRight()) {
                annotateEntered(row);
            } else {
                annotateSkipped(row);
            }
            LOG.trace(EXIT);
        }
    }
}
