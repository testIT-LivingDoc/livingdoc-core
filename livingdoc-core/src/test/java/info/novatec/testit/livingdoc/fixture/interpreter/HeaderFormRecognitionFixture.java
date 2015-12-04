package info.novatec.testit.livingdoc.fixture.interpreter;

import info.novatec.testit.livingdoc.interpreter.HeaderForm;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.interpreter.column.GivenColumn;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;
import info.novatec.testit.livingdoc.interpreter.column.RecalledColumn;
import info.novatec.testit.livingdoc.interpreter.column.SavedColumn;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class HeaderFormRecognitionFixture {
    private String headerText;

    public String cellsWillBe() throws Exception {
        MockFixture mockFixture = new MockFixture();
        mockFixture.willRespondTo(headerText, null);

        Column col = HeaderForm.parse(headerText).selectColumn(mockFixture);
        if (col instanceof GivenColumn) {
            return "given";
        } else if (col instanceof ExpectedColumn) {
            return "expected";
        } else if (col instanceof NullColumn) {
            return "ignore";
        } else if (col instanceof SavedColumn) {
            return "saved";
        } else if (col instanceof RecalledColumn) {
            return "recalled";
        } else {
            throw new Exception();
        }
    }

    public void setHeaderText(String text) {
        if (text != null) {
            headerText = text;
        } else {
            headerText = "";
        }
    }
}
