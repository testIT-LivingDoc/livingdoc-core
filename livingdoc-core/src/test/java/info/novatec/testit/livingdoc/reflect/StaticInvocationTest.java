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
package info.novatec.testit.livingdoc.reflect;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Calculator;


/**
 * ?: support for varargs methods
 */
public class StaticInvocationTest {
    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new Calculator();
    }

    @Test
    public void testInvokesMethodOnTarget() throws Throwable {
        StaticInvocation sum = new StaticInvocation(calculator, Calculator.SUM);
        assertEquals(5, sum.send("3", "2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChecksInputsAgainstExpectedNumberOfArguments() throws Exception {
        StaticInvocation add = new StaticInvocation(calculator, Calculator.ADD);
        add.send("3", "2");
    }
}
