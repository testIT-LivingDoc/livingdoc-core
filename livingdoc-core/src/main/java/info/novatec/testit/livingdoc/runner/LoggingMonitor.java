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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Statistics;


public class LoggingMonitor implements SpecificationRunnerMonitor {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingMonitor.class);

    @Override
    public void testRunning(String location) {
        LOG.info("Running {}", location);
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        Statistics stats = new Statistics(rightCount, wrongCount, exceptionCount, ignoreCount);
        LOG.info(stats.toString() + ( stats.indicatesFailure() ? " <<< FAILURE! " : "" ));
    }

    @Override
    public void exceptionOccurred(Throwable t) {
        LOG.error(LOG_ERROR, t);
        Throwable cause = t.getCause();
        if (cause != null) {
            LOG.error(cause.getMessage(), cause);
        }
    }
}
