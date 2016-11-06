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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.After;
import org.junit.Test;

import info.novatec.testit.livingdoc.interpreter.DecisionTableInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;


public class LivingDocTest {
    @After
    public void tearDown() throws Exception {
        LivingDoc.setLocale(Locale.ENGLISH);
    }

    @Test
    public void testCanFindAnInterpreterGivenAClassName() {
        assertTrue(LivingDoc.isAnInterpreter("info.novatec.testit.livingdoc.interpreter.DecisionTableInterpreter"));
        assertFalse(LivingDoc.isAnInterpreter("MissingInterpreter"));
    }

    @Test
    public void testCanInstantiateAnInterpreterGivenAClassName() throws Throwable {
        Interpreter interpreter = LivingDoc.getInterpreter("info.novatec.testit.livingdoc.interpreter.DecisionTableInterpreter",
            dummyFixture());
        assertThat(interpreter, is(instanceOf(DecisionTableInterpreter.class)));
    }

    @Test
    public void testComplainsWhenInterpreterCannotBeFound() {
        try {
            LivingDoc.getInterpreter("MissingInterpreter");
            fail();
        } catch (Throwable e) {
            assertTrue(true);
        }
    }

    @Test
    public void testComplainsWhenInterpreterCannotBeInstantiated() {
        try {
            LivingDoc.getInterpreter(BadInterpreter.class.getName());
            fail();
        } catch (Throwable e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInterpreterClassNamesCanBeAliased() {
        LivingDoc.aliasInterpreter("Calculate", DecisionTableInterpreter.class.getName());
        assertTrue(LivingDoc.isAnInterpreter("Calculate"));
    }

    @Test
    public void testShouldFindCoreInterpetersUsingTheirHumanName() {
        assertTrue(LivingDoc.isAnInterpreter("rule for"));
    }

    private Fixture dummyFixture() {
        return new PlainOldFixture(new Object());
    }

    @Test
    public void testOnlyClassesThatImplementsInterpreterShouldBeConsideredInterpreters() {
        assertFalse(LivingDoc.isAnInterpreter(String.class.getName()));
    }

    @Test
    public void testInterpreterInterfaceShouldNotBeConsideredAnInterpreter() {
        assertFalse(LivingDoc.isAnInterpreter(Interpreter.class.getName()));
    }

    @Test
    public void testThatIfKeyNotInBundleTheKeyIsReturned() {
        String key = "a.key";
        assertEquals(key, LivingDoc.$(key));
    }

    @Test
    public void testThatDefaultLocaleIsUsedToResolveKeys() {
        LivingDoc.setLocale(Locale.ENGLISH);

        assertEquals("Expected", LivingDoc.$("expected"));
        LivingDoc.setLocale(Locale.FRANCE);
        assertEquals("Attendu", LivingDoc.$("expected"));
    }

    public static class BadInterpreter implements Interpreter {
        // cannot be instantiated.
        private BadInterpreter() {
            super();
        }

        @Override
        public void interpret(Specification specification) {
            // No implementation needed.
        }
    }
}
