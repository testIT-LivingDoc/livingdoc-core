package info.novatec.testit.livingdoc.util;

import info.novatec.testit.livingdoc.reflect.AfterRow;
import info.novatec.testit.livingdoc.reflect.BeforeFirstExpectation;
import info.novatec.testit.livingdoc.reflect.BeforeRow;


public class FixtureWithRowAndFirstExpectationAnnotation {
    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;

    @BeforeRow
    public void beforeRow() {
        c = c + 1;
        d = d + 1;
    }

    @BeforeFirstExpectation
    public void beforeFirstExpectation() {
        b = a;
    }

    @AfterRow
    public void afterRow() {
        d = 0;
    }

}
