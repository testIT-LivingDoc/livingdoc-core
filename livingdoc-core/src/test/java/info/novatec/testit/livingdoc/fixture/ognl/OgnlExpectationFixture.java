package info.novatec.testit.livingdoc.fixture.ognl;

import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import info.novatec.testit.livingdoc.ognl.OgnlExpectation;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class OgnlExpectationFixture {

    private OgnlExpectation expectation;
    private Object actual;
    private Object target;

    public OgnlExpectationFixture() {
        target = new FixtureForOgnlExpressions();
    }

    public void expected(String expected) {
        expectation = new OgnlExpectation(expected, target);
    }

    public String state() {
        return expectation.meets(actual) ? "right" : "wrong";
    }

    public void setActual(String strval) {
        actual = formatActual(strval);
    }

    private Object formatActual(String strval) {
        try {
            return Integer.parseInt(strval);
        } catch (NumberFormatException e) {
            // No implementation needed.
        }

        try {
            if (Pattern.matches("^.*(f|F)\\s*$", strval)) {
                return Float.parseFloat(strval);
            }
        } catch (NumberFormatException e) {
            // No implementation needed.
        }

        try {
            if (Pattern.matches("^.*(d|D)\\s*$", strval)) {
                return Double.parseDouble(strval);
            }
        } catch (NumberFormatException e) {
            // No implementation needed.
        }

        if (strval.equalsIgnoreCase("true")) {
            return new Boolean(true);
        }
        if (strval.equalsIgnoreCase("false")) {
            return new Boolean(false);
        }

        return strval;
    }

    private static class FixtureForOgnlExpressions {

        @SuppressWarnings("unused")
        public boolean isLeapYear(int year) {
            GregorianCalendar calendar = new GregorianCalendar();
            return calendar.isLeapYear(year);
        }

        @SuppressWarnings("unused")
        public int sum(int i1, int i2) {
            return i1 + i2;
        }

        @SuppressWarnings("unused")
        public String uppercase(String str) {
            return str.toUpperCase();
        }

    }

    public static void main(String[] args) {
        OgnlExpectation expectation = new OgnlExpectation(OgnlExpectation.formatExtractExpression("isLeapYear(2006)"),
            new FixtureForOgnlExpressions());
        System.out.println(expectation.meets(false));

    }
}
