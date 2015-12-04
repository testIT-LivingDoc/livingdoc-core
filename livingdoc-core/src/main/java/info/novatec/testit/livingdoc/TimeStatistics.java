/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc;

import java.io.Serializable;


public class TimeStatistics implements Serializable {
    private static final long serialVersionUID = - 1L;

    private long total;
    private long execution;

    public TimeStatistics() {
        this(0, 0);
    }

    public TimeStatistics(long total, long execution) {
        this.total = total;
        this.execution = execution;
    }

    public long getTotal() {
        return total;
    }

    public long getExecution() {
        return execution;
    }

    public void tally(TimeStatistics other) {
        tally(other.getTotal(), other.getExecution());
    }

    public void tally(long paramTotal, long paramExecution) {
        this.total += paramTotal;
        this.execution += paramExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ( ! ( o instanceof TimeStatistics )) {
            return false;
        }

        TimeStatistics that = ( TimeStatistics ) o;
        return total == that.total && execution == that.execution;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( int ) ( execution ^ ( execution >>> 32 ) );
        result = prime * result + ( int ) ( total ^ ( total >>> 32 ) );
        return result;
    }

    @Override
    public String toString() {
        return String.format("total time: %d ms, execution-time: %d ms", getTotal(), getExecution());
    }
}
