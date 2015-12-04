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

import info.novatec.testit.livingdoc.util.DuckType;


public class SpecificationRunnerMonitorProxy implements SpecificationRunnerMonitor {
    private final SpecificationRunnerMonitor proxied;

    public SpecificationRunnerMonitorProxy(Object proxied) {
        if ( ! DuckType.instanceOf(SpecificationRunnerMonitor.class, proxied)) {
            throw new IllegalArgumentException("Not a SpecificationRunnerMonitor: " + proxied.getClass().getName());
        }
        this.proxied = DuckType.implement(SpecificationRunnerMonitor.class, proxied);
    }

    @Override
    public void testRunning(String location) {
        proxied.testRunning(location);
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        proxied.testDone(rightCount, wrongCount, exceptionCount, ignoreCount);
    }

    @Override
    public void exceptionOccurred(Throwable t) {
        proxied.exceptionOccurred(t);
    }
}
