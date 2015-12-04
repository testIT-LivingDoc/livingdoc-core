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

package info.novatec.testit.livingdoc.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;


public class XmlReportTest {
    private static final String RESULTS = "<table><tr><td>my results</td></tr></table>";

    @Test
    public void testThatTheValuesAreAccessibleAfterAnInstanciationWithAString() throws Exception {
        XmlReport report = XmlReportMother.simpleResults();

        assertEquals(1, report.getSuccess(0));
        assertEquals(2, report.getFailure(0));
        assertEquals(3, report.getError(0));
        assertEquals(4, report.getIgnored(0));
        assertEquals(5, report.getAnnotation(0));
        assertEquals(6, report.getExecutionTime(0));
        assertEquals(7, report.getTotalTime(0));
        assertEquals("Calculator", report.getDocumentName(0));
        assertEquals("http://testit.novatec.info/confluence/display/LIVINGDOCDEMO/Calculator", report
            .getDocumentExternalLink(0));
        assertEquals(RESULTS, report.getResults(0));
        assertEquals("unix", report.getSections(0));
        assertNull(report.getSections(1));
    }

    @Test
    public void testThatTheValuesAreAccessibleAfterAnInstanciationWithAStream() throws Exception {
        XmlReport report = XmlReportMother.simpleResults();

        assertEquals(1, report.getSuccess(0));
        assertEquals(2, report.getFailure(0));
        assertEquals(3, report.getError(0));
        assertEquals(4, report.getIgnored(0));
        assertEquals(5, report.getAnnotation(0));
        assertEquals(6, report.getExecutionTime(0));
        assertEquals(7, report.getTotalTime(0));
        assertEquals(RESULTS, report.getResults(0));
    }

    @Test
    public void testThatTheValuesAreAccessibleAfterAnInstanciationWithACompilerAndAString() throws Exception {
        XmlReport parser = new XmlReport("");
        Document document = documentFor(RESULTS);
        document.setSections(new String[] { "unix" });
        document.tally(new Statistics(1, 2, 3, 4));
        parser.generate(document);

        assertEquals(1, parser.getSuccess(0));
        assertEquals(2, parser.getFailure(0));
        assertEquals(3, parser.getError(0));
        assertEquals(4, parser.getIgnored(0));
        assertEquals(RESULTS, parser.getResults(0));
        assertEquals("unix", parser.getSections(0));
    }

    @Test
    public void testStatisticsResult() throws Exception {
        XmlReport report = XmlReportMother.simpleResults();

        Statistics stats = report.toStatistics();

        assertEquals(1, stats.rightCount());
        assertEquals(2, stats.wrongCount());
        assertEquals(3, stats.exceptionCount());
        assertEquals(4, stats.ignoredCount());
    }

    private Document documentFor(String html) throws IOException {
        return HtmlDocumentBuilder.tables().build(new StringReader(html));
    }
}
