/* Copyright (c) 2007 Pyxis Technologies inc.
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

package info.novatec.testit.livingdoc.fixture.interpreter.flow.workflow;


import info.novatec.testit.livingdoc.interpreter.flow.AbstractRow;
import info.novatec.testit.livingdoc.interpreter.flow.RowSelector;
import info.novatec.testit.livingdoc.interpreter.flow.workflow.WorkflowRowSelector;
import info.novatec.testit.livingdoc.interpreter.flow.Action;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass
public class RowFormWorkflowFixture {
    private Tables tables;
    private AbstractRow row;

    public void setRow(Tables row) {
        this.tables = row;
        this.row = parseCommand();
    }

    public String typeOfAction() {
        return row.getClass().getSimpleName().replaceAll("Row", "");
    }

    private AbstractRow parseCommand() {
        RowSelector selector = new WorkflowRowSelector(new PlainOldFixture(new Object()));

        return ( AbstractRow ) selector.select(tables.at(0, 0));
    }

    public String actionName() {
        Action action = Action.parse(row.actionCells(tables.at(0, 0)));
        return action.name();

    }

    public String[] parameters() {
        Action action = Action.parse(row.actionCells(tables.at(0, 0)));
        return action.arguments();
    }
}
