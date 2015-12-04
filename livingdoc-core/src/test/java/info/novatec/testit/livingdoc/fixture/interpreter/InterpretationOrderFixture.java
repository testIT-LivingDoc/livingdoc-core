package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.interpreter.RuleForInterpreter;
import info.novatec.testit.livingdoc.interpreter.SetupInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass("Interpretationsreihenfolge")
public class InterpretationOrderFixture {
    public Tables table;

    public String[] orderOfInterpretation() throws Exception {
        FlowTracer tracer = new FlowTracer();
        Interpreter interpreter = interpreterFor(table, new PlainOldFixture(tracer));
        interpreter.interpret(new FakeSpecification(table));

        return tracer.trace.toArray(new String[tracer.trace.size()]);
    }

    private Interpreter interpreterFor(Tables paramTable, Fixture fixture) throws Exception {
        String interpreterCalled = paramTable.firstChild().firstChild().at(0).getContent();

        if ("setup".equals(interpreterCalled)) {
            return new SetupInterpreter(fixture);
        } else if ("rule for".equals(interpreterCalled)) {
            return new RuleForInterpreter(fixture);
        }
        // TODO find Exception
        throw new Exception("No interpreter found");
    }

    public static class FlowTracer {
        public final List<String> trace = new ArrayList<String>();

        public void header(String cellValue) {
            trace.add(cellValue);
        }
    }
}
