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

package info.novatec.testit.livingdoc.maven.plugin;

import org.apache.maven.plugin.logging.Log;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.runner.SpecificationRunnerMonitor;


public class LoggerMonitor implements SpecificationRunnerMonitor {
    private Log log;

    public LoggerMonitor(Log log) {
        this.log = log;
    }

    @Override
    public void testRunning(String name) {
        String logMsg = "Running " + name;
        log.info(logMsg);
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        Statistics stats = new Statistics(rightCount, wrongCount, exceptionCount, ignoreCount);
        StringBuilder logMsg = new StringBuilder(stats.toString());
        if (stats.hasFailed()) {
            logMsg.append(" <<< FAILURE! ");
        }
        log.info(logMsg.toString());
    }

    @Override
    public void exceptionOccurred(Throwable t) {
        log.error("Error running specification", t);
    }
}
