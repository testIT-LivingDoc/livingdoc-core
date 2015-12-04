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

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.util.NameUtils;
import ognl.DefaultTypeConverter;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;


/**
 * Wrapper of ognl expression parsing, value extraction and insertion.
 */
public class OgnlExpression {
    private static final Logger LOG = LoggerFactory.getLogger(OgnlExpression.class);

    private static final Object[] NO_ROOT = new Object[] { null };
    private static final String GET_VALUE = null;

    // Must be ordered... order is important in expression resolution.
    private List<String> expressions;
    private Object[] targets;
    private Map<String, Object> context;

    @SuppressWarnings("unchecked")
    private OgnlExpression(List<String> expressions, Object... targets) {
        this.targets = targets.length != 0 ? targets : NO_ROOT;
        this.expressions = expressions;
        this.context = Ognl.createDefaultContext(null);
        Ognl.setTypeConverter(this.context, new OgnlTypeConverter());
    }

    public static OgnlExpression onSingleExpression(String expression, Object... targets) {
        List<String> expressions = new ArrayList<String>();
        expressions.add(expression);

        return new OgnlExpression(expressions, targets);
    }

    public static OgnlExpression onUnresolvedExpression(String expression, Object... targets) {
        OgnlResolution resolver = new OgnlResolution(expression);

        return new OgnlExpression(resolver.expressionsListToResolve(), targets);
    }

    public static OgnlExpression onUnresolvedExpression(String expression, String format, Object... targets) {
        OgnlResolution resolver = new OgnlResolution(expression);

        return new OgnlExpression(resolver.expressionsListToResolve(format), targets);
    }

    public static boolean isGetter(String valueExpression) {
        return isExpression(valueExpression);
    }

    public static boolean isSetter(String valueExpression) {
        return isExpression(valueExpression);
    }

    private static boolean isExpression(String valueExpression) {
        return ! NameUtils.isJavaIdentifier(NameUtils.decapitalize(valueExpression));
    }

    public void addContextVariable(String name, Object value) {
        context.put(name, value);
    }

    public Object extractValue() {
        return executeOnTargets(GET_VALUE);
    }

    public void insertValue(String setValue) {
        executeOnTargets(setValue);
    }

    private Object executeOnTargets(String value) {
        UnresolvableExpressionException resolutionException = new UnresolvableExpressionException(value);

        for (Object target : targets) {
            for (String expression : expressions) {
                try {
                    return executeExpressionOnTarget(target, expression, value);
                } catch (OgnlException e) {
                    LOG.error(LOG_ERROR, e);
                    resolutionException.addCause(e);
                }
            }
        }

        throw resolutionException;
    }

    private Object executeExpressionOnTarget(Object target, String expression, String value) throws OgnlException {
        Object parsedExpression = Ognl.parseExpression(expression);

        if (value == GET_VALUE) {
            return Ognl.getValue(parsedExpression, context, target);
        }
        Ognl.setValue(parsedExpression, context, target, value);
        return null;
    }

    private class OgnlTypeConverter implements TypeConverter {

        private TypeConverter defaultTypeConverter = new DefaultTypeConverter();

        @Override
        @SuppressWarnings({ "rawtypes" })
        public Object convertValue(Map paramContext, Object target, Member member, String propertyName, Object fromValue,
            Class toType) {

            Object toValue;
            Class< ? > type = toType;

            if ( ( fromValue instanceof String ) && TypeConversion.supports(type)) {
                toValue = TypeConversion.parse(( String ) fromValue, type);
            } else {
                toValue = defaultTypeConverter.convertValue(paramContext, target, member, propertyName, fromValue, type);
            }

            return toValue;

        }

    }
}
