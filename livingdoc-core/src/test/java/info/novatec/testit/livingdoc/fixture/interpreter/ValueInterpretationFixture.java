package info.novatec.testit.livingdoc.fixture.interpreter;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.fixture.util.AnnotationUtil;
import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.interpreter.column.GivenColumn;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Cells;


@FixtureClass
public class ValueInterpretationFixture {
    private String cellText;
    public String recognizedAs;
    public String resultingValue;
    public boolean ruleRespected;
    public String inputCellRecognizedAs;

    public String comparisonWillBe() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Cells cell = new Cells(cellText);

        MockFixture mockFixture = new MockFixture();
        mockFixture.willRespondTo("foo", "anything");

        Column col = new ExpectedColumn(mockFixture.send("foo"));
        col.doCell(cell);

        return AnnotationUtil.getAnnotationOnCell(cell);
    }

    public String inputWillBe() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Fixture fixture = new PlainOldFixture(new MyFixture(this));
        Column column = new GivenColumn(fixture.send("foo"));
        column.doCell(new Cells(cellText));

        return inputCellRecognizedAs;
    }

    public static class MyFixture {
        ValueInterpretationFixture valueInterpretationFixture;

        public MyFixture(ValueInterpretationFixture var) {
            valueInterpretationFixture = var;
        }

        public void foo(String val) {
            valueInterpretationFixture.inputCellRecognizedAs = val;
        }
    }

    public void setCellText(String text) {
        if (text == null) {
            cellText = "";
        } else {
            cellText = text;
        }
    }

    public String getCellText() {
        return cellText;
    }

    public void setRecognizedAs(String recognizedAs) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        Fixture fixture;
        Column column = new NullColumn();
        ruleRespected = false;
        resultingValue = "";

        fixture = fixtureFor(recognizedAs);
        column = new GivenColumn(fixture.send("foo"));
        column.doCell(new Cells(cellText));
    }

    private Fixture fixtureFor(String paramRecognizedAs) {
        if ("a number".equals(paramRecognizedAs)) {
            return new PlainOldFixture(new MyFixtureFloat(this));
        }

        if ("a string".equals(paramRecognizedAs)) {
            return new PlainOldFixture(new MyFixtureString(this));
        }

        if ("a boolean".equals(paramRecognizedAs)) {
            return new PlainOldFixture(new MyFixtureBoolean(this));
        }
        return null;
    }

    public static class MyFixtureFloat {
        ValueInterpretationFixture interpretationFixture;

        public MyFixtureFloat(ValueInterpretationFixture val) {
            interpretationFixture = val;
        }

        public void foo(Float var) {
            interpretationFixture.ruleRespected = true;
            interpretationFixture.resultingValue = var.toString();
        }
    }

    public static class MyFixtureString {
        ValueInterpretationFixture interpretationFixture;

        public MyFixtureString(ValueInterpretationFixture val) {
            interpretationFixture = val;
        }

        public void foo(String var) {
            interpretationFixture.ruleRespected = true;
            interpretationFixture.resultingValue = var;
        }
    }

    public static class MyFixtureBoolean {
        ValueInterpretationFixture interpretationFixture;

        public MyFixtureBoolean(ValueInterpretationFixture val) {
            interpretationFixture = val;
        }

        public void foo(Boolean var) {
            interpretationFixture.ruleRespected = true;
            interpretationFixture.resultingValue = var.toString();
        }
    }

}
