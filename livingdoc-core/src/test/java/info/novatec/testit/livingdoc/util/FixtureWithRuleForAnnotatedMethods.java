package info.novatec.testit.livingdoc.util;

import info.novatec.testit.livingdoc.reflect.AfterRow;
import info.novatec.testit.livingdoc.reflect.AfterTable;
import info.novatec.testit.livingdoc.reflect.BeforeFirstExpectation;
import info.novatec.testit.livingdoc.reflect.BeforeRow;
import info.novatec.testit.livingdoc.reflect.BeforeTable;


public class FixtureWithRuleForAnnotatedMethods {
    public int a = 0;
    public int bt = 0;
    public int br = 0;
    public int bfe = 0;
    public int ar = 0;
    public int at = 0;

    @BeforeTable
    public void beforeTable() {
        bt = a;
    }

    @BeforeRow
    public void beforeRow() {
        br = bt + a;
    }

    @BeforeFirstExpectation
    public void beforeFirstExpectation() {
        bfe = br + a;
    }

    @AfterRow
    public void afterRow() {
        ar = bfe + a;
    }

    @AfterTable
    public void afterTable() {
        at = ar + a;
    }

    @Override
    public String toString() {
        return "[a=" + a + "][bt=" + bt + "][br=" + br + "][bfe=" + bfe + "][ar=" + ar + "][at=" + at + "]";
    }

}
