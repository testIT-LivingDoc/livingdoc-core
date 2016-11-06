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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


/**
 * <code>Interpreter</code> implementation that pushes imports into the context
 * import stack.
 */
public class ImportInterpreter implements Interpreter  {
    private static final Logger LOG = LoggerFactory.getLogger(ImportInterpreter.class);

    private final SystemUnderDevelopment systemUnderDevelopment;

    public ImportInterpreter(SystemUnderDevelopment systemUnderDevelopment) {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        Example table = specification.nextExample();
        for (Example row = table.at(0, 1); row != null; row = row.nextSibling()) {
            if ( ! row.hasChild()) {
                continue;
            }
            String packageName = row.firstChild().getContent();
            systemUnderDevelopment.addImport(packageName);
            LivingDoc.addImport(packageName);
        }
        specification.exampleDone(new Statistics());
        LOG.debug(EXIT);
    }
}
