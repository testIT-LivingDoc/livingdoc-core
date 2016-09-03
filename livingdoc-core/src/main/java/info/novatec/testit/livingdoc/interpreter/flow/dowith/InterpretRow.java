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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.interpreter.flow.AbstractRow;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.ExampleWrapper;


@Deprecated
public class InterpretRow extends AbstractRow {
    public InterpretRow(Fixture fixture) {
        super(fixture);
    }

    @Override
    public List<Example> actionCells(Example row) {
        return ExampleUtil.asList(row.at(0, 1));
    }

    @Override
    public void interpret(Specification table) {
        final Example row = table.nextExample();

        Document document = Document.text(ExampleWrapper.sandbox(row));
        document.execute(new LivingDocInterpreterSelector(systemUnderDevelopment()));
        table.exampleDone(document.getStatistics());

        while (table.hasMoreExamples()) {
            table.nextExample();
        }
    }

    private SystemUnderDevelopment systemUnderDevelopment() {
        return new DefaultSystemUnderDevelopment() {
            @Override
            public Fixture getFixture(String name, String... params) throws NoSuchMethodException, IllegalArgumentException,
                InvocationTargetException, IllegalAccessException {
                List<String> cells = new ArrayList<String>();
                cells.add(name);
                cells.addAll(Arrays.asList(params));
                Action action = new Action(cells);
                Call call = action.checkAgainst(fixture);
                Object target = call.execute();
                return fixture.fixtureFor(target);
            }

            @Override
            public void addImport(String packageName) {
                // No implementation needed.
            }
        };
    }
}
