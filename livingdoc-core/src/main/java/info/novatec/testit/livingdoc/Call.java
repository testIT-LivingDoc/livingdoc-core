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
package info.novatec.testit.livingdoc;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.call.Result;
import info.novatec.testit.livingdoc.call.ResultHandler;
import info.novatec.testit.livingdoc.call.Stub;
import info.novatec.testit.livingdoc.call.StubSyntax;
import info.novatec.testit.livingdoc.expectation.Collator;
import info.novatec.testit.livingdoc.expectation.Expectation;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.SystemUnderDevelopmentException;


/**
 * A test to be executed against the SUT.
 */
public class Call {
    private static final Logger LOG = LoggerFactory.getLogger(Call.class);

    private final Message message;
    private final Collection<String> inputs = new ArrayList<String>();

    private Expectation expectation;
    private Result result;
    private List<ResultHandler> handlers = new ArrayList<ResultHandler>();

    public Call(Message message) {
        this.message = message;
    }

    public void addInput(String... values) {
        inputs.addAll(Arrays.asList(values));
    }

    public Object execute(String... args) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        result = new Result(expectation);
        try {
            result.setActual(message.send(mergeInputsWith(args)));
        } catch (SystemUnderDevelopmentException e) {
            LOG.error(LOG_ERROR, e);
            result.exceptionOccured(e.getCause());
        }

        dispatchForHandling(result);

        return result.getActual();
    }

    private void dispatchForHandling(Result paramResult) {
        for (ResultHandler handler : handlers) {
            handler.handle(paramResult);
        }
    }

    public void expect(Collator collator) {
        expect(collator.toExpectation());
    }

    public void expect(Expectation expected) {
        this.expectation = expected;
    }

    public void expect(String value) {
        expect(ShouldBe.literal(value));
    }

    private String[] mergeInputsWith(String... args) {
        inputs.addAll(Arrays.asList(args));
        return inputs.toArray(new String[inputs.size()]);
    }

    public Result getResult() {
        return result;
    }

    public boolean wasRight() {
        return result.isRight();
    }

    public boolean wasWrong() {
        return result.isWrong();
    }

    public boolean wasIgnored() {
        return result.isIgnored();
    }

    public boolean hasFailed() {
        return result.isException();
    }

    public Throwable getFailure() {
        return result.getException();
    }

    public StubSyntax will(Stub stub) {
        ResultHandler handler = new ResultHandler(stub);
        handlers.add(handler);
        return handler;
    }
}
