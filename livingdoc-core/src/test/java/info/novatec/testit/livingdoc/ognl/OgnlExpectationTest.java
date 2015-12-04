/* Copyright (c) 2006 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.ognl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class OgnlExpectationTest {

    @Test
    public void testThatMatcheeIsAConditionalExpressionWhenItContainsQueryPlaceholders() {
        assertFalse(OgnlExpectation.conditionalExpression("anyvalue"));
        assertTrue(OgnlExpectation.conditionalExpression("?"));
        assertTrue(OgnlExpectation.conditionalExpression("? > 0"));
        assertTrue(OgnlExpectation.conditionalExpression("method(?)"));
        assertTrue(OgnlExpectation.conditionalExpression("?\n"));
        assertFalse(OgnlExpectation.conditionalExpression("\"StringWith?\""));
        assertTrue(OgnlExpectation.conditionalExpression("\"StringWith?\"?"));
        assertTrue(OgnlExpectation.conditionalExpression("?\"StringWith?\""));
        assertFalse(OgnlExpectation.conditionalExpression("\"StringWith?\" \"StringWith?\""));
        assertTrue(OgnlExpectation.conditionalExpression("\"StringWith?\" ? \"StringWith?\""));
    }

    @Test
    public void testThatMatcheeIsAnExtractExpressionWhenEnclosedInParenthesis() {
        assertFalse(OgnlExpectation.extractExpression("text"));
        assertFalse(OgnlExpectation.extractExpression("text="));
        assertFalse(OgnlExpectation.extractExpression("te=xt"));
        assertTrue(OgnlExpectation.extractExpression("=text"));
        assertTrue(OgnlExpectation.extractExpression(" = text"));
        assertTrue(OgnlExpectation.extractExpression("  =  text  "));
    }

    @Test
    public void testExtractExpressionConsistingOfAStringConstant() {
        // Must enclosed string in double quotes in matchee.
        String expr = "right";
        String matchee = OgnlExpectation.formatExtractExpression(String.format("\"%s\"", expr));
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets("wrong"));
        assertTrue(expectation.meets(expr));
    }

    @Test
    public void testExtractExpressionConsistingOfAnIntegerConstant() {
        String expr = "1";
        String matchee = OgnlExpectation.formatExtractExpression(expr);
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(new Integer(2)));
        assertTrue(expectation.meets(new Integer(expr)));
    }

    @Test
    public void testExtractExpressionConsistingOfALongConstant() {
        String expr = "1";
        String matchee = OgnlExpectation.formatExtractExpression(expr);
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(new Long(2)));
        assertTrue(expectation.meets(new Long(expr)));
    }

    @Test
    public void testExtractExpressionConsistingOfARealConstant() {
        // Must specify a float constant as ognl convert constants with no float
        // type
        // suffix are convert to Double.
        String expr = "1.1e-1f";
        String matchee = OgnlExpectation.formatExtractExpression(expr);
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(new Float(0.1)));
        assertTrue(expectation.meets(new Float(expr)));
    }

    @Test
    public void testExtractExpressionConsistingOfADoubleConstant() {
        String expr = "1.1e-1";
        String matchee = OgnlExpectation.formatExtractExpression(expr);
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(new Double(0.1)));
        assertTrue(expectation.meets(new Double(expr)));
    }

    @Test
    public void testExtractExpressionConsistingOfABooleanConstant() {
        String expr = "true";
        String matchee = OgnlExpectation.formatExtractExpression(expr);
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(Boolean.FALSE));
        assertTrue(expectation.meets(Boolean.TRUE));
    }

    @Test
    public void testConditionalExpressionWithASimpleComparisonExpression() {
        String matchee = "? > 0";
        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertFalse(expectation.meets(new Integer(0)));
        assertTrue(expectation.meets(new Integer(1)));
    }

    @Test
    public void testConditionalExpressionThatContainsStringsEnclosedInDoubleQuotesWithPlaceholdersInIt() {
        String stringWithPlaceholder = "StringWith?";
        String matchee = "? lt \"" + stringWithPlaceholder + "\"" + " or ? gt \"" + stringWithPlaceholder + "\""
            + " or ? eq \"" + stringWithPlaceholder + "\"";

        OgnlExpectation expectation = new OgnlExpectation(matchee);

        assertTrue(expectation.meets(stringWithPlaceholder));
    }
}
