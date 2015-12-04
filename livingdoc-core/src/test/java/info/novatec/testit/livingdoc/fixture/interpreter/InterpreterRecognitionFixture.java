package info.novatec.testit.livingdoc.fixture.interpreter;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.document.InterpreterSelector;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.interpreter.SkipInterpreter;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.FakeExample;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass
public class InterpreterRecognitionFixture {
    public String interpreterCalled;

    public Rows interpreterRow;

    private Interpreter interpreter;

    private InterpreterSelector interpreterSelector;

    public InterpreterRecognitionFixture() {
        SystemUnderDevelopment sud = new DefaultSystemUnderDevelopment();
        sud.addImport("info.novatec.testit.livingdoc.interpreter");
        sud.addImport("info.novatec.testit.livingdoc.fixture.interpreter");
        interpreterSelector = new LivingDocInterpreterSelector(sud);
    }

    public String instanceOf() {
        interpreter = interpreterSelector.selectInterpreter(createFakeExampleForInterpreterRow(interpreterRow));
        return interpreter.getClass().getSimpleName();
    }

    public boolean isAnInterpreter() {
        return ! ( interpreter instanceof SkipInterpreter );
    }

    private FakeExample createFakeExampleForInterpreterRow(Rows theInterpreterRow) {
        FakeExample table = new Tables();

        table.addChild(theInterpreterRow);
        table.addChild(new Rows());
        table.addChild(new Rows());

        return table;
    }
}
