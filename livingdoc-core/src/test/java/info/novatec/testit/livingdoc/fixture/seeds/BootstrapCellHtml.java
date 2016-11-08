/* Copyright (c) 2007 Pyxis Technologies inc.
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

package info.novatec.testit.livingdoc.fixture.seeds;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.interpreter.DecisionTableInterpreter;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;


public class BootstrapCellHtml extends PlainOldFixture {
    private static final String HTML_ROW_HEADER = "<tr><td>result?</td></tr>";
    private static final String HTML_ROW_DATA = "<tr><td>%s</td></tr>";
    private static final String HTML_TEMPLATE = "<html><table>" + HTML_ROW_HEADER + HTML_ROW_DATA + "</table></html>";

    private static final String ERROR = "error";
    private String expected, returned;

    public BootstrapCellHtml(Object target, String expected, String returned) {
        super(target);
        this.expected = expected;
        this.returned = returned;
    }

    public String executeInterpreter() {
        Example example = null;

        try {
            example = parse();
            FakeSpecification spec = new FakeSpecification(example);
            Interpreter columnInterpreter = new DecisionTableInterpreter(this);
            columnInterpreter.interpret(spec);
        } catch (Exception e) {
            printErrorInCell(example, e);
        }

        return getContentFromCell(example);
    }

    public String result() throws Exception {
        if (returned.equals(ERROR)) {
            throw new Exception("exception");
        }
        return returned;
    }

    private Example parse() {
        String documentContent = String.format(HTML_TEMPLATE, expected);
        return HtmlDocumentBuilder.tables().parse(documentContent);
    }

    private void printErrorInCell(Example example, Exception e) {
        example.annotate(Annotations.exception(e));
    }

    private String getContentFromCell(Example example) {
        return example.getContent();
    }
}
