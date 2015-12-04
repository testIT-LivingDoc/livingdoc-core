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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.novatec.testit.livingdoc.expectation.Expectation;
import info.novatec.testit.livingdoc.util.FactoryMethod;


public class OgnlExpectation implements Expectation {
    private static final String CONDITIONAL_EXPRESSION_NO_DOUBLE_QUOTES_PATTERN = "(?s).*\\?.*";
    private static final String CONDITIONAL_EXPRESSION_WITH_DOUBLE_QUOTES_PATTERN = "(?s)(\"[^\"]*\")+";
    private static final String CONDITIONAL_EXPRESSION_REPLACE = "\\?";

    private static final String EXTRACT_EXPRESSION_PATTERN = "^\\s*=\\s*.*\\s*$";
    private static final String EXTRACT_EXPRESSION_FORMAT = "=%s";

    private static final String RESULT_CONTEXT_VARIABLE_NAME = "result";
    private static final String OGNL_RESULT_VARIABLE_REFERENCE = "#" + RESULT_CONTEXT_VARIABLE_NAME;

    private static final String EQ_EXPR = OGNL_RESULT_VARIABLE_REFERENCE + "==(%s)";

    private final String matchee;
    private final Object target;

    @FactoryMethod
    public static OgnlExpectation create(String expected) {

        if ( ! OgnlExpectation.conditionalExpression(expected) && ! OgnlExpectation.extractExpression(expected)) {
            return null;
        }
        return new OgnlExpectation(expected);
    }

    public OgnlExpectation(String matchee) {
        this(matchee, null);
    }

    public OgnlExpectation(String matchee, Object target) {
        this.target = target;
        assertValidExpression(matchee);
        this.matchee = matchee;
    }

    private void assertValidExpression(String paramMatchee) {
        if ( ! conditionalExpression(paramMatchee) && ! extractExpression(paramMatchee)) {
            throw new IllegalArgumentException("Unsupported ognl expression type");
        }
    }

    @Override
    public StringBuilder describeTo(StringBuilder sb) {
        return sb.append(matchee);
    }

    @Override
    public boolean meets(Object result) {
        Object meets = executeExpression(result);

        if ( ! ( meets instanceof Boolean )) {
            throw new NotABooleanExpressionException(matchee);
        }

        return ( Boolean ) meets;
    }

    private Object executeExpression(Object result) {
        OgnlExpression ognl = buildExpression();

        ognl.addContextVariable(RESULT_CONTEXT_VARIABLE_NAME, result);

        return ognl.extractValue();
    }

    private OgnlExpression buildExpression() {
        if (conditionalExpression(matchee)) {
            return buildConditionalExpression();
        }
        return buildEqualExpression();
    }

    public static boolean conditionalExpression(String expectation) {
        String expression = expectation.replaceAll(CONDITIONAL_EXPRESSION_WITH_DOUBLE_QUOTES_PATTERN, "");

        return Pattern.matches(CONDITIONAL_EXPRESSION_NO_DOUBLE_QUOTES_PATTERN, expression);
    }

    public static boolean extractExpression(String expectation) {
        return Pattern.matches(EXTRACT_EXPRESSION_PATTERN, expectation);
    }

    public static String formatExtractExpression(String expectation) {
        return String.format(EXTRACT_EXPRESSION_FORMAT, expectation);
    }

    private OgnlExpression buildConditionalExpression() {
        String expression = replacePlaceholders();

        if (extractExpression(expression)) {
            expression = removeExtractExpressionDelimiters(expression);
        }

        return OgnlExpression.onUnresolvedExpression(expression, target);
    }

    private String replacePlaceholders() {
        // Algorithm:
        // 1. Replace all double quotes enclosed strings by a unique identifier.
        // 2. Replace placeholders by the ognl variable.
        // 3. Bring back enclosed double quotes strings in expression.
        List<String> doubleQuotesEnclosedStrings = extractDoubleQuotesEnclosedStrings();

        UUID uuid = UUID.randomUUID();

        String expression = matchee.replaceAll(CONDITIONAL_EXPRESSION_WITH_DOUBLE_QUOTES_PATTERN, uuid.toString());
        expression = expression.replaceAll(CONDITIONAL_EXPRESSION_REPLACE, OGNL_RESULT_VARIABLE_REFERENCE);

        for (String doubleQuotesEnclosedString : doubleQuotesEnclosedStrings) {
            expression = expression.replaceFirst(uuid.toString(), doubleQuotesEnclosedString);
        }

        return expression;
    }

    private List<String> extractDoubleQuotesEnclosedStrings() {
        List<String> doubleQuotesEnclosedStrings = new ArrayList<String>();

        Pattern pattern = Pattern.compile(CONDITIONAL_EXPRESSION_WITH_DOUBLE_QUOTES_PATTERN);
        Matcher matcher = pattern.matcher(matchee);

        while (matcher.find()) {
            doubleQuotesEnclosedStrings.add(matcher.group());
        }
        return doubleQuotesEnclosedStrings;
    }

    private OgnlExpression buildEqualExpression() {
        String expression = removeExtractExpressionDelimiters(matchee);

        return OgnlExpression.onUnresolvedExpression(expression, EQ_EXPR, target);
    }

    private String removeExtractExpressionDelimiters(String expression) {
        String cleanExpression = expression.trim();
        cleanExpression = cleanExpression.substring(1);

        return cleanExpression;
    }
}
