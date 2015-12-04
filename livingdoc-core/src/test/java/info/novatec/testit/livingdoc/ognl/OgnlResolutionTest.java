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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import info.novatec.testit.livingdoc.util.NameUtils;


public class OgnlResolutionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testThatWhenNoExpressionIsSpecifiedItComplain() {
        OgnlResolution resolution = new OgnlResolution(" ");
        resolution.expressionsListToResolve();
    }

    @Test
    public void testThatOneTokenCodedAloneReturnThatToken() {
        String expression = "alone";
        OgnlResolution resolution = new OgnlResolution(expression);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(expression));
    }

    @Test
    public void testExpressionsGeneratedFromTwoTokens() {
        String token1 = "token1";
        String token2 = "token2";
        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(3, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2)));
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    @Test
    public void testExpressionsGeneratedFromThreeTokens() {
        String token1 = "token1";
        String token2 = "token2";
        String token3 = "token3";
        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2 + " " + token3);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(5, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2 + "." + token3));
        assertTrue(expressions.contains(token1 + "." + NameUtils.toLowerCamelCase(token2 + " " + token3)));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2) + "." + token3));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2 + " " + token3)));
        assertTrue(expressions.contains(token1 + " " + token2 + " " + token3));
    }

    @Test
    public void testExpressionsGeneratedFromTokensWithSpaces() {
        String token1 = "token1";
        String token2 = "token2";
        OgnlResolution resolution = new OgnlResolution("  " + token1 + "  " + token2 + "  ");
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(3, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(NameUtils.toLowerCamelCase(token1 + " " + token2)));
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    @Test
    public void testFirstTokenIsNotStartingAsAJavaIdentifier() {
        String token1 = "#token1";
        String token2 = "token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(2, expressions.size());
        assertTrue(expressions.contains(token1 + "." + token2));
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    @Test
    public void testTwoTokensWithSecondOneNotStartingAsAJavaIdentifier() {
        String token1 = "token1";
        String token2 = "#token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    @Test
    public void testTwoTokensNotStartingAsAJavaIdentifier() {
        String token1 = "#token1";
        String token2 = "#token2";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2));
    }

    @Test
    public void testThreeTokensWithOneNotIdentifier() {
        for (int i = 0; i < 3; i ++ ) {
            String expression = "";
            for (int x = 0; x < 3; x ++ ) {
                if (x == i) {
                    expression += "#";
                }
                expression += "token ";
            }

            OgnlResolution resolution = new OgnlResolution(expression);
            Collection<String> expressions = resolution.expressionsListToResolve();

            int size = 0;

            switch (i) {
                case 0:
                    size = 5;
                    break;
                case 1:
                    size = 2;
                    break;
                case 2:
                    size = 3;
                    break;
                default:
                    size = 0;
            }

            assertEquals(size, expressions.size());
        }
    }

    @Test
    public void testThreeTokensWithTwoNotIdentifier() {
        for (int i = 0; i < 3; i ++ ) {
            String expression = "";
            for (int x = 0; x < 3; x ++ ) {
                if (x != i) {
                    expression += "#";
                }
                expression += "token ";
            }

            OgnlResolution resolution = new OgnlResolution(expression);
            Collection<String> expressions = resolution.expressionsListToResolve();

            int size = 0;

            switch (i) {
                case 0:
                    size = 1;
                    break;
                case 1:
                    size = 2;
                    break;
                case 2:
                    size = 2;
                    break;
                default:
                    size = 0;
            }

            assertEquals(size, expressions.size());
        }
    }

    @Test
    public void testThreeTokensNotIdentifiers() {
        String token1 = "#token1";
        String token2 = "#token2";
        String token3 = "#token3";

        OgnlResolution resolution = new OgnlResolution(token1 + " " + token2 + " " + token3);
        Collection<String> expressions = resolution.expressionsListToResolve();

        assertEquals(1, expressions.size());
        assertTrue(expressions.contains(token1 + " " + token2 + " " + token3));
    }

    @Test
    public void testExpressionWithFormat() {
        OgnlResolution resolution = new OgnlResolution("call of method(parameter)");
        Collection<String> expressions = resolution.expressionsListToResolve("format(%s)");

        for (String expression : expressions) {
            assertTrue(expression.startsWith("format("));
        }
    }
}
