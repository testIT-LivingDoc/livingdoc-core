package info.novatec.testit.livingdoc.samples.application.calculator;

public class ExpectedElementInList {
    private String expectedElement;

    public ExpectedElementInList(String element) {
        expectedElement = element;
    }

    public String getExpectedElement() {
        return expectedElement;
    }

    public static ExpectedElementInList parse(String val) {
        return new ExpectedElementInList(val);
    }

    public static String toString(ExpectedElementInList value) {
        return value.getExpectedElement();
    }
}
