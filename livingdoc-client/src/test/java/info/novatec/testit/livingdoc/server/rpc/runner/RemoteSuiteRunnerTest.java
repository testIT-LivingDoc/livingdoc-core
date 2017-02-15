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

import static org.mockito.Matchers.any;
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
import info.novatec.testit.livingdoc.server.domain.DocumentNode;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rpc.runner.report.Report;
import info.novatec.testit.livingdoc.server.rpc.runner.report.ReportGenerator;


@RunWith(MockitoJUnitRunner.class)
public class RemoteSuiteRunnerTest {

    @Mock
    protected SpecificationRunnerMonitor monitor;
    @Mock
    protected RestRemoteRunner restRemoteRunner;
    @Mock
    protected ReportGenerator reportGenerator;
    @Mock
    protected Report report;

    private RemoteSuiteRunner runner;

    private DocumentNode documentNode = new DocumentNode("test");
    private LivingDocServerException serverException = new LivingDocServerException();

    @Before
    public void setUp() throws Exception {
        runner = new RemoteSuiteRunner();
        runner.setLocale(Locale.ENGLISH);
        runner.setMonitor(monitor);
        runner.setProject("project");
        runner.setReportGenerator(reportGenerator);
        runner.setRepositoryId("repositoryId");
        runner.setSystemUnderTest("sut");
        runner.setRestRemoteRunner(restRemoteRunner);
    }

    @Test
    public void testWithEmptySpecificationResultList() throws Exception {
        doReturn(documentNode).when(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(
            SystemUnderTest.class));

        runner.run("a", "b");

        verify(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(SystemUnderTest.class));
        verify(monitor).testRunning("repositoryId/a");
        verify(monitor).testDone(0, 0, 0, 0);
    }

    @Test
    public void testWithSpecificationHierarchyFailure() throws Exception {
        doThrow(serverException).when(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(
            SystemUnderTest.class));

        runner.run("a", "b");

        verify(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(SystemUnderTest.class));
        verify(monitor).exceptionOccurred(serverException);
    }

    @Test
    public void testASuccessfullExecution() throws Exception {
        final DocumentNode createdDocumentNode = createDocumentNode();
        final Execution execution = createExecution();
        doReturn(createdDocumentNode).when(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(
            SystemUnderTest.class));
        doReturn(execution).when(restRemoteRunner).runSpecification("project", "sut", "repositoryId", "A", false, "en");
        doReturn(report).when(reportGenerator).openReport("repositoryId-A");

        runner.run("a", "b");

        verify(restRemoteRunner).getSpecificationHierarchy(any(Repository.class), any(SystemUnderTest.class));
        verify(monitor).testRunning("repositoryId/A");
        verify(restRemoteRunner).runSpecification("project", "sut", "repositoryId", "A", false, "en");
        verify(reportGenerator).openReport("repositoryId-A");
        verify(report).generate(execution);
        verify(monitor).testDone(4, 3, 2, 1);
        verify(reportGenerator).closeReport(report);
    }

    private DocumentNode createDocumentNode() {
        final DocumentNode node = documentNode;

        DocumentNode childNodeA = new DocumentNode("A");
        childNodeA.setIsExecutable(true);

        node.addChildren(childNodeA);
        return node;
    }

    private Execution createExecution() {
        final Execution execution = new Execution();
        execution.setSuccess(4);
        execution.setFailures(3);
        execution.setErrors(2);
        execution.setIgnored(1);
        return execution;
    }
}
