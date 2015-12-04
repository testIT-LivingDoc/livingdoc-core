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

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.interpreter.collection.CollectionInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;


/**
 * <code>Interpreter</code> implementation that checks the properties of a set
 * of arbitrary objects against values of a set of <code>Specification</code>.
 */
public class SetOfInterpreter extends CollectionInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(SetOfInterpreter.class);

    public SetOfInterpreter(Fixture fixture) {
        super(fixture);
    }

    @Override
    protected boolean mustProcessMissing() {
        LOG.debug(ENTRY);
        LOG.debug(EXIT_WITH, true);
        return true;
    }

    @Override
    protected boolean mustProcessSurplus() {
        LOG.debug(ENTRY);
        LOG.debug(EXIT_WITH, true);
        return true;
    }
}
