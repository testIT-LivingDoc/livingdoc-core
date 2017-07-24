/*
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

import info.novatec.testit.livingdoc.runner.*;
import info.novatec.testit.livingdoc.server.*;
import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rpc.runner.report.*;

import java.io.*;
import java.util.*;


public class RemoteDocumentRunner implements SpecificationRunner {

    private RestRemoteRunner restRemoteRunner;
    private SpecificationRunnerMonitor monitor;
    private String project;
    private String systemUnderTest;
    private String repositoryId;
    private ReportGenerator reportGenerator;
    private Locale locale;

    public void setRestRemoteRunner(RestRemoteRunner restRemoteRunner) {
        this.restRemoteRunner = restRemoteRunner;
    }

    public void setMonitor(SpecificationRunnerMonitor monitor) {
        this.monitor = monitor;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setSystemUnderTest(String systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void setFileReportGenerator(FileReportGenerator fileReportGenerator) {
        this.reportGenerator = fileReportGenerator;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void run(String source, String output) {
        Report report = null;

        try {
            monitor.testRunning(getLocation(source, '/'));

            Execution execution = restRemoteRunner.runSpecification(project, systemUnderTest, repositoryId, source, false,
                    locale.getLanguage());

            report = reportGenerator.openReport(getLocation(source, '-'));

            report.generate(execution);

            monitor.testDone(execution.getSuccess(), execution.getFailures(), execution.getErrors(), execution.getIgnored());
        } catch (Throwable t) {
            if (report != null) {
                report.renderException(t);
            }
            monitor.exceptionOccurred(t);
        } finally {
            closeReport(report);
        }
    }

    private String getLocation(String specificationName, char separator) {
        return repositoryId + separator + specificationName;
    }

    private void closeReport(Report report) {
        if (report == null) {
            return;
        }
        try {
            reportGenerator.closeReport(report);
        } catch (LivingDocServerException e) {
            monitor.exceptionOccurred(e);
        } catch (IOException e) {
            monitor.exceptionOccurred(e);
        }
    }

    @Override
    public info.novatec.testit.livingdoc.report.ReportGenerator getReportGenerator() {
        throw new UnsupportedOperationException();
    }
}
