package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.Tables.parse;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Assertions;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.interpreter.SkipInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.Tables;


public class LivingDocInterpreterSelectorTest {

    private InterpreterSelector selector;

    @Before
    public void setUp() {
        SystemUnderDevelopment sud = new DefaultSystemUnderDevelopment();
        sud.addImport("info.novatec.testit.livingdoc.document");
        LivingDoc.aliasInterpreter("MyInterpreter", MyInterpreter.class);
        selector = new LivingDocInterpreterSelector(sud);
    }

    @Test
    public void testReadsInterpreterClassNamesFromFirstCellOfRow() {
        Tables table = parse("[MyInterpreter]");
        MyInterpreter interpreter = ( MyInterpreter ) selector.selectInterpreter(table);

        assertNull(interpreter.getFixture());
    }

    @Test
    public void testSecondCellCanIndicateTheFixtureToUse() {
        Tables table = parse("[MyInterpreter][LivingDocInterpreterSelectorTest$MyFixture]");
        MyInterpreter interpreter = ( MyInterpreter ) selector.selectInterpreter(table);
        MyFixture myFixture = ( MyFixture ) interpreter.getFixture().getTarget();

        assertEquals(0, myFixture.parameterCount());
    }

    @Test
    public void testFixturesCanHaveParametersInEveryOtherCell() {
        Tables table = parse(
            "[MyInterpreter][LivingDocInterpreterSelectorTest$MyFixture][ignored][1st parameter][ignored][2nd parameter]");
        MyInterpreter interpreter = ( MyInterpreter ) selector.selectInterpreter(table);
        MyFixture myFixture = ( MyFixture ) interpreter.getFixture().getTarget();
        assertEquals(2, myFixture.parameterCount());
    }

    @Test
    public void testSkipsTableWhenInterpreterCannotBeFound() {
        Tables table = parse("[MissingInterpreter]");
        Interpreter interpreter = selector.selectInterpreter(table);
        assertThat(interpreter, is(instanceOf(SkipInterpreter.class)));
        Assertions.assertAnnotatedException(table.at(0, 0, 0));
    }

    @Test
    public void testFindInterpeterByAliasDefinedInPropertiesFile() {
        Tables table = parse("[DoIt]");
        Interpreter interpreter = selector.selectInterpreter(table);
        assertThat(interpreter, instanceOf(AliasInterpreter.class));
    }

    @Test
    public void testFindInterpeterByAliasDefinedInPropertyFileWithUmlauts() {
        // Prepare
        Tables tableUmlauts = parse("[Döit]");

        // Act
        Interpreter interpreterUmlauts = selector.selectInterpreter(tableUmlauts);

        // Check
        assertThat(interpreterUmlauts, instanceOf(AliasInterpreter.class));
    }

    @Test
    public void testFindInterpeterByAliasDefinedInPropertyFileWithWhitespaces() {
        // Prepare
        Tables tableUmlauts = parse("[Do it  now]");

        // Act
        Interpreter interpreterUmlauts = selector.selectInterpreter(tableUmlauts);

        // Check
        assertThat(interpreterUmlauts, instanceOf(AliasInterpreter.class));
    }

    @Test
    public void testFindInterpeterByAliasDefinedInPropertyFileWithSpecialChars() {
        // Prepare
        Tables tableUmlauts = parse("[<strong>Do %20 it!</strong>]");

        // Act
        Interpreter interpreterUmlauts = selector.selectInterpreter(tableUmlauts);

        // Check
        assertThat(interpreterUmlauts, instanceOf(AliasInterpreter.class));
    }

    @Test
    public void testFindMultipleInterpetersByAliasesDefinedInPropertyFile() {
        // Prepare
        Tables table = parse("[DoIt]");
        Tables tableUmlauts = parse("[Döit]");

        // Act
        Interpreter interpreter = selector.selectInterpreter(table);
        Interpreter interpreterUmlauts = selector.selectInterpreter(tableUmlauts);

        // Check
        assertThat(interpreter, instanceOf(AliasInterpreter.class));
        assertThat(interpreterUmlauts, instanceOf(AliasInterpreter.class));
    }

    public static class MyFixture {
        public String[] parameters;

        public MyFixture() {
            this(new String[0]);
        }

        public MyFixture(String first, String second) {
            this(new String[] { first, second });
        }

        public MyFixture(String... parameters) {
            this.parameters = parameters;
        }

        public int parameterCount() {
            return parameters.length;
        }

    }

    public static class MyInterpreter implements Interpreter {
        private Fixture fixture;

        public MyInterpreter(SystemUnderDevelopment sud) {
            // No implementation needed.
        }

        public MyInterpreter(Fixture fixture) {
            this.fixture = fixture;
        }

        public Fixture getFixture() {
            return fixture;
        }

        @Override
        public void interpret(Specification specification) {
            // No implementation needed.
        }
    }

    /**
     * Example interpreter to test the ability to load interpreter by defined
     * aliases in a aliases.properties file.
     */
    public static class AliasInterpreter implements Interpreter {

        public AliasInterpreter(SystemUnderDevelopment sud) {
            // No implementation needed.
        }

        @Override
        public void interpret(Specification specification) {
            // No implementation needed.
        }

    }

}
