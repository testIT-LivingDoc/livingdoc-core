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

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_THREE;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH_NULL;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class RowFixtureSplitter {
    private static final Logger LOG = LoggerFactory.getLogger(RowFixtureSplitter.class);

    private List<RowFixture> match = new ArrayList<RowFixture>();
    private List<Example> missing = new ArrayList<Example>();
    private List<Fixture> surplus = new ArrayList<Fixture>();

    public List<RowFixture> getMatch() {
        return match;
    }

    public List<Example> getMissing() {
        return missing;
    }

    public List<Fixture> getSurplus() {
        return surplus;
    }

    public void split(Example rows, List<Fixture> adapters, Example headers) {
        LOG.trace(ENTRY_WITH_THREE, rows.toString(), adapters.toString(), headers.toString());
        surplus.addAll(adapters);
        for (Example row : rows) {
            Fixture adapter = findMatchingFixture(row, surplus, headers);
            if (adapter != null) {
                surplus.remove(adapter);
                match.add(new RowFixture(row, adapter));
            } else {
                missing.add(row);
            }
        }
        LOG.trace(EXIT);
    }

    private Fixture findMatchingFixture(Example row, List<Fixture> fixtures, Example headers) {
        LOG.trace(ENTRY_WITH_THREE, row.toString(), fixtures.toString(), headers.toString());
        for (Fixture fixture : fixtures) {
            if (Row.parse(fixture, headers).matches(row.firstChild())) {
                LOG.trace(EXIT_WITH, fixture.toString());
                return fixture;
            }
        }
        LOG.trace(EXIT_WITH_NULL);
        return null;
    }

}
