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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.interpreter.column.GivenColumn;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;
import info.novatec.testit.livingdoc.interpreter.column.RecalledColumn;
import info.novatec.testit.livingdoc.interpreter.column.SavedColumn;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;


public class HeaderFormTest {

    @Test
    public void testExpectedColumnsEndWithQuestionMarkOrParentheses() throws Exception {
        assertThat(header("expected?"), is(instanceOf(ExpectedColumn.class)));
        assertThat(header("expected()"), is(instanceOf(ExpectedColumn.class)));
    }

    @Test
    public void testSavedColumnsEndWithEqualSign() throws Exception {
        assertThat(header("saved?="), is(instanceOf(SavedColumn.class)));
        assertThat(header("saved="), is(instanceOf(SavedColumn.class)));
    }

    @Test
    public void testRecalledColumnsStartWithEqualSign() throws Exception {
        assertThat(header("=recalled"), is(instanceOf(RecalledColumn.class)));
    }

    @Test
    public void testGivenColumnsAreTheDefault() throws Exception {
        assertThat(header("given"), is(instanceOf(GivenColumn.class)));
    }

    @Test
    public void testBlankHeaderYieldsNullColumn() throws Exception {
        assertThat(header(null), is(instanceOf(NullColumn.class)));
        assertThat(header(""), is(instanceOf(NullColumn.class)));
        assertThat(header("    "), is(instanceOf(NullColumn.class)));
    }

    @Test
    public void testStripsSpecialMarkers() {
        assertEquals("expected", HeaderForm.parse("expected?").message());
        assertEquals("expected", HeaderForm.parse("expected()").message());
        assertEquals("saved", HeaderForm.parse("saved=").message());
        assertEquals("saved", HeaderForm.parse("saved?=").message());
        assertEquals("saved", HeaderForm.parse("saved()=").message());

        assertEquals("given", HeaderForm.parse("given").message());
    }

    private Column header(String text) throws Exception {
        return HeaderForm.parse(text).selectColumn(dummyFixture());
    }

    @SuppressWarnings("unused")
    private PlainOldFixture dummyFixture() {
        return new PlainOldFixture(new Object() {
            public String expected;
            public String saved;
            public String given;
            public String recalled;
        });
    }
}
