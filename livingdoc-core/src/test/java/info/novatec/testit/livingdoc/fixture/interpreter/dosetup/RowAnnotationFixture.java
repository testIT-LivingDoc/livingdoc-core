package info.novatec.testit.livingdoc.fixture.interpreter.dosetup;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.fixture.util.AnnotationUtil;
import info.novatec.testit.livingdoc.interpreter.DoSetupInterpreter;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.FakeExample;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass("DoSetup Row Annotation")
public class RowAnnotationFixture {
    public Rows row;
    public String enterRow;
    public String rwie;

    public void prepareRWIE(Statistics stats) {
        rwie = stats.rightCount() + " " + stats.wrongCount() + " " + stats.ignoredCount() + " " + stats.exceptionCount();
    }

    public void enterRow(String val) {
        enterRow = val;
        DoSetupInterpreter setup = new DoSetupInterpreter(new PlainOldFixture(new MyFixture(enterRow)));
        FakeExample table = new Tables();

        table.addChild(new Rows());
        table.addChild(row);

        FakeSpecification fakeSpecification = new FakeSpecification(table);
        setup.interpret(fakeSpecification);

        prepareRWIE(fakeSpecification.stats());
    }

    public Integer[] cellsMarked() {
        return AnnotationUtil.cellsMarked(row);
    }

    public String rowMarkedAs() {
        int cellToWatch = row.firstChild().remainings() - 1;
        return AnnotationUtil.getAnnotationOnCell(row.at(0, cellToWatch));
    }

    public static class MyFixture {
        private String enterRow;

        public MyFixture(String enterRow) {
            this.enterRow = enterRow;
        }

        public boolean rowOfTable(int index1, int index2) throws Exception {
            if ("exception".equals(enterRow)) {
                throw new Exception();
            }
            return ! "failed".equals(enterRow);
        }

        public boolean rowOfTableWith(int index1, int index2, int index3) throws Exception {
            if ("exception".equals(enterRow)) {
                throw new Exception();
            }
            return ! "failed".equals(enterRow);
        }

        public void existingMethodWithMissingParameter(int p1) {
            // No implementation needed.
        }
    }
}
