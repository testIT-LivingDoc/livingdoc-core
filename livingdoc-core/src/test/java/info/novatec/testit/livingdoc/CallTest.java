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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.annotation.RightAnnotation;
import info.novatec.testit.livingdoc.annotation.WrongAnnotation;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.StaticInvocation;


@RunWith(MockitoJUnitRunner.class)
public class CallTest {
    @Mock
    private Annotatable annotatable;

    private Calculator calculator = new Calculator();

    @Test
    public void testIgnoresIfNoExpectationIsSet() throws Exception {
        Call call = new Call(new StaticInvocation(calculator, Calculator.TOTAL));
        call.execute();
        assertTrue(call.wasIgnored());
    }

    @Test
    public void testReportsASuccessIfOutputMatchesExpectation() throws Exception {
        Call call = new Call(new StaticInvocation(calculator, Calculator.SUM));
        call.addInput("5", "2");
        call.expect(ShouldBe.equal(7));

        call.will(Annotate.right(annotatable)).when(ResultIs.right());
        call.execute();
        verify(annotatable).annotate(any(RightAnnotation.class));
    }

    @Test
    public void testReportsAFailureIfOutputIsUnexpected() throws Exception {
        Call call = new Call(new StaticInvocation(calculator, Calculator.ADD));
        call.addInput("3");
        call.expect(ShouldBe.equal(3));

        call.will(Annotate.wrong(annotatable)).when(ResultIs.wrong());
        call.execute();
        verify(annotatable).annotate(any(WrongAnnotation.class));
    }

    @Test
    public void testAcceptsExceptionsAsValidExpectations() throws Exception {
        StaticInvocation divide = new StaticInvocation(calculator, Calculator.DIVIDE);
        Call call = new Call(divide);
        call.addInput("7", "0");
        call.expect(ShouldBe.instanceOf(ArithmeticException.class));

        call.execute();
        assertTrue(call.wasRight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedExceptionsAreReportedAsErrors() throws Exception {
        StaticInvocation error = new StaticInvocation(calculator, Calculator.MULTIPLY);
        Call call = new Call(error);
        call.execute();
    }

    @Test
    public void testConvertsExpectationValueToActionReturnType() throws Exception {
        Call call = new Call(new StaticInvocation(calculator, Calculator.SUM));
        call.addInput("2", "1");
        call.expect("3");
        call.execute();
        assertTrue(call.wasRight());
    }
}
