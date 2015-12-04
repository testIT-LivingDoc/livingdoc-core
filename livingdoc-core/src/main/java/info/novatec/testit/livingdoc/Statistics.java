/* Copyright (c) 2006 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc;

import java.io.Serializable;


public final class Statistics implements Serializable {
    private static final long serialVersionUID = - 1L;

    private int rightCount;
    private int wrongCount;
    private int exceptionCount;

    private int ignoredCount;

    public Statistics() {
        this(0, 0, 0, 0);
    }

    public Statistics(int right, int wrong, int exception, int ignored) {
        this.rightCount = right;
        this.wrongCount = wrong;
        this.exceptionCount = exception;
        this.ignoredCount = ignored;
    }

    public int exceptionCount() {
        return exceptionCount;
    }

    public int wrongCount() {
        return wrongCount;
    }

    public int ignoredCount() {
        return ignoredCount;
    }

    public int rightCount() {
        return rightCount;
    }

    public void tally(Statistics other) {
        rightCount += other.rightCount();
        wrongCount += other.wrongCount();
        ignoredCount += other.ignoredCount();
        exceptionCount += other.exceptionCount();
    }

    public boolean indicatesFailure() {
        return wrongCount > 0 || exceptionCount > 0;
    }

    public int totalCount() {
        return rightCount() + wrongCount() + exceptionCount() + ignoredCount();
    }

    @Override
    public String toString() {
        return String.format("%d tests: %d right, %d wrong, %d ignored, %d exception(s)", totalCount(), rightCount(),
            wrongCount(), ignoredCount(), exceptionCount());
    }

    public void right() {
        rightCount ++ ;
    }

    public void wrong() {
        wrongCount ++ ;
    }

    public void exception() {
        exceptionCount ++ ;
    }

    public void ignored() {
        ignoredCount ++ ;
    }

    public boolean hasFailed() {
        return wrongCount() > 0 || exceptionCount() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ( ! ( o instanceof Statistics )) {
            return false;
        }

        Statistics that = ( Statistics ) o;

        return exceptionCount == that.exceptionCount && ignoredCount == that.ignoredCount && rightCount == that.rightCount
            && wrongCount == that.wrongCount;
    }

    @Override
    public int hashCode() {
        int result;
        result = rightCount;
        result = 31 * result + wrongCount;
        result = 31 * result + exceptionCount;
        result = 31 * result + ignoredCount;
        return result;
    }
}
