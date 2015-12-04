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

package info.novatec.testit.livingdoc.runner;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.CommentTableFilter;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.document.InterpreterSelector;
import info.novatec.testit.livingdoc.document.LivingDocTableFilter;
import info.novatec.testit.livingdoc.document.SectionsTableFilter;
import info.novatec.testit.livingdoc.report.Report;
import info.novatec.testit.livingdoc.report.ReportGenerator;
import info.novatec.testit.livingdoc.repository.DocumentRepository;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class DocumentRunner implements SpecificationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentRunner.class);

    private ReportGenerator reportGenerator;
    private SystemUnderDevelopment systemUnderDevelopment;
    private DocumentRepository documentRepository;
    private String[] sections;
    private SpecificationRunnerMonitor monitor;
    private Class< ? extends InterpreterSelector> interpreterSelectorClass;
    private boolean lazy;

    public DocumentRunner() {
        monitor = new NullSpecificationRunnerMonitor();
    }

    public void setMonitor(SpecificationRunnerMonitor monitor) {
        this.monitor = monitor;
    }

    public void setSystemUnderDevelopment(SystemUnderDevelopment systemUnderDevelopment) {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    public void setInterpreterSelector(Class< ? extends InterpreterSelector> interpreterSelectorClass) {
        this.interpreterSelectorClass = interpreterSelectorClass;
    }

    public void setRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void setSections(String... sections) {
        this.sections = sections;
    }

    public void setReportGenerator(ReportGenerator generator) {
        this.reportGenerator = generator;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    @Override
    public void run(String input, String output) {
        Report report = null;
        final StopWatch total, execution;

        try {

            report = reportGenerator.openReport(output);
            monitor.testRunning(input);

            total = new StopWatch();
            total.start();

            Document document = documentRepository.loadDocument(input);
            document.setSections(sections);
            document.addFilter(new CommentTableFilter());
            document.addFilter(new SectionsTableFilter(sections));
            document.addFilter(new LivingDocTableFilter(lazy));

            execution = new StopWatch();
            execution.start();

            systemUnderDevelopment.onStartDocument(document);
            document.execute(newInterpreterSelector(systemUnderDevelopment));
            systemUnderDevelopment.onEndDocument(document);

            execution.stop();
            document.done();
            total.stop();

            document.getTimeStatistics().tally(total.getTime(), execution.getTime());

            report.generate(document);
            Statistics stats = document.getStatistics();
            monitor.testDone(stats.rightCount(), stats.wrongCount(), stats.exceptionCount(), stats.ignoredCount());
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            if (report != null) {
                report.renderException(e);
            }
            monitor.exceptionOccurred(e);
        } finally {
            closeReport(report);
        }
    }

    private void closeReport(Report report) {
        if (report == null) {
            return;
        }
        try {
            reportGenerator.closeReport(report);
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            monitor.exceptionOccurred(e);
        }
    }

    private InterpreterSelector newInterpreterSelector(SystemUnderDevelopment sud) throws SecurityException,
        NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        Constructor< ? extends InterpreterSelector> constructor = interpreterSelectorClass.getConstructor(
            SystemUnderDevelopment.class);
        return constructor.newInstance(sud);
    }

    @Override
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
}
