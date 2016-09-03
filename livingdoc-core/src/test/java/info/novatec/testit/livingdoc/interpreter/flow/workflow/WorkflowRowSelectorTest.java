package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Assertions;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.interpreter.flow.InterpretRow;
import info.novatec.testit.livingdoc.interpreter.flow.Row;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Rows;


public class WorkflowRowSelectorTest {
    private WorkflowRowSelector selector;

    @Before
    public void setUp() throws Exception {
        selector = new WorkflowRowSelector(new PlainOldFixture(new Object()));
    }

    @Test
    public void testInstantiatesRowIfFirstCellOfExampleContainsFullyQualifiedClassName() {
        Rows rows = Rows.parse("[" + GivenRow.class.getName() + "]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(GivenRow.class)));
    }

    @Test
    public void testThatRowInterfaceIsNotConsideredARow() {
        selector.addSuffix("Row");
        selector.searchPackage(packageName(Row.class));
        Rows rows = Rows.parse("[]"); // inferred row class is Row interface
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(DefaultRow.class)));
    }

    @Test
    public void testSearchesForRowClassInIncludedPackages() {
        Rows rows = Rows.parse("[WhenRow]");
        selector.searchPackage(packageName(AndNotThatRow.class));
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(WhenRow.class)));
    }

    @Test
    public void testThatIncludedSuffixesAreOptionalInRowClassNames() {
        Rows rows = Rows.parse("[ " + packageName(CheckRow.class) + ".Check]");
        selector.addSuffix("Row");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(CheckRow.class)));
    }

    @Test
    public void testThatDefaultRowPackageIsIncludedByDefault() {
        Rows rows = Rows.parse("[WhenRow]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(WhenRow.class)));
    }

    @Test
    public void testThatRowSuffixIsAddedByDefault() {
        Rows rows = Rows.parse("[Check]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(CheckRow.class)));
    }

    @Test
    public void testSupportsHumanizedClassNames() {
        Rows rows = Rows.parse("[given row]");
        selector.searchPackage(packageName(GivenRow.class));
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(GivenRow.class)));
    }

    @Test
    public void testRecognizesInterpreterNames() {
        Rows rows = Rows.parse("[decision table]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(InterpretRow.class)));
    }

    @Test
    public void testDefaultsToSelectingDefaultRow() {
        Rows rows = Rows.parse("[anything else]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(DefaultRow.class)));
    }

    @Test
    public void testShouldSkipWhenRowClassCannotBeInstantiated() {
        Rows rows = Rows.parse("[" + UninstantiableRow.class.getName() + "]");
        Row row = selector.select(rows);
        assertThat(row, is(instanceOf(SkipRow.class)));
        FakeSpecification spec = specification(rows);
        row.interpret(spec);
        Assertions.assertAnnotatedException(rows.firstChild());
        assertFalse(spec.hasMoreExamples());
    }

    private String packageName(Class< ? > aClass) {
        return aClass.getPackage().getName();
    }

    private FakeSpecification specification(Example example) {
        return new FakeSpecification(example);
    }

    public static class UninstantiableRow implements Row {

        private UninstantiableRow() {
        }

        @Override
        public void interpret(Specification row) {
            // No implementation needed.
        }
    }

}
