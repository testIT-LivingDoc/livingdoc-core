package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedException;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedIgnored;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedRight;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedStopped;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedWrongWithDetails;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedWrongWithoutDetail;
import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.RightAnnotation;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.Tables;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowInterpreterTest {

    private Tables tables;
    private WorkflowInterpreter interpreter;
    @Mock
    private Target fixture;
    private static boolean stopOnFirstFailure;

    @BeforeClass
    public static void beforeClass() throws Exception {
        stopOnFirstFailure = LivingDoc.isStopOnFirstFailure();
    }

    @Before
    public void setUp() throws Exception {
        interpreter = new WorkflowInterpreter(code());
        LivingDoc.setStopOnFirstFailure(false);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        LivingDoc.setStopOnFirstFailure(stopOnFirstFailure);
    }

    @Test
    public void testATableDefinesASequenceOfActions() throws Exception {
        doReturn(true).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[workflow][mock]\n" + "[commandTakingNoParameter]\n" + "[commandTakingNoParameter]");
        interpreter.interpret(document());

        verify(fixture, times(2)).commandTakingNoParameter();
    }

    @Test
    public void testActionsAcceptParametersInEveryOtherCell() throws Exception {
        tables = Tables.parse("[workflow][mock]\n"
            + "[command] [a parameter] [expecting] [a second parameter] [parameters] [a third parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
    }

    @Test
    public void testKeywordsContainingSpacesAreCamelized() throws Exception {
        tables = Tables.parse("[workflow][mock]\n" + "[command expecting a single parameter][parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingASingleParameter("parameter");
    }

    @Test
    public void testDefaultActionsAreAnnotatedRightIfTheyReturnTrue() throws Exception {
        doReturn(true).when(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");

        tables = Tables.parse("[workflow][mock]\n"
            + "[command][a parameter][expecting][a second parameter][parameters][a third parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
        assertAnnotatedRight(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedRight(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 1, 3));
        assertAnnotatedRight(tables.at(0, 1, 4));
        assertNotAnnotated(tables.at(0, 1, 5));
    }

    @Test
    public void testDefaultActionsAreAnnotatedWrongIfTheyReturnFalse() throws Exception {
        doReturn(false).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[workflow][mock]\n" + "[commandTakingNoParameter]");
        interpreter.interpret(document());

        verify(fixture).commandTakingNoParameter();
        assertAnnotatedWrongWithoutDetail(tables.at(0, 1, 0));
    }

    @Test
    public void testDefaultActionsColorFirstKeywordOnException() throws Exception {
        doThrow(new RuntimeException("Command failed")).when(fixture).commandExpectingParameters("a parameter",
            "a second parameter", "a third parameter");

        tables = Tables.parse("[workflow][mock]\n"
            + "[command][a parameter][expecting][a second parameter][parameters][a third parameter]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
        assertAnnotatedException(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertNotAnnotated(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 1, 3));
        assertNotAnnotated(tables.at(0, 1, 4));
        assertNotAnnotated(tables.at(0, 1, 5));
    }

    @Test
    public void testActionsThatReturnAnotherValueOrVoidShouldNotBeAnnotated() throws Exception {
        doReturn(Object.class).when(fixture).commandExpectingParameters("a parameter", "a second parameter",
            "a third parameter");

        tables = Tables.parse("[workflow][mock]\n"
            + "[command][a parameter][expecting][a second parameter][parameters][a third parameter]\n"
            + "[commandReturningNothing]");
        interpreter.interpret(document());

        verify(fixture).commandExpectingParameters("a parameter", "a second parameter", "a third parameter");
        verify(fixture).commandReturningNothing();
        assertNotAnnotated(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertNotAnnotated(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 1, 3));
        assertNotAnnotated(tables.at(0, 1, 4));
        assertNotAnnotated(tables.at(0, 1, 5));
        assertNotAnnotated(tables.at(0, 2, 0));
    }

    @Test
    public void testActionsCanReturnAFixtureToInterpretRestOfTable() throws Exception {
        doReturn(Object.class).when(fixture).commandTakingNoParameter();

        tables = Tables.parse("[workflow][mock]\n" + "[" + RightInterpreter.class.getName() + "][commandTakingNoParameter]\n"
            + "[actionOnReturnedFixture]\n" + "[actionOnReturnedFixture]");
        interpreter.interpret(document());

        verify(fixture).commandTakingNoParameter();
        Example rows = tables.at(0, 1);
        assertAnnotatedRight(rows.at(1, 0));
        assertAnnotatedRight(rows.at(2, 0));
    }

    @Test
    public void testCheckSpecialActionExpectsValueOfLastCellAndColorsLastCell() throws Exception {
        when(fixture.thatValueOfIs("balance")).thenReturn("100", "150");

        tables = Tables.parse("[workflow][mock]\n" + "[check][that value of][balance][is][100]\n"
            + "[check][that value of][balance][is][120]");
        interpreter.interpret(document());

        verify(fixture, times(2)).thatValueOfIs("balance");
        assertAnnotatedRight(tables.at(0, 1, 4));
        assertAnnotatedWrongWithDetails(tables.at(0, 2, 4));
    }

    @Test
    public void testCheckSpecialActionColorsFirstKeywordCellOnException() throws Exception {
        doThrow(new Exception("Command failed")).when(fixture).commandThrowingException();

        tables = Tables.parse("[workflow][mock]\n" + "[check][commandThrowingException][value]");
        interpreter.interpret(document());

        verify(fixture).commandThrowingException();
        assertAnnotatedException(tables.at(0, 1, 1));
        assertNotAnnotated(tables.at(0, 1, 2));
    }

    @Test
    public void testRejectSpecialActionExpectsFailureAndColorTheKeyword() throws Exception {
        when(fixture.commandExpectingASingleParameter("parameter")).thenReturn(true, false, new RuntimeException());

        tables = Tables.parse("[workflow][mock]\n" + "[and not that][commandExpectingASingleParameter][parameter]\n"
            + "[and not that][commandExpectingASingleParameter][parameter]\n"
            + "[and not that][commandExpectingASingleParameter][parameter]");
        interpreter.interpret(document());

        verify(fixture, times(3)).commandExpectingASingleParameter("parameter");
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedWrongWithoutDetail(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 2, 1));
        assertAnnotatedRight(tables.at(0, 2, 0));
        assertNotAnnotated(tables.at(0, 3, 1));
        assertAnnotatedRight(tables.at(0, 3, 0));
    }

    @Test
    public void testSpecialActionsCanHaveAnyCase() throws Exception {
        when(fixture.thatValueOfIs("balance")).thenReturn("1", "2");
        when(fixture.commandExpectingASingleParameter("parameter")).thenReturn(true, false);

        tables = Tables.parse("[workflow][mock]\n" + "[CHECK][that value of][balance][is][1]\n"
            + "[Check][that value of][balance][is][3]\n" + "[aNd nOt That][commandExpectingASingleParameter][parameter]\n"
            + "[And not that][commandExpectingASingleParameter][parameter]");
        interpreter.interpret(document());

        verify(fixture, times(2)).thatValueOfIs("balance");
        verify(fixture, times(2)).commandExpectingASingleParameter("parameter");
        assertAnnotatedRight(tables.at(0, 1, 4));
        assertAnnotatedWrongWithDetails(tables.at(0, 2, 4));
        assertAnnotatedWrongWithoutDetail(tables.at(0, 3, 0));
        assertAnnotatedRight(tables.at(0, 4, 0));
    }

    @Test
    public void testWillConsumeAllTablesInDocumentAndCompileStatistics() {
        doReturn(true).when(fixture).openAccount("123456");
        when(fixture.thatBalanceOfAccountIs("123456")).thenReturn("0.00", "100.00");
        doReturn(true).when(fixture).depositInAccount("100.00", "123456");

        tables = Tables.parse("[workflow][mock]\n" + "****\n" + "[open account][123456]\n" + "****\n"
            + "[check][that balance of account][123456][is][0.00]\n" + "****\n" + "[deposit][100.00][in account][123456]\n"
            + "****\n" + "[check][that balance of account][123456][is][100.00]");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).openAccount("123456");
        verify(fixture, times(2)).thatBalanceOfAccountIs("123456");
        verify(fixture).depositInAccount("100.00", "123456");
        assertAnnotatedRight(tables.at(1, 0, 0));
        assertAnnotatedRight(tables.at(2, 0, 4));
        assertAnnotatedRight(tables.at(3, 0, 0));
        assertAnnotatedRight(tables.at(4, 0, 4));
        assertFalse(specification.hasMoreExamples());
        assertEquals(new Statistics(4, 0, 0, 0), specification.stats);
    }

    @Test
    public void testEndKeywordStopsFlow() {
        doReturn(true).when(fixture).openAccount("123456");
        doReturn("0.00").when(fixture).thatBalanceOfAccountIs("123456");

        tables = Tables.parse("[workflow][mock]\n" + "****\n" + "[open account][123456]\n" + "****\n"
            + "[check][that balance of account][123456][is][0.00]\n" + "****\n" + "[end]\n" + "*****"
            + "[deposit][100.00][in account][654321]\n" + "****\n" + "[check][that balance of account][654321][is][100.00]");
        Specification specification = document();
        interpreter.interpret(specification);

        InOrder inOrder = inOrder(fixture);
        inOrder.verify(fixture).openAccount("123456");
        inOrder.verify(fixture).thatBalanceOfAccountIs("123456");
        inOrder.verify(fixture, never()).depositInAccount("100.00", "654321");
        inOrder.verify(fixture, never()).thatBalanceOfAccountIs("654321");
        assertTrue(specification.hasMoreExamples());
        Example next = specification.nextExample();
        assertEquals("deposit", ExampleUtil.contentOf(next.at(0, 0, 0)));
    }

    @Test
    public void testDisplayKeywordFlow() {
        doReturn("0.00").when(fixture).theBalanceOfAccount("123456");
        doReturn("5.45").when(fixture).actualMortgageRate();

        tables = Tables.parse("[workflow][mock]\n" + "****\n" + "[display][the balance of account][123456]\n" + "****\n"
            + "[display][actual mortgage rate]");
        Specification specification = document();
        interpreter.interpret(specification);

        verify(fixture).theBalanceOfAccount("123456");
        verify(fixture).actualMortgageRate();
        assertAnnotatedIgnored(tables.at(1, 0, 3));
        assertAnnotatedIgnored(tables.at(2, 0, 2));
    }

    @Test
    public void testThatThrownedExceptionWithOptionsStopOnFailureShouldAnnotedTheRowStopped() {
        LivingDoc.setStopOnFirstFailure(true);

        doThrow(RuntimeException.class).when(fixture).commandExpectingASingleParameter("parameter");

        tables = Tables.parse("[workflow][mock]\n" + "[command expecting a single parameter][parameter]\n"
            + "[command expecting a single parameter][parameter]");
        FakeSpecification specification = document();
        interpreter.interpret(specification);

        verify(fixture).commandExpectingASingleParameter("parameter");
        assertTrue(specification.stats().hasFailed());
        assertAnnotatedException(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
        assertAnnotatedStopped(tables.at(0, 1, 2));
        assertNotAnnotated(tables.at(0, 2, 0));
        assertNotAnnotated(tables.at(0, 2, 1));
    }

    private Fixture code() {
        return new PlainOldFixture(fixture);
    }

    private FakeSpecification document() {
        return new FakeSpecification(tables);
    }

    public static interface Target {
        void commandReturningNothing();

        boolean commandThrowingException() throws Exception;

        Object commandTakingNoParameter();

        Object thatValueOfIs(String first);

        String thatBalanceOfAccountIs(String accountNumber);

        boolean openAccount(String accountNumber);

        boolean depositInAccount(String amount, String accountNumber);

        Object commandExpectingParameters(String first, String second, String third);

        Object commandExpectingASingleParameter(String p);

        Object theBalanceOfAccount(String accountNumber);

        Object actualMortgageRate();
    }

    public static class RightInterpreter implements Interpreter {
        public RightInterpreter(Fixture fixture) {
            // No implementation needed.
        }

        @Override
        public void interpret(Specification specification) {
            execute(specification.nextExample().firstChild());
        }

        public void execute(Example rows) {
            if ( ! rows.hasChild()) {
                return;
            }
            for (Example row : rows) {
                row.firstChild().annotate(new RightAnnotation());
            }
        }
    }
}
