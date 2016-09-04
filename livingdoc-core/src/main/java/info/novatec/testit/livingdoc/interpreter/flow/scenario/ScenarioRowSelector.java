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
package info.novatec.testit.livingdoc.interpreter.flow.scenario;

import static info.novatec.testit.livingdoc.LivingDoc.isAnInterpreter;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.interpreter.flow.InterpretRow;
import info.novatec.testit.livingdoc.interpreter.flow.Row;
import info.novatec.testit.livingdoc.interpreter.flow.RowSelector;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class ScenarioRowSelector implements RowSelector {
    private final Fixture fixture;

    public ScenarioRowSelector(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public Row select(Example example) {
        if (isAnInterpreter(identifier(example))) {
            return new InterpretRow(fixture);
        }

        return new ScenarioRow(fixture);
    }

    private String identifier(Example row) {
        return ExampleUtil.contentOf(row.firstChild());
    }
}
