/* Copyright (c) 2009 Pyxis Technologies inc.
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

import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedEntered;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedSkipped;
import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


@RunWith(MockitoJUnitRunner.class)
public class DoSetupInterpreterTest {

    private Tables tables;
    private DoSetupInterpreter interpreter;
    @Mock
    private Target fixture;

    @Before
    public void setUp() throws Exception {
        interpreter = new DoSetupInterpreter(code());
    }

    @Test
    public void testATableDefinesASequenceOfActions() throws Exception {
        doReturn(true).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[do setup][mock]\n" + "[commandTakingNoParameter]\n" + "[commandTakingNoParameter]");
        interpreter.interpret(document());

        verify(fixture, times(2)).commandTakingNoParameter();
    }

    @Test
    public void testStatisticShouldBeInFailuresWithNonExistingMethod() {
        tables = Tables.parse("[do setup][mock]\n" + "[non existing method]\n");

        FakeSpecification specification = document();
        interpreter.interpret(specification);

        assertTrue(specification.stats().hasFailed());
    }

    @Test
    public void testStatisticShouldBeInFailuresWithBadNumberOfMethodParameter() {
        tables = Tables.parse("[do setup][mock]\n" + "[commandExpectingParameters]\n");

        FakeSpecification specification = document();
        interpreter.interpret(specification);

        assertTrue(specification.stats().hasFailed());
    }

    @Test
    public void testActionsAcceptParametersInEveryOtherCell() throws Exception {
        tables = Tables.parse("[do setup][mock]\n"
            + "[command] [a parameter] [expecting] [a second parameter] [parameters] [a third parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
    }

    @Test
    public void testKeywordsContainingSpacesAreCamelized() throws Exception {
        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingASingleParameter("parameter");
    }

    @Test
    public void testLastNewCellsAreAnnotatedEnteredIfTheyReturnTrue() throws Exception {
        doReturn(true).when(fixture).commandExpectingASingleParameter("parameter");

        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[command expecting a single parameter][parameter]\n");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture, times(2)).commandExpectingASingleParameter("parameter");
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedEntered(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertNotAnnotated(tables.at(0, 2, 1));
        assertAnnotatedEntered(tables.at(0, 2, 2));
        assertFalse(specification.stats().hasFailed());
    }

    @Test
    public void testLastNewCellsAreAnnotatedSkippedIfTheyReturnAnException() throws Exception {
        doThrow(RuntimeException.class).when(fixture).commandExpectingASingleParameter("parameter");

        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[command expecting a single parameter][parameter]\n");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).commandExpectingASingleParameter("parameter");
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedSkipped(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertNotAnnotated(tables.at(0, 2, 1));
        assertAnnotatedSkipped(tables.at(0, 2, 2));
        assertFalse(specification.stats().hasFailed());
    }

    @Test
    public void testLastNewCellsAreAnnotatedSkippedIfTheyReturnFalse() throws Exception {
        doReturn(false).when(fixture).commandExpectingASingleParameter("parameter");

        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[command expecting a single parameter][parameter]\n");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).commandExpectingASingleParameter("parameter");
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedSkipped(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertNotAnnotated(tables.at(0, 2, 1));
        assertAnnotatedSkipped(tables.at(0, 2, 2));
        assertFalse(specification.stats().hasFailed());
    }

    @Test
    public void testLastNewCellsAreAnnotatedSkippedOnlyAfterAReturnFalse() throws Exception {
        doReturn(true).when(fixture).commandExpectingASingleParameter("parameter");
        doReturn(false).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[commandTakingNoParameter]\n" + "[command expecting a single parameter][parameter]\n");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).commandExpectingASingleParameter("parameter");
        verify(fixture).commandTakingNoParameter();
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedEntered(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertAnnotatedSkipped(tables.at(0, 2, 1));
        assertNotAnnotated(tables.at(0, 3, 0));
        assertNotAnnotated(tables.at(0, 3, 1));
        assertAnnotatedSkipped(tables.at(0, 3, 2));
        assertFalse(specification.stats().hasFailed());
    }

    @Test
    public void testLastNewCellsAreAnnotatedSkippedOnlyAfterAnException() throws Exception {
        doReturn(true).when(fixture).commandExpectingASingleParameter("parameter");
        doThrow(RuntimeException.class).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[do setup][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[commandTakingNoParameter]\n" + "[command expecting a single parameter][parameter]\n");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).commandExpectingASingleParameter("parameter");
        verify(fixture).commandTakingNoParameter();
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedEntered(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertAnnotatedSkipped(tables.at(0, 2, 1));
        assertNotAnnotated(tables.at(0, 3, 0));
        assertNotAnnotated(tables.at(0, 3, 1));
        assertAnnotatedSkipped(tables.at(0, 3, 2));
        assertFalse(specification.stats().hasFailed());
    }

    private Fixture code() {
        return new PlainOldFixture(fixture);
    }

    private FakeSpecification document() {
        return new FakeSpecification(tables);
    }

    public static interface Target {
        Object commandTakingNoParameter();

        Object commandExpectingParameters(String first, String second, String third);

        Object commandExpectingASingleParameter(String p);
    }
}
