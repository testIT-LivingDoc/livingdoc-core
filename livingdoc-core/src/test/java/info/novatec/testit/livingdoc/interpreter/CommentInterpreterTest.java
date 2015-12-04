package info.novatec.testit.livingdoc.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.AlternateCalculator;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.util.Tables;


public class CommentInterpreterTest {
    private CommentInterpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new CommentInterpreter();
    }

    @Test
    public void testSkipsTable() {
        Tables tables = Tables.parse("[" + CommentInterpreter.class.getName() + "][" + AlternateCalculator.class.getName()
            + "]\n" + "[a][b][sum?]\n" + "[6][2][8]\n" + "[5][2][8]\n");
        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        assertEquals(1, spec.stats.ignoredCount());
    }
}
