package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.ArrayList;
import java.util.Collection;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.fixture.util.AnnotationTable;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.FakeExample;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass
public class StatisticsAndAnnotationsFixture {
    public Tables table;
    public String[] headers;
    public String rwie;
    public ArrayList<Value> valueList;

    public int a;
    public int b;

    private Interpreter interpreter;

    public StatisticsAndAnnotationsFixture(String interpreterName) throws Throwable {
        interpreter = LivingDoc.getInterpreter(interpreterName, new Object[] { new PlainOldFixture(this) });
    }

    public void setValues(String[] values) {
        valueList = new ArrayList<Value>();

        for (String value : values) {
            valueList.add(new Value(value));
        }
    }

    public Collection<Value> query() {
        return valueList;
    }

    public int division() {
        return a / b;
    }

    public void prepareRWIE(Statistics stats) {
        rwie = stats.rightCount() + " " + stats.wrongCount() + " " + stats.ignoredCount() + " " + stats.exceptionCount();
    }

    public AnnotationTable annotations() {
        FakeExample example = new Tables();

        example.addChild(new Rows());
        example.addChild(( Rows ) table.at(0, 0));

        FakeSpecification specification = new FakeSpecification(example);
        interpreter.interpret(specification);

        prepareRWIE(specification.stats());

        return new AnnotationTable(table);
    }

    public static class Value {
        public String value;
        private String other;

        public Value(String value) {
            String[] subvalues = value.split(":");

            this.value = subvalues[0];
            this.other = ( subvalues.length > 1 ) ? subvalues[1] : "";
        }

        public int getOther() {
            return Integer.parseInt(other);
        }
    }

}
