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
package info.novatec.testit.livingdoc.server.rpc.runner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.novatec.testit.livingdoc.runner.SpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.rpc.runner.report.Report;
import info.novatec.testit.livingdoc.server.rpc.runner.report.ReportGenerator;


@RunWith(MockitoJUnitRunner.class)
public class RemoteDocumentRunnerTest {

    @Mock
    protected SpecificationRunnerMonitor monitor;
    @Mock
    protected XmlRpcRemoteRunner xmlRpcRemoteRunner;
    @Mock
    protected ReportGenerator reportGenerator;
    @Mock
    protected Report report;

    private RemoteDocumentRunner runner;
    private Execution execution;

    private LivingDocServerException serverException = new LivingDocServerException();

    @Before
    public void setUp() throws Exception {
        runner = new RemoteDocumentRunner();
        runner.setLocale(Locale.ENGLISH);
        runner.setMonitor(monitor);
        runner.setProject("project");
        runner.setReportGenerator(reportGenerator);
        runner.setRepositoryId("repositoryId");
        runner.setSystemUnderTest("sut");
        runner.setXmlRpcRemoteRunner(xmlRpcRemoteRunner);

        execution = new Execution();
        execution.setSuccess(4);
        execution.setFailures(3);
        execution.setErrors(2);
        execution.setIgnored(1);
    }

    @Test
    public void testRunningSpecificationWithFailure() throws Exception {
        doThrow(serverException).when(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false,
            "en");

        runner.run("a", "b");

        verify(monitor).testRunning("repositoryId/a");
        verify(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        verify(monitor).exceptionOccurred(serverException);
    }

    @Test
    public void testWithReportGenerationFailure() throws Exception {
        doReturn(execution).when(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        doReturn(report).when(reportGenerator).openReport("repositoryId-a");
        doThrow(serverException).when(report).generate(execution);

        runner.run("a", "b");

        verify(monitor).testRunning("repositoryId/a");
        verify(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        verify(reportGenerator).openReport("repositoryId-a");
        verify(report).generate(execution);
        verify(report).renderException(serverException);
        verify(monitor).exceptionOccurred(serverException);
        verify(reportGenerator).closeReport(report);
    }

    @Test
    public void testWithClosingReportFailure() throws Exception {
        doReturn(execution).when(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        doReturn(report).when(reportGenerator).openReport("repositoryId-a");
        doThrow(serverException).when(reportGenerator).closeReport(report);

        runner.run("a", "b");

        verify(monitor).testRunning("repositoryId/a");
        verify(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        verify(reportGenerator).openReport("repositoryId-a");
        verify(report).generate(execution);
        verify(monitor).testDone(4, 3, 2, 1);
        verify(reportGenerator).closeReport(report);
        verify(monitor).exceptionOccurred(serverException);
    }

    @Test
    public void testASuccessfullExecution() throws Exception {
        doReturn(execution).when(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        doReturn(report).when(reportGenerator).openReport("repositoryId-a");

        runner.run("a", "b");

        verify(monitor).testRunning("repositoryId/a");
        verify(xmlRpcRemoteRunner).runSpecification("project", "sut", "repositoryId", "a", false, "en");
        verify(reportGenerator).openReport("repositoryId-a");
        verify(report).generate(execution);
        verify(monitor).testDone(4, 3, 2, 1);
        verify(reportGenerator).closeReport(report);
    }
}
