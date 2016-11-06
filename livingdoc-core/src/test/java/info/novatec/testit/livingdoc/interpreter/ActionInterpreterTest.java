package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedException;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedRight;
import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedWrong;
import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


@Deprecated
@RunWith(MockitoJUnitRunner.class)
public class ActionInterpreterTest {

    private ActionInterpreter interpreter;
    @Mock
    private Target fixture;

    @Before
    public void setUp() {
        interpreter = new ActionInterpreter(new PlainOldFixture(fixture));
    }

    @Test
    public void testThatDefinesASequenceOfActions() {
        interpreter.interpret(specificationFor("[action][mock]\n" + "[performAction]\n" + "[performAction]"));

        verify(fixture, times(2)).performAction();
    }

    @Test
    public void testCanAddOneArgumentToAnAction() {
        interpreter.interpret(specificationFor("[action][mock]\n" + "[performActionWithArgument][argument1]"));

        verify(fixture).performActionWithArgument("argument1");
    }

    @Test
    public void testCanAddMultipleArgumentsToAnAction() {
        interpreter.interpret(specificationFor("[action][mock]\n" + "[performActionWithArguments][argument1][argument2]"));

        verify(fixture).performActionWithArguments("argument1", "argument2");
    }

    @Test
    public void testThatNoAnnotationIsAppliedIfMethodExecutesNormallyAndDoesNotReturnABoolean() {
        Tables tables = tablesFor("[action][mock]\n" + "[performAction]");
        interpreter.interpret(specificationFor(tables));

        verify(fixture).performAction();
        assertNotAnnotated(tables.at(0, 1, 0));
    }

    @Test
    public void testThatFirstCellIsAnnotatedRightIfMethodExecutesNormallyAndReturnsTrue() {
        doReturn(true).when(fixture).performCheck("argument1");

        Tables tables = tablesFor("[action][mock]\n" + "[performCheck][argument1]");
        interpreter.interpret(specificationFor(tables));

        verify(fixture).performCheck("argument1");
        assertAnnotatedRight(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
    }

    @Test
    public void testThatFirstCellIsAnnotatedWrongIfMethodExecutesNormallyAndReturnsFalse() {
        Tables tables = tablesFor("[action][mock]\n" + "[performCheck][argument1]");
        interpreter.interpret(specificationFor(tables));

        verify(fixture).performCheck("argument1");
        assertAnnotatedWrong(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
    }

    @Test
    public void testThatFirstCellIsAnnotatedExceptionIfMethodThrowsAnException() {
        doThrow(IllegalArgumentException.class).when(fixture).performCheck("argument1");

        Tables tables = tablesFor("[action][mock]\n" + "[performCheck][argument1]");
        interpreter.interpret(specificationFor(tables));

        verify(fixture).performCheck("argument1");
        assertAnnotatedException(tables.at(0, 1, 0));
        assertNotAnnotated(tables.at(0, 1, 1));
    }

    @Test
    public void testThatFirstCellIsAnnotatedWithAnExceptionIfMethodSpecifiedIsNotFound() {
        Tables tables = tablesFor("[action][mock]\n" + "[unknownMethod]");
        interpreter.interpret(specificationFor(tables));

        assertAnnotatedException(tables.at(0, 1, 0));
    }

    private Specification specificationFor(String examples) {
        return specificationFor(tablesFor(examples));
    }

    private Specification specificationFor(Tables tables) {
        return new FakeSpecification(tables);
    }

    private Tables tablesFor(String examples) {
        return Tables.parse(examples);
    }

    private static interface Target {
        public void performAction();

        public void performActionWithArgument(String arg);

        public void performActionWithArguments(String arg1, String arg2);

        public boolean performCheck(String arg);
    }

}
