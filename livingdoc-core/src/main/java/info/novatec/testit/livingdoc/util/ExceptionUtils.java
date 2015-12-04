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
package info.novatec.testit.livingdoc.util;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseStackTrace;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackFrames;

import info.novatec.testit.livingdoc.LivingDoc;


/**
 * Utility methods for manipulating <code>Throwable</code> objects.
 */
public final class ExceptionUtils {
    public final static Integer FULL = Integer.MAX_VALUE;

    private ExceptionUtils() {
    }

    public static String stackTrace(Throwable t, String separator) {
        return stackTrace(t, separator, FULL);
    }

    public static String stackTrace(Throwable throwable, String separator, int depth) {

        int stackTraceDepth = depth;

        if (LivingDoc.isDebugEnabled()) {
            stackTraceDepth = FULL;
        }

        StringBuilder sw = new StringBuilder();
        // sw.append(getMessage(throwable));
        Throwable rootCause = getRootCause(throwable);
        String[] printableStacktrace = null;
        if (rootCause != null && throwable != rootCause) {
            // sw.append(separator).append("[Cause:
            // "+getRootCauseMessage(throwable)+"]");
            printableStacktrace = getRootCauseStackTrace(throwable);
        } else {
            printableStacktrace = getStackFrames(throwable);
        }
        for (int i = 0; i < printableStacktrace.length && i <= stackTraceDepth; i ++ ) {
            String element = printableStacktrace[i];
            element = element.replaceAll("\t", "");
            if (i > 0) {
                sw.append(separator);
            }
            sw.append(element);
        }
        return sw.toString();
    }
}
