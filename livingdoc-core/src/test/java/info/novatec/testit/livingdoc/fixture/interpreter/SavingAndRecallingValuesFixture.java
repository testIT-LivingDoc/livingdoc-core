package info.novatec.testit.livingdoc.fixture.interpreter;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.Variables;
import info.novatec.testit.livingdoc.fixture.util.AnnotationUtil;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.SavedColumn;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Cells;


@FixtureClass
public class SavingAndRecallingValuesFixture {
    public static Variables variables = new Variables();

    public String cellText;
    public String outputFromSystemUnderDevelopment;

    public Object annotation() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Cells cell = new Cells(cellText);

        MockFixture mockFixture = new MockFixture();
        mockFixture.willRespondTo("outputFromSystemUnderDevelopment", outputFromSystemUnderDevelopment);

        Column col = new SavedColumn(mockFixture.check("outputFromSystemUnderDevelopment"));
        col.bindTo(variables);
        col.doCell(cell);

        return AnnotationUtil.getAnnotationOnCell(cell);
    }

    public String inputToSystemUnderDevelopment() {
        return TypeConversion.toString(variables.getVariable(cellText));
    }

    @SuppressWarnings("unused")
    public void setCellDisplaysAs(String symbol) {
        // No implementation needed.
    }

    public String getCellDisplaysAs() {
        return outputFromSystemUnderDevelopment;
    }
}
