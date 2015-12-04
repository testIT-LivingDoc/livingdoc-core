/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.server.rpc.runner.report;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.Writer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.server.domain.Execution;


@RunWith(MockitoJUnitRunner.class)
public class HtmlReportTest {

    @Mock
    private Writer writer;
    private HtmlReport htmlReport = HtmlReport.newInstance("test");

    @Test
    public void testThatNothingIsPrintedWhenNoExecution() throws Exception {
        htmlReport.printTo(writer);
        verifyZeroInteractions(writer);
    }

    @Test
    public void testThatExceptionIsPrintedCorrectly() throws Exception {
        htmlReport.renderException(new NullPointerException("testThatExceptionIsPrintedCorrectly"));
        htmlReport.printTo(writer);

        String containedString1 = "testThatExceptionIsPrintedCorrectly";
        String containedString2 = "info.novatec.testit.livingdoc.server.rpc.runner.report";
        verify(writer).write(contains(containedString1));
        verify(writer).write(contains(containedString2));
        verify(writer).flush();
    }

    @Test
    public void testThatResultIsPrintedCorrectly() throws Exception {
        Execution execution = new Execution();
        execution.setResults("<html>test</html>");

        htmlReport.generate(execution);
        htmlReport.printTo(writer);

        String containedString1 = "body, p, td, table, tr, .bodytext, .stepfield {";
        String containedString2 = "<title>test</title>";
        String containedString3 = "<div id=\"Content\"";
        verify(writer).write(contains(containedString1));
        verify(writer).write(contains(containedString2));
        verify(writer).write(contains(containedString3));
        verify(writer).flush();
    }
}
