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

import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedException;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedMissing;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedRight;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedStopped;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedSurplus;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedWrongWithDetails;
import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.interpreter.collection.RowFixtureTarget;
import info.novatec.testit.livingdoc.reflect.CollectionProvider;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


public class ListOfInterpreterTest {

    private RowFixtureTarget target = new RowFixtureTarget();
    private ListOfInterpreter interpreter = new ListOfInterpreter(new PlainOldFixture(target));
    private Tables tables;
    private static boolean stopOnFirstFailure;

    @BeforeClass
    public static void beforeClass() throws Exception {
        stopOnFirstFailure = LivingDoc.isStopOnFirstFailure();
    }

    @Before
    public void setUp() throws Exception {
        LivingDoc.setStopOnFirstFailure(false);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        LivingDoc.setStopOnFirstFailure(stopOnFirstFailure);
    }

    @Test
    public void testInterpretsOneRowOfSpecification() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[a][b][c]\n" + "[1][2][3]");
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedRight(tables.at(0, 2, 1));
        assertAnnotatedRight(tables.at(0, 2, 2));

        assertAnnotatedSurplus(tables.at(0, 3, 0));
        assertAnnotatedSurplus(tables.at(0, 4, 0));
    }

    private FakeSpecification document() {
        return new FakeSpecification(tables);
    }

    @Test
    public void testInterpretAllRowsOfSpecification() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[a][b][c]\n" + "[1][2][3]\n" + "[1][2][3]\n" + "[1][2][3]");
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedRight(tables.at(0, 2, 1));
        assertAnnotatedRight(tables.at(0, 2, 2));
        assertAnnotatedRight(tables.at(0, 3, 0));
        assertAnnotatedRight(tables.at(0, 3, 1));
        assertAnnotatedRight(tables.at(0, 3, 2));
        assertAnnotatedRight(tables.at(0, 4, 0));
        assertAnnotatedRight(tables.at(0, 4, 1));
        assertAnnotatedRight(tables.at(0, 4, 2));
    }

    @Test
    public void testInterpreterWhenSpecificationDoesRespectsOrder() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]\n" + "[3]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedRight(tables.at(0, 3, 0));
        assertAnnotatedRight(tables.at(0, 4, 0));
    }

    @Test
    public void testInterpreterWhenSpecificationDoesNotRespectOrder() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[3]\n" + "[2]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedWrongWithDetails(tables.at(0, 3, 0));
        assertAnnotatedWrongWithDetails(tables.at(0, 4, 0));
    }

    @Test
    public void testInterpreterCanWorkWithCollectionReturnedFromQueryAction() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[a][b][c]\n" + "[1][2][3]");

        interpreter = new ListOfInterpreter(fixtureThatReturnsABC());
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedRight(tables.at(0, 2, 1));
        assertAnnotatedRight(tables.at(0, 2, 2));
    }

    @Test
    public void testInterpreterCollectsTestExecutionStatistics() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[a][b][c]\n" + "[1][2][4]");

        interpreter = new ListOfInterpreter(fixtureThatReturnsABC());
        interpreter.interpret(document());

        assertAnnotatedRight(tables.at(0, 2, 0));
        assertAnnotatedRight(tables.at(0, 2, 1));
        assertAnnotatedWrongWithDetails(tables.at(0, 2, 2));
    }

    @Test
    public void testFixtureReturnsMoreRows() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        interpreter.interpret(document());

        assertAnnotatedSurplus(tables.at(0, 4, 0));
    }

    @Test
    public void testFixtureReturnsMoreRowsUsingStopOnFirstFailure() throws Exception {
        LivingDoc.setStopOnFirstFailure(true);

        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedSurplus(tables.at(0, 4, 0));
        assertAnnotatedStopped(tables.at(0, 4, 1));
    }

    @Test
    public void testFixtureReturnsFewerRows() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]\n" + "[3]\n" + "[4]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        interpreter.interpret(document());
        assertAnnotatedMissing(tables.at(0, 5, 0));
    }

    @Test
    public void testFixtureReturnsFewerRowsUsingStopOnFirstFailure() throws Exception {
        LivingDoc.setStopOnFirstFailure(true);

        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]\n" + "[3]\n" + "[4]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedMissing(tables.at(0, 5, 0));
        assertAnnotatedStopped(tables.at(0, 5, 1));
    }

    @Test
    public void testFixtureThatReturnsEmptyCollection() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsEmptyCollection());
        interpreter.interpret(document());
        assertAnnotatedMissing(tables.at(0, 2, 0));
        assertAnnotatedMissing(tables.at(0, 3, 0));
    }

    @Test
    public void testFixtureThatReturnsEmptyCollectionUsingStopOnFirstFailure() throws Exception {
        LivingDoc.setStopOnFirstFailure(true);

        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]\n" + "[2]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsEmptyCollection());

        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedMissing(tables.at(0, 2, 0));
        assertAnnotatedStopped(tables.at(0, 2, 1));
        assertNotAnnotated(tables.at(0, 3, 0));
    }

    @Test
    public void testCanAccessTheCollectionproviderAttributes() {
        tables = Tables.parse("[list of][values]\n" + "[A][B][C]\n" + "[1][2][3]\n" + "[1][2][3]\n" + "[1][2][3]");
        interpreter = new ListOfInterpreter(new PlainOldFixture(new TestObjectWithCollectionProvider(1)));
        interpreter.interpret(document());
        assertEquals(2, interpreter.statistics().wrongCount());
    }

    @Test
    public void testThatCellWithMethodThrowingAnExceptionIsAnnotated() throws Exception {
        tables = Tables.parse("[list of][values]\n" + "[ex]\n" + "[1]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedException(tables.at(0, 2, 0));
    }

    @Test
    public void testThatCellWithMethodThrowingAnExceptionIsAnnotatedStopWhenStopOnFirstFailure() throws Exception {
        LivingDoc.setStopOnFirstFailure(true);

        tables = Tables.parse("[list of][values]\n" + "[ex]\n" + "[1]");
        interpreter = new ListOfInterpreter(fixtureThatReturnsFoosAndBars());
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedException(tables.at(0, 2, 0));
        assertAnnotatedStopped(tables.at(0, 2, 1));
    }

    @Test
    public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToException() {
        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]");
        interpreter = new ListOfInterpreter(new DefaultFixture(new FixtureThatHasNoCollection()));
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedException(tables.at(0, 1, 0));
    }

    @Test
    public void testThatFixtureWithoutAProperCollectionQueryHasAnnotatedCellToExceptionAndStopWhenStopOnFirstFailure() {
        LivingDoc.setStopOnFirstFailure(true);

        tables = Tables.parse("[list of][values]\n" + "[bar]\n" + "[1]");
        interpreter = new ListOfInterpreter(new DefaultFixture(new FixtureThatHasNoCollection()));
        FakeSpecification document = document();
        interpreter.interpret(document);
        assertTrue(document.stats().indicatesFailure());
        assertAnnotatedException(tables.at(0, 1, 0));
        assertAnnotatedStopped(tables.at(0, 1, 1));
    }

    private Fixture fixtureThatReturnsABC() {
        return new PlainOldFixture(new FixtureThatReturnsABCRowCollection());
    }

    private Fixture fixtureThatReturnsFoosAndBars() {
        return new PlainOldFixture(new FixtureThatReturnsCollectionOfFoo(3));
    }

    private Fixture fixtureThatReturnsEmptyCollection() {
        return new PlainOldFixture(new FixtureThatReturnsCollectionOfFoo(0));
    }

    public static class FixtureThatReturnsABCRowCollection {

        public Collection<Object> query() {
            ArrayList<Object> list = new ArrayList<Object>();

            list.add(new RowFixtureTarget.TestObject());
            return list;
        }
    }

    public class FixtureThatHasNoCollection {
        // No implementation needed.
    }

    public class FixtureThatReturnsCollectionOfFoo {

        private int howmany;

        public FixtureThatReturnsCollectionOfFoo(int howmany) {
            this.howmany = howmany;
        }

        public Collection<Foo> query() {
            Collection<Foo> c = new ArrayList<Foo>();
            for (int i = 0; i < howmany; i ++ ) {
                c.add(new Foo("" + ( i + 1 )));
            }
            return c;
        }

        public class Foo {

            private String bar;

            public Foo(String bar) {
                this.bar = bar;
            }

            public String getBar() {
                return bar;
            }

            public String getEx() {
                throw new RuntimeException(bar);
            }
        }
    }

    public class TestObjectWithCollectionProvider {

        private int howMany;

        public TestObjectWithCollectionProvider(int howMany) {
            this.howMany = howMany;
        }

        @CollectionProvider()
        public Collection<Object> SomeMethod() {
            List<Object> objects = new ArrayList<Object>();

            for (int i = 0; i != howMany; ++ i) {
                objects.add(new RowFixtureTarget.TestObject());
            }
            return objects;
        }
    }
}
