package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class NumberUtilTest {

    @Test
    public void testCanSubstituteCommaToPeriod() {
        assertEquals("1.1", NumberUtil.substituteDecimalSeparatorToPeriod("1,1"));
    }
}
