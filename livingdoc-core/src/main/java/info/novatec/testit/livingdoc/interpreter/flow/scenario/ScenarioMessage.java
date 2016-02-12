/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.interpreter.flow.scenario;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.call.ResultMatcher;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.SystemUnderDevelopmentException;
import info.novatec.testit.livingdoc.util.DuckType;


public class ScenarioMessage extends Message {
    private static final Logger LOG = LoggerFactory.getLogger(ScenarioMessage.class);

    private final Object target;
    private Method method;
    private Annotation annotation;
    private MatchResult matchResult;

    private Object[] arguments;

    public ScenarioMessage(Object target, String example) {
        this.target = target;

        init(example);
    }

    @Override
    public int getArity() {
        return 0; // not used
    }

    @Override
    public Object send(String... args) throws SystemUnderDevelopmentException {
        arguments = buildArguments();
        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException ex) {
            LOG.error(LOG_ERROR, ex);
            throw new SystemUnderDevelopmentException(ex.getTargetException());
        } catch (IllegalAccessException ex) {
            LOG.error(LOG_ERROR, ex);
            throw new SystemUnderDevelopmentException(ex);
        }
    }

    public ResultMatcher annotationIs(Class< ? extends Annotation> annotationType) {
        return new AnnotationResultMatcher(annotation, annotationType);
    }

    public Object[] arguments() {
        return arguments;
    }

    public MatchResult matchResult() {
        return matchResult;
    }

    public Class< ? extends Throwable>[] getIgnoredExceptions() {
        IgnoredException ignoredException = method.getAnnotation(IgnoredException.class);
        return ignoredException == null ? null : ignoredException.value();
    }

    private void init(String example) {

        Method[] methods = target.getClass().getMethods();

        for (Method aMethod : methods) {
            if (match(aMethod, example)) {
                return;
            }
        }

        if (method == null) {
            throw new IllegalArgumentException(String.format("Cannot find method matching '%s' in class '%s'", example,
                target.getClass()));
        }
    }

    private boolean match(Method paramMethod, String scenario) {

        for (Annotation anAnnotation : paramMethod.getAnnotations()) {
            String regex = regularExpressionOf(anAnnotation);

            if (regex != null) {
                MatchResult match = match(scenario, regex);

                if (match != null) {
                    this.method = paramMethod;
                    this.annotation = anAnnotation;
                    this.matchResult = match;
                    return true;
                }
            }
        }

        return false;
    }

    private String regularExpressionOf(Annotation paramAnnotation) {

        if (DuckType.instanceOf(RegEx.class, paramAnnotation)) {
            try {
                RegEx regEx = DuckType.implement(RegEx.class, paramAnnotation);
                return regEx.value();
            } catch (Exception e) {
                LOG.error(LOG_ERROR, e);
                // Not a valid RegEx annotation
            }
        }

        return null;
    }

    private MatchResult match(String scenario, String regex) {

        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(scenario);

        return match.find() ? match.toMatchResult() : null;
    }

    private Object[] buildArguments() {

        String[] args = new String[matchResult.groupCount()];

        for (int i = 0; i < matchResult.groupCount(); i ++ ) {
            args[i] = matchResult.group(i + 1);
        }

        if (method.getParameterTypes().length != args.length) {
            throw new IllegalArgumentException(String.format("Wrong number of arguments: expected %d but got %d", method
                .getParameterTypes().length, args.length));
        }

        return TypeConversion.convert(args, method.getParameterTypes());
    }
}
