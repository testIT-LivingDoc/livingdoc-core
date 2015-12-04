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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CompositeSpecificationRunnerMonitor implements SpecificationRunnerMonitor {
    private final List<SpecificationRunnerMonitor> monitors;

    public CompositeSpecificationRunnerMonitor(SpecificationRunnerMonitor... monitors) {
        this(Arrays.asList(monitors));
    }

    public CompositeSpecificationRunnerMonitor(List<SpecificationRunnerMonitor> monitors) {
        this.monitors = new ArrayList<SpecificationRunnerMonitor>();
        this.monitors.addAll(monitors);
    }

    public CompositeSpecificationRunnerMonitor() {
        this(Collections.<SpecificationRunnerMonitor> emptyList());
    }

    public void add(SpecificationRunnerMonitor monitor) {
        monitors.add(monitor);
    }

    @Override
    public void testRunning(String location) {
        for (SpecificationRunnerMonitor monitor : monitors) {
            monitor.testRunning(location);
        }
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        for (SpecificationRunnerMonitor monitor : monitors) {
            monitor.testDone(rightCount, wrongCount, exceptionCount, ignoreCount);
        }
    }

    @Override
    public void exceptionOccurred(Throwable t) {
        for (SpecificationRunnerMonitor monitor : monitors) {
            monitor.exceptionOccurred(t);
        }
    }
}
