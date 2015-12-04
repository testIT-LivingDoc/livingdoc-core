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

import info.novatec.testit.livingdoc.expectation.DuckExpectation;


public class Expectation {
    private String expected;
    private Object actual;
    private String describe;

    public Expectation() {
    }

    public Expectation(String expected) {
        this.expected = expected;
    }

    public boolean meets() {
        DuckExpectation duck = DuckExpectation.create(getExpected());
        return duck.meets(actual);
    }

    public String getExpected() {
        return expected;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public String getDescribe() {
        return describe == null ? String.format("%s (and not %s)", getActual(), getExpected()) : describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return String.format("Expectation: { expected: %s, actual: %s }", expected, actual);
    }
}
