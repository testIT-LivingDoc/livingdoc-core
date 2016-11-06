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
package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static info.novatec.testit.livingdoc.util.Tables.parse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.AlternateCalculator;
import info.novatec.testit.livingdoc.Assertions;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.interpreter.DecisionTableInterpreter;
import info.novatec.testit.livingdoc.interpreter.SetOfInterpreter;
import info.novatec.testit.livingdoc.interpreter.collection.RowFixtureTarget;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.Tables;


public class DocumentTest {
    private Tables tables;

    @Test
    public void testInterpretsASequenceOfTables() {
        tables = parse("[" + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n"
            + "[a][b][sum?]\n" + "[6][2][8]\n" + "[5][2][8]\n" + "****\n" + "[" + SetOfInterpreter.class.getName() + "]["
            + RowFixtureTarget.class.getName() + "]\n" + "[a][b][c]\n" + "[1][2][3]");

        Document document = Document.text(tables);
        execute(document);
        assertEquals(2, document.getStatistics().rightCount());
        assertEquals(3, document.getStatistics().wrongCount());
    }

    @Test
    public void testComplainsIfInterpreterCannotBeLoaded() {
        tables = parse("[NotAnInterpreter]\n" + "[1][2][3]");
        execute(document());
        Assertions.assertAnnotatedException(tables.at(0, 0, 0));
    }

    @Test
    public void testDocumentCanBeFiltered() {
        tables = parse("[" + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n"
            + "[a][b][sum()]\n" + "[6][2][8]\n" + "****\n" + "[Begin Info]\n" + "****\n" + "[should not be interpreted]"
            + "****\n" + "[End Info]\n" + "****\n" + "[" + DecisionTableInterpreter.class.getName() + "]["
            + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n" + "[6][2][8]\n" + "****\n" + "[Begin Info]\n"
            + "****\n" + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "[section]\n" + "[unix]\n"
            + "****\n" + "****\n" + "[should not be interpreted]" + "****\n" + "[section]\n" + "****\n" + "[Begin Info]\n"
            + "****\n" + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "["
            + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n"
            + "[6][2][8]\n");

        Document document = document();
        document.addFilter(new CommentTableFilter());
        document.addFilter(new SectionsTableFilter("mac"));
        execute(document);
        assertEquals(3, document.getStatistics().rightCount());
    }

    @Test
    public void testInLazyModeDocumentWithNoLivingDocTestTagIsNotInterpreted() {
        tables = parse("[" + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n"
            + "[a][b][sum()]\n" + "[6][2][8]\n" + "****\n" + "[Begin Info]\n" + "****\n" + "[should not be interpreted]"
            + "****\n" + "[End Info]\n" + "****\n" + "[" + DecisionTableInterpreter.class.getName() + "]["
            + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n" + "[6][2][8]\n" + "****\n" + "[Begin Info]\n"
            + "****\n" + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "[section]\n" + "[unix]\n"
            + "****\n" + "****\n" + "[should not be interpreted]" + "****\n" + "[section]\n" + "****\n" + "[Begin Info]\n"
            + "****\n" + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "["
            + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n"
            + "[6][2][8]\n");

        Document document = document();
        document.addFilter(new CommentTableFilter());
        document.addFilter(new SectionsTableFilter("mac"));
        document.addFilter(new LivingDocTableFilter(true));
        execute(document);
        assertEquals(0, document.getStatistics().rightCount());
    }

    @Test
    public void testInLazyModeDocumentWithLivingDocTestTagIsInterpretedWhereTagsAre() {
        tables = parse("[" + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "[" + DecisionTableInterpreter.class.getName()
            + "][" + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n" + "[6][2][8]\n" + "****\n"
            + "[Begin Info]\n" + "****\n" + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "["
            + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n"
            + "[6][2][8]\n" + "****\n" + "[Begin Info]\n" + "****\n" + "[should not be interpreted]" + "****\n"
            + "[End Info]\n" + "****\n" + "[section]\n" + "[unix]\n" + "****\n" + "****\n" + "[should not be interpreted]"
            + "****\n" + "[section]\n" + "****\n" + "[Begin Info]\n" + "****\n" + "[should not be interpreted]" + "****\n"
            + "[End Info]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST + "]\n" + "****\n" + "["
            + DecisionTableInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" + "[a][b][sum()]\n"
            + "[6][2][8]\n");

        Document document = document();
        document.addFilter(new CommentTableFilter());
        document.addFilter(new SectionsTableFilter("mac"));
        document.addFilter(new LivingDocTableFilter(true));
        execute(document);
        assertEquals(2, document.getStatistics().rightCount());
    }

    @Test
    public void testThatFirstCellOfTableCanSpecifyAnInterpreterClass() {
        tables = parse("[" + InterpreterExpectingASystemUnderDevelopment.class.getName() + "]");
        Example cell = tables.at(0, 0, 0);
        execute(document());
        assertNotAnnotated(cell);
    }

    @Test
    public void testThatInterpreterCanHaveAFixtureSpecifiedInSecondCell() {
        tables = parse("[" + InterpreterExpectingAFixture.class.getName() + "][" + Target.class.getName() + "]");

        execute(document());
        assertNotAnnotated(tables.at(0, 0, 0));
        assertNotAnnotated(tables.at(0, 0, 1));
    }

    @Test
    public void testComplainsWhenFixtureIntantiationFails() {
        tables = parse("[" + InterpreterExpectingAFixture.class.getName() + "][NonExistingFixture]");

        execute(document());
        Assertions.assertAnnotatedException(tables.at(0, 0, 0));
    }

    private Document document() {
        return Document.text(tables);
    }

    private void execute(Document document) {
        final DefaultSystemUnderDevelopment systemUnderDevelopment = new DefaultSystemUnderDevelopment();
        document.execute(new LivingDocInterpreterSelector(systemUnderDevelopment));
    }

    public static class InterpreterExpectingASystemUnderDevelopment implements Interpreter {
        public InterpreterExpectingASystemUnderDevelopment(SystemUnderDevelopment sud) {
            // No implementation needed.
        }

        @Override
        public void interpret(Specification specification) {
            specification.nextExample();
        }

        public void execute(Example example) {
            // No implementation needed.
        }
    }

    public static class InterpreterExpectingAFixture implements Interpreter {
        public InterpreterExpectingAFixture(Fixture fixture) {
            // No implementation needed.
        }

        @Override
        public void interpret(Specification specification) {
            specification.nextExample();
        }

        public void execute(Example example) {
            // No implementation needed.
        }
    }

    public static class Target {
        // No implementation needed.
    }
}
