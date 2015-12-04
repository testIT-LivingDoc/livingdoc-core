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
package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.LivingDoc.canContinue;
import static info.novatec.testit.livingdoc.LivingDoc.setStopOnFirstFailure;
import static info.novatec.testit.livingdoc.LivingDoc.shouldStop;
import static info.novatec.testit.livingdoc.util.Tables.parse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.util.Tables;


public class OptionsInterpreterTest {

    private Tables tables;
    private OptionsInterpreter interpreter;
    private static boolean stopOnFirstFailure;

    @BeforeClass
    public static void beforeClass() throws Exception {
        stopOnFirstFailure = LivingDoc.isStopOnFirstFailure();
    }

    @Before
    public void setUp() throws Exception {
        interpreter = new OptionsInterpreter(null);
        setStopOnFirstFailure(false); // reset
    }

    @AfterClass
    public static void tearDown() throws Exception {
        setStopOnFirstFailure(stopOnFirstFailure); // reset
    }

    @Test
    public void testSettingStopOnFirstFailureOption() throws Exception {
        tables = parse("Options\n" + "[stop on first failure][true]");

        interpreter.interpret(document());

        assertTrue(LivingDoc.isStopOnFirstFailure());
    }

    @Test
    public void testShouldStopWhenExceptionFoundInStats() {
        assertFalse(shouldStop(new Statistics(0, 0, 0, 0)));
        assertFalse(shouldStop(new Statistics(0, 0, 1, 0)));

        assertTrue(canContinue(new Statistics(0, 0, 0, 0)));
        assertTrue(canContinue(new Statistics(0, 0, 1, 0)));

        setStopOnFirstFailure(true);

        assertFalse(shouldStop(new Statistics(0, 0, 0, 0)));
        assertTrue(shouldStop(new Statistics(0, 0, 1, 0)));

        assertTrue(canContinue(new Statistics(0, 0, 0, 0)));
        assertFalse(canContinue(new Statistics(0, 0, 1, 0)));
    }

    private FakeSpecification document() {
        return new FakeSpecification(tables);
    }
}
