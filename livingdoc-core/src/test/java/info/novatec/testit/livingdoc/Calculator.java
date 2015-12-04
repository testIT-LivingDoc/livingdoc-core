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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.AssertionFailedError;


public class Calculator {
    public static Method ADD, DIVIDE, MULTIPLY, SUM, TOTAL;
    public static Field X;

    static {
        try {
            TOTAL = Calculator.class.getMethod("total");
            ADD = Calculator.class.getMethod("add", int.class);
            MULTIPLY = Calculator.class.getMethod("multiply", int.class);
            SUM = Calculator.class.getMethod("sum", int.class, int.class);
            DIVIDE = Calculator.class.getMethod("divide", int.class, int.class);

            X = Calculator.class.getField("x");
        } catch (Exception e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    public int x = 0;
    public int a;

    public void add(int value) {
        x += value;
    }

    public int sum(int lhs, int rhs) {
        return lhs + rhs;
    }

    public int total() {
        return x;
    }

    public void multiply(int y) {
        x *= y;
    }

    public int divide(int lhs, int rhs) {
        return lhs / rhs;
    }
}
