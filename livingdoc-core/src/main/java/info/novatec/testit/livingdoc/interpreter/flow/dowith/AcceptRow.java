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

package info.novatec.testit.livingdoc.interpreter.flow.dowith;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.interpreter.flow.AbstractRow;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class AcceptRow extends AbstractRow {
    private static final Logger LOG = LoggerFactory.getLogger(AcceptRow.class);

    public static boolean matches(String keyword) {
        return "accept".equalsIgnoreCase(keyword);
    }

    public AcceptRow(Fixture fixture) {
        super(fixture);
    }

    @Override
    public void interpret(Specification table) {
        Example row = table.nextExample();
        Example accept = row.firstChild();
        Action action = Action.parse(actionCells(row));
        try {
            Call call = action.checkAgainst(fixture);
            call.expect(ShouldBe.TRUE);
            call.will(Annotate.right(accept)).when(ResultIs.right());
            call.will(Annotate.wrong(accept)).when(ResultIs.wrong());
            call.will(Annotate.exception(accept)).when(ResultIs.exception());
            call.will(tallyStatistics(table));
            call.execute();
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            reportException(table);
            CollectionUtil.first(keywordCells(row)).annotate(Annotations.exception(e));
        }
    }

    private List<Example> keywordCells(Example row) {
        return CollectionUtil.odd(row.firstChild());
    }

    @Override
    public List<Example> actionCells(Example row) {
        return ExampleUtil.asList(row.at(0, 1));
    }
}
