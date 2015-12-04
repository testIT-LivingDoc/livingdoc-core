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
public class XmlReportTest {

    @Mock
    private Writer writer;
    private XmlReport xmlReport = XmlReport.newInstance("test");

    @Test
    public void testThatNothingIsPrintedWhenNoExecution() throws Exception {
        xmlReport.printTo(writer);
        verifyZeroInteractions(writer);
    }

    @Test
    public void testThatExceptionIsPrintedCorrectly() throws Exception {
        String containedString =
            "<global-exception><![CDATA[java.lang.NullPointerException: testThatExceptionIsPrintedCorrectly";
        xmlReport.renderException(new NullPointerException("testThatExceptionIsPrintedCorrectly"));
        xmlReport.printTo(writer);

        verify(writer).write(contains(containedString));
        verify(writer).flush();
    }

    @Test
    public void testThatResultIsPrintedCorrectly() throws Exception {
        Execution execution = new Execution();
        execution.setResults("<html>test</html>");
        xmlReport.generate(execution);
        xmlReport.printTo(writer);

        String success = "<success>0</success>";
        String failure = "<failure>0</failure>";
        String error = "<error>0</error>";
        String result = "<results><![CDATA[<html>test</html>]]></results>";
        verify(writer).write(contains(success));
        verify(writer).write(contains(failure));
        verify(writer).write(contains(error));
        verify(writer).write(contains(result));
        verify(writer).flush();
    }
}
