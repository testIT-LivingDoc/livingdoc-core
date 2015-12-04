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
package info.novatec.testit.livingdoc.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.testing.TestingFixture;
import info.novatec.testit.livingdoc.util.Tables;


public class ImportInterpreterTest {
    private ImportInterpreter interpreter;
    private SystemUnderDevelopment systemUnderDevelopment;

    @Before
    public void setUp() throws Exception {
        systemUnderDevelopment = new DefaultSystemUnderDevelopment();
        interpreter = new ImportInterpreter(systemUnderDevelopment);
    }

    @Test
    public void testSpecifiedPackagesShouldBeAutomaticallyImportedInSystemUnderTest() throws Throwable {
        Tables tables = Tables.parse("[import]\n" + "[info.novatec.testit.livingdoc.testing]");
        interpreter.interpret(spec(tables));

        assertEquals(TestingFixture.class, systemUnderDevelopment.getFixture("TestingFixture").getTarget().getClass());
    }

    @Test
    public void testSpecifiedPackagesShouldBeAutomaticallyImportedInFramework() throws Exception {
        Tables tables = Tables.parse("[import]\n" + "[info.novatec.testit.livingdoc.testing]");
        interpreter.interpret(spec(tables));

        assertTrue(LivingDoc.isAnInterpreter("TestingInterpreter"));
    }

    private Specification spec(Tables tables) {
        return new FakeSpecification(tables);
    }

    public static class CustomInterpreter implements Interpreter {

        @Override
        public void interpret(Specification specification) {
            // No implementation needed.
        }
    }
}
