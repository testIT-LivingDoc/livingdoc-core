package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.util.Tables.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Assertions;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.EnterRow;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


public class SetupInterpreterTest {
    private Tables tables;
    private SetupInterpreter interpreter;
    private TargetFixture targetFixture;

    @Before
    public void setUp() throws Exception {
        targetFixture = new TargetFixture();
        interpreter = new SetupInterpreter(new PlainOldFixture(targetFixture));
        LivingDoc.setStopOnFirstFailure(false); // reset
    }

    @After
    public void tearDown() throws Exception {
        LivingDoc.setStopOnFirstFailure(false); // reset
    }

    @Test
    public void testColumnHeaderSpecifiesAnInputValue() throws Exception {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[5][3]");

        interpreter.interpret(document());
        assertEquals(5, targetFixture.a);
        assertEquals(3, targetFixture.b);
    }

    @Test
    public void testExecuteMethodIsCallAfterInput() {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[5][3]\n" + "[2][3]");

        interpreter.interpret(document());
        assertEquals(21, targetFixture.product);
    }

    @Test
    public void testThatSpecificationIsBypassWhenNoEnterRowMethod() {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n");

        FakeSpecification document = document();
        SetupInterpreter interpreter2 = new SetupInterpreter(new PlainOldFixture(new TargetFixtureNoEnterRow()));
        interpreter2.interpret(document);
        assertEquals(1, document.stats().exceptionCount());

    }

    @Test
    public void testThatStatisticsEnterRowMethod() {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[2][-1]\n" + "[4][-1]\n" + "[4][2]");

        FakeSpecification document = document();
        interpreter.interpret(document);
        assertEquals(2, document.stats().exceptionCount());

    }

    @Test
    public void testThatCellWithResultIsAdded() {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[5][3]\n" + "[2][3]");

        interpreter.interpret(document());
        Assertions.assertAnnotatedEntered(tables.at(0, 2, 2));
    }

    @Test
    public void testThatCellWithResultIsAddedWithEnterRowAnnotation() {
        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[5][3]\n" + "[2][3]");

        FakeSpecification document = document();
        SetupInterpreter interpreter2 = new SetupInterpreter(new PlainOldFixture(new AnnotatedTargetFixture()));
        interpreter2.interpret(document);

        Assertions.assertAnnotatedEntered(tables.at(0, 2, 2));
    }

    @Test
    public void testThatHeaderContainingUnresolvableMethodIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure() {
        LivingDoc.setStopOnFirstFailure(true);

        tables = parse("[setup][dummy Fixture]\n" + "[a][c]\n" + "[5][3]\n" + "[2][3]");

        FakeSpecification document = document();
        interpreter.interpret(document);

        assertTrue(document.stats().hasFailed());
        Assertions.assertAnnotatedStopped(tables.at(0, 1, 2));
    }

    @Test
    public void testThatCellContainingBadValueIsAnnotatedStoppedWhenOptionsIsStopOnFirstFailure() {
        LivingDoc.setStopOnFirstFailure(true);

        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[fail][3]\n" + "[2][3]");

        FakeSpecification document = document();
        interpreter.interpret(document);

        assertTrue(document.stats().hasFailed());
        Assertions.assertAnnotatedStopped(tables.at(0, 2, 2));
    }

    @Test
    public void testResultIsMarkedWithExceptionStackWhenEnterRowThrowAnException() {
        LivingDoc.setStopOnFirstFailure(true);

        tables = parse("[setup][dummy Fixture]\n" + "[a][b]\n" + "[2][3]");

        FakeSpecification document = document();

        SetupInterpreter interpreter2 = new SetupInterpreter(new DefaultFixture(new TargetFixtureWithExceptionOnEnterRow()));
        interpreter2.interpret(document);

        assertTrue(document.stats().indicatesFailure());
        Assertions.assertAnnotatedException(tables.at(0, 2, 2));
    }

    private FakeSpecification document() {
        return new FakeSpecification(tables);
    }

    public class TargetFixtureWithExceptionOnEnterRow {
        public int a;
        public int b;

        public void enterRow() throws Exception {
            throw new Exception();
        }
    }

    public static class TargetFixtureNoEnterRow {
        public int a;
        public int b;
    }

    public static class TargetFixture {
        public int a;
        public int b;
        public int product = 0;

        public void enterRow() throws Exception {
            if (b < 0) {
                throw new Exception();
            }
            product += a * b;
        }
    }

    public static class AnnotatedTargetFixture {
        public int a;
        public int b;
        public int product = 0;

        @EnterRow
        public void doTheDew() throws Exception {
            if (b < 0) {
                throw new Exception();
            }
            product += a * b;
        }
    }
}
