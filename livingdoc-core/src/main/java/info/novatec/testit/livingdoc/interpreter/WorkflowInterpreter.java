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

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.interpreter.flow.AbstractFlowInterpreter;
import info.novatec.testit.livingdoc.interpreter.flow.workflow.WorkflowRowSelector;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class WorkflowInterpreter extends AbstractFlowInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowInterpreter.class);

    private static final Collection<String> suffixes = Collections.synchronizedCollection(new ArrayList<String>());
    private static final Collection<String> packages = Collections.synchronizedCollection(new ArrayList<String>());

    public WorkflowInterpreter(Fixture fixture) {
        super(fixture);
        WorkflowRowSelector selector = new WorkflowRowSelector(fixture);
        selector.addSuffixes(suffixes);
        selector.addPackages(packages);
        setRowSelector(selector);

    }

    public static void addRowSuffix(String suffix) {
        LOG.debug(ENTRY_WITH, suffix);
        suffixes.add(suffix);
        LOG.debug(EXIT);
    }

    public static void addRowsInPackage(String packageName) {
        LOG.debug(ENTRY_WITH, packageName);
        packages.add(packageName);
        LOG.debug(EXIT);
    }

}
