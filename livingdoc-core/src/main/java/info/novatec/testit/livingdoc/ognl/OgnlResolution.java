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

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.util.NameUtils;


/**
 * Build an ordered expressions list to resolve at OGNL execution.
 */
public class OgnlResolution {
    private String expression;
    private String format;

    public OgnlResolution(String expression) {
        if (StringUtils.isBlank(expression)) {
            throw new IllegalArgumentException("No expression to resolve");
        }

        this.expression = expression;
    }

    public List<String> expressionsListToResolve() {
        String[] parts = expression.trim().split("\\s+");
        List<String> expressionsList = new ArrayList<String>();

        withDots(expressionsList, parts);
        noDots(expressionsList, parts);
        asIs(expressionsList, parts);

        return expressionsList;
    }

    public List<String> expressionsListToResolve(String paramFormat) {
        this.format = paramFormat;

        return expressionsListToResolve();
    }

    private void withDots(List<String> expressionsList, String[] parts) {
        boolean[] dots = new boolean[parts.length - 1];

        placeDots(expressionsList, parts, dots, 0);
    }

    private void placeDots(List<String> expressionsList, String[] parts, boolean[] dots, int from) {
        for (int i = from; i < dots.length; i ++ ) {
            dots[i] = true;

            placeDots(expressionsList, parts, dots, i + 1);

            addExpression(expressionsList, parts, dots);

            dots[i] = false;
        }
    }

    private void noDots(List<String> expressionsList, String[] parts) {
        boolean[] dots = new boolean[parts.length - 1];

        addExpression(expressionsList, parts, dots);
    }

    private void asIs(List<String> expressionsList, String[] parts) {
        StringBuilder addExpression = new StringBuilder();

        for (int i = 0; i < parts.length; i ++ ) {
            addExpression.append(parts[i]).append(' ');
        }

        addToList(expressionsList, addExpression.toString());
    }

    private void addExpression(List<String> expressionsList, String[] parts, boolean[] dots) {
        StringBuilder addExpression = new StringBuilder();
        StringBuilder identifier = new StringBuilder();

        for (int i = 0; i < parts.length; i ++ ) {
            if (startAsAnIdentifier(parts[i])) {
                identifier.append(parts[i]).append(' ');

                if (identifierComplete(parts, dots, i)) {
                    addExpression.append(NameUtils.toLowerCamelCase(identifier.toString()));
                    addExpression.append( insertDot(parts, dots, i) ? "." : " " );

                    identifier.setLength(0);
                }
            } else {
                addExpression.append(parts[i]);
                addExpression.append(insertDot(parts, dots, i) ? "." : " " );
            }
        }

        addToList(expressionsList, addExpression.toString());
    }

    private void addToList(List<String> expressionsList, String paramExpression) {
        String addExpression = paramExpression.trim();

        if (format != null) {
            addExpression = String.format(format, addExpression);
        }

        if ( ! StringUtils.isEmpty(addExpression) && ! expressionsList.contains(addExpression)) {
            expressionsList.add(addExpression);
        }
    }

    private boolean startAsAnIdentifier(String token) {
        return Character.isJavaIdentifierStart(token.codePointAt(0));
    }

    private boolean identifierComplete(String[] parts, boolean[] dots, int index) {
        /* The identifier is complete if: - It's the last token or - The next
         * token does not start as a valid Java identifier or - A dot is to be
         * insert after identifier. */
        return index == parts.length - 1 || ! Character.isJavaIdentifierStart(firstChar(parts[index + 1])) || insertDot(
            parts, dots, index);
    }

    private int firstChar(String s) {
        return s.codePointAt(0);
    }

    private boolean insertDot(String[] parts, boolean[] dots, int index) {
        /* A dot is to be inserted if: - It's not the last token and -
         * Indication to actually try to insert it and - The next token start as
         * a valid Java identifier. */
        return index < parts.length - 1 && dots[index] && Character.isJavaIdentifierStart(firstChar(parts[index + 1]));
    }
}
