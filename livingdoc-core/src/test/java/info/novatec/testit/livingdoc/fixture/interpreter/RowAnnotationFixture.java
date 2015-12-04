package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.fixture.util.AnnotationUtil;
import info.novatec.testit.livingdoc.interpreter.SetupInterpreter;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.FakeExample;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass("Row Annotation")
public class RowAnnotationFixture {
    public Rows row;
    public String enterRow;
    public String rwie;

    public void prepareRWIE(Statistics stats) {
        rwie = stats.rightCount() + " " + stats.wrongCount() + " " + stats.ignoredCount() + " " + stats.exceptionCount();
    }

    public void enterRow(String val) {
        enterRow = val;
        SetupInterpreter setup = new SetupInterpreter(new PlainOldFixture(new MyFixture(this)));
        FakeExample table = new Tables();

        table.addChild(new Rows());
        table.addChild(new Rows(headers(row)));
        table.addChild(row);

        FakeSpecification fakeSpecification = new FakeSpecification(table);
        setup.interpret(fakeSpecification);

        prepareRWIE(fakeSpecification.stats());
    }

    private Object[] headers(Rows paramRow) {
        List<String> headerList = new ArrayList<String>();

        for (int i = 0; i < paramRow.firstChild().remainings(); i ++ ) {
            headerList.add("foo");
        }
        return headerList.toArray();
    }

    public Integer[] cellsMarked() {
        return AnnotationUtil.cellsMarked(row);
    }

    public String rowMarkedAs() {
        int cellToWatch = row.firstChild().remainings() - 1;
        return AnnotationUtil.getAnnotationOnCell(row.at(0, cellToWatch));
    }

    public static class MyFixture {
        private RowAnnotationFixture rowAnnotationFixture;

        public MyFixture(RowAnnotationFixture var) {
            rowAnnotationFixture = var;
        }

        public void enterRow() throws Exception {
            if ("failed".equals(rowAnnotationFixture.enterRow)) {
                throw new Exception("business error");
            }
        }

        public void foo(String val) throws Exception {
            if ("error".equals(val)) {
                throw new Exception();
            }
        }
    }
}
