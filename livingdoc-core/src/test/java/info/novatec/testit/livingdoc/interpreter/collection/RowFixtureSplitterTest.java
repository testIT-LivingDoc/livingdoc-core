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

package info.novatec.testit.livingdoc.interpreter.collection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


public class RowFixtureSplitterTest {

    @Test
    public void testThatRowsAndFixturesAreMatched() throws Exception {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new Integer(1)));
        adapters.add(new PlainOldFixture(new Integer(3)));
        adapters.add(new PlainOldFixture(new Integer(2)));

        Example example = Tables.parse("[intValue]\n" + "[1]\n" + "[2]\n" + "[3]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();

        splitter.split(example.at(1), adapters, example.firstChild());
        List<RowFixture> matchees = splitter.getMatch();

        assertEquals(3, matchees.size());
        RowFixture match = matchees.get(0);
        assertEquals("1", match.getRow().firstChild().getContent());
        assertEquals(new Integer(1), match.getAdapter().check("intValue").send());
        match = matchees.get(1);
        assertEquals("2", match.getRow().firstChild().getContent());
        assertEquals(new Integer(2), match.getAdapter().check("intValue").send());

    }

    @Test
    public void testThatMissingRowsAreProcessed() {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new Integer(1)));
        adapters.add(new PlainOldFixture(new Integer(3)));

        Example example = Tables.parse("[intValue]\n" + "[1]\n" + "[2]\n" + "[3]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());
        List<RowFixture> matchees = splitter.getMatch();
        assertEquals(2, matchees.size());
        assertEquals(1, splitter.getMissing().size());
        assertEquals("2", splitter.getMissing().get(0).firstChild().getContent());

    }

    @Test
    public void testThatSurplusRowsAreProcessed() {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new Integer(1)));
        adapters.add(new PlainOldFixture(new Integer(2)));
        adapters.add(new PlainOldFixture(new Integer(3)));

        Example example = Tables.parse("[intValue]\n" + "[1]\n" + "[3]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());
        List<RowFixture> matchees = splitter.getMatch();
        assertEquals(2, matchees.size());
        assertEquals(0, splitter.getMissing().size());
        assertEquals(1, splitter.getSurplus().size());
    }

    @Test
    public void testThatMultipleOccurencesOfItemInSpecification() throws Exception {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new String("Big Mac")));
        adapters.add(new PlainOldFixture(new String("Frites")));
        adapters.add(new PlainOldFixture(new String("Coke")));
        adapters.add(new PlainOldFixture(new String("Coke")));

        Example example = Tables.parse("[toString]\n" + "[Big Mac]\n" + "[Big Mac]\n" + "[Frites]\n" + "[Coke]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());

        assertEquals(3, splitter.getMatch().size());
        assertEquals(1, splitter.getMissing().size());
        assertEquals(1, splitter.getSurplus().size());
        assertEquals("Big Mac", splitter.getMissing().get(0).firstChild().getContent());
        assertEquals("Coke", splitter.getSurplus().get(0).check("toString").send());
    }

    @Test
    public void testThatMatchesOnMoreThanOneCellSpecification() throws Exception {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new EmployeName("Lapointe", "Christian")));
        adapters.add(new PlainOldFixture(new EmployeName("Rochambeau", "Fabrice")));
        adapters.add(new PlainOldFixture(new EmployeName("Carrey", "Gilles")));

        Example example = Tables.parse("[last][first]\n" + "[Lapointe][Christian]\n" + "[Rochambeau][Fabrice]\n"
            + "[Carrey][Gilles]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());

        assertEquals(3, splitter.getMatch().size());
        assertEquals(0, splitter.getMissing().size());
        assertEquals(0, splitter.getSurplus().size());

        assertEquals("Lapointe", splitter.getMatch().get(0).getRow().firstChild().getContent());
        // assertEquals("Lapointe",
        // splitter.getMatch().get(0).getAdapter().getQuery("last").send());
    }

    @Test
    public void testThatSurplusAndMissingOnThanOneCellSpecification() throws Exception {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new EmployeName("Lapointe", "Jean-Christophe")));
        adapters.add(new PlainOldFixture(new EmployeName("Muller", "Fabrice")));
        adapters.add(new PlainOldFixture(new EmployeName("Carrey", "Gilles")));
        adapters.add(new PlainOldFixture(new EmployeName("Rochambeau", "Benjamin")));

        Example example = Tables.parse("[last][first]\n" + "[Lapointe][Christian]\n" + "[Lapointe][Jean-Christophe]\n"
            + "[Rochambeau][Patrice]\n" + "[Carrey][Gilles]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());

        assertEquals(2, splitter.getMatch().size());
        assertEquals(2, splitter.getMissing().size());
        assertEquals(2, splitter.getSurplus().size());

        assertEquals("Jean-Christophe", splitter.getMatch().get(0).getRow().at(0, 1).getContent());
        assertEquals("Christian", splitter.getMissing().get(0).at(0, 1).getContent());
        assertEquals("Fabrice", splitter.getSurplus().get(0).check("first").send());
    }

    @Test
    public void testThatExpectedColumnAreNotConsideredInMatching() throws Exception {
        ArrayList<Fixture> adapters = new ArrayList<Fixture>();
        adapters.add(new PlainOldFixture(new EmployeName("Lapointe", "Jean-Christophe", true)));
        adapters.add(new PlainOldFixture(new EmployeName("Carrey", "Gilles", false)));

        Example example = Tables.parse("[last][first][is developper][is developper?]\n"
            + "[Lapointe][Jean-Christophe][true][false]\n" + "[Carrey][Gilles][false][true]").at(0, 0);

        RowFixtureSplitter splitter = new RowFixtureSplitter();
        splitter.split(example.at(1), adapters, example.firstChild());

        assertEquals(2, splitter.getMatch().size());
        assertEquals(0, splitter.getMissing().size());
        assertEquals(0, splitter.getSurplus().size());

        assertEquals("Jean-Christophe", splitter.getMatch().get(0).getRow().at(0, 1).getContent());
    }

    public static class EmployeName {
        public String last;
        public String first;
        public boolean isDevelopper;

        public EmployeName(String last, String first) {
            this(last, first, false);
        }

        public EmployeName(String last, String first, boolean isDevelopper) {
            this.isDevelopper = isDevelopper;
            this.last = last;
            this.first = first;
        }
    }
}
