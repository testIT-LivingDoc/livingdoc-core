package info.novatec.testit.livingdoc.fixture.interpreter;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.fixture.util.AnnotationUtil;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Cells;


@FixtureClass("Cell Annotation")
public class CellAnnotationFixture {
    public String comparisonValue;
    public String returnedValue;

    public Object annotation() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Cells cell = new Cells(comparisonValue);

        MockFixture mockFixture = new MockFixture();
        mockFixture.willRespondTo("foo", returnedValue);

        Column col = new ExpectedColumn(mockFixture.send("foo"));
        col.doCell(cell);

        return AnnotationUtil.getAnnotationOnCell(cell);
    }

    public void setComparisonValue(String text) {
        comparisonValue = text == null ? "" : text;
    }

    public static class MyFixture {
        private CellAnnotationFixture cellAnnotationFixture;

        public MyFixture(CellAnnotationFixture var) {
            cellAnnotationFixture = var;
        }

        public String foo(String valueToCompare) throws Exception {
            if ("error".equals(cellAnnotationFixture.returnedValue)) {
                throw new Exception();
            }
            return cellAnnotationFixture.returnedValue;
        }
    }
}
