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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.novatec.testit.livingdoc.runner.SpecificationRunner;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerMonitor;
import info.novatec.testit.livingdoc.server.domain.DocumentNode;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rpc.runner.report.ReportGenerator;


public class RemoteSuiteRunner implements SpecificationRunner {
    private XmlRpcRemoteRunner xmlRpcRemoteRunner;
    private SpecificationRunnerMonitor monitor;
    private String project;
    private String systemUnderTest;
    private String repositoryId;

    private final RemoteDocumentRunner documentRunner;

    public RemoteSuiteRunner() {
        this.documentRunner = new RemoteDocumentRunner();
    }

    public void setXmlRpcRemoteRunner(XmlRpcRemoteRunner xmlRpcRemoteRunner) {
        this.xmlRpcRemoteRunner = xmlRpcRemoteRunner;
        documentRunner.setXmlRpcRemoteRunner(xmlRpcRemoteRunner);
    }

    public void setMonitor(SpecificationRunnerMonitor monitor) {
        this.monitor = monitor;
        documentRunner.setMonitor(monitor);
    }

    public void setProject(String project) {
        this.project = project;
        documentRunner.setProject(project);
    }

    public void setSystemUnderTest(String systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
        documentRunner.setSystemUnderTest(systemUnderTest);
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        documentRunner.setRepositoryId(repositoryId);
    }

    public void setReportGenerator(ReportGenerator reportGenerator) {
        documentRunner.setReportGenerator(reportGenerator);
    }

    public void setLocale(Locale locale) {
        documentRunner.setLocale(locale);
    }

    @Override
    public void run(String source, String destination) {
        try {
            SystemUnderTest sut = SystemUnderTest.newInstance(systemUnderTest);
            sut.setProject(Project.newInstance(project));

            Repository repository = Repository.newInstance(repositoryId);

            DocumentNode documentNode = xmlRpcRemoteRunner.getSpecificationHierarchy(repository, sut);
            List<DocumentNode> executableSpecs = extractExecutableSpecifications(documentNode);

            if (executableSpecs.isEmpty()) {
                monitor.testRunning(getLocation(source, '/'));
                monitor.testDone(0, 0, 0, 0);
            } else {
                for (DocumentNode spec : executableSpecs) {
                    documentRunner.run(spec.getTitle(), destination);
                }
            }
        } catch (Throwable t) {
            monitor.exceptionOccurred(t);
        }
    }

    private String getLocation(String specificationName, char separator) {
        return repositoryId + separator + specificationName;
    }

    private List<DocumentNode> extractExecutableSpecifications(DocumentNode node) {
        List<DocumentNode> specifications = new ArrayList<DocumentNode>();

        if (node.isExecutable()) {
            specifications.add(node);
        }

        for (DocumentNode childNode : node.getChildren()) {
            specifications.addAll(extractExecutableSpecifications(childNode));
        }

        return specifications;
    }

    @Override
    public info.novatec.testit.livingdoc.report.ReportGenerator getReportGenerator() {
        throw new UnsupportedOperationException();
    }
}
