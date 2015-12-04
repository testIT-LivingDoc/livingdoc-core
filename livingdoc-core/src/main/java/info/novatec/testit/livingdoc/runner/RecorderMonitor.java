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

import info.novatec.testit.livingdoc.Statistics;


public class RecorderMonitor implements SpecificationRunnerMonitor {
    private Statistics statistics = new Statistics();
    private Throwable exception;
    private int locationCount = 0;

    @Override
    public void testRunning(String location) {
        locationCount ++ ;
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        Statistics stats = new Statistics(rightCount, wrongCount, exceptionCount, ignoreCount);
        statistics.tally(stats);
    }

    @Override
    public void exceptionOccurred(Throwable t) {
        exception = t;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public boolean hasTestFailures() {
        return statistics.indicatesFailure();
    }

    public int getLocationCount() {
        return locationCount;
    }
}
