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

package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.interpreter.column.Column;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.interpreter.column.GivenColumn;
import info.novatec.testit.livingdoc.interpreter.column.NullColumn;
import info.novatec.testit.livingdoc.interpreter.column.RecalledColumn;
import info.novatec.testit.livingdoc.interpreter.column.SavedColumn;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;


public final class HeaderForm {
    private static final Logger LOG = LoggerFactory.getLogger(HeaderForm.class);

    private final String text;

    public static HeaderForm parse(String text) {
        return new HeaderForm(text);
    }

    private HeaderForm(String text) {
        this.text = text;
    }

    public boolean isGiven() {
        LOG.debug(ENTRY);
        boolean isGiven = ! isExpected() && ! isSaved();
        LOG.debug(EXIT_WITH, isGiven);
        return isGiven;
    }

    public boolean isExpected() {
        LOG.debug(ENTRY);
        boolean isExpected = header().endsWith("()") || header().endsWith("?");
        LOG.debug(EXIT_WITH, isExpected);
        return isExpected;
    }

    private String header() {
        LOG.trace(ENTRY);
        String header = text.trim();
        LOG.trace(EXIT_WITH, header);
        return header;
    }

    public boolean isSaved() {
        LOG.debug(ENTRY);
        boolean isSaved = header().endsWith("=");
        LOG.debug(EXIT_WITH, isSaved);
        return isSaved;
    }

    public boolean isRecalled() {
        LOG.debug(ENTRY);
        boolean isRecalled = header().startsWith("=");
        LOG.debug(EXIT_WITH, isRecalled);
        return isRecalled;
    }

    public String message() {
        LOG.debug(ENTRY);
        String message = header().replaceAll("=", "").replaceAll("\\?", "").replaceAll("\\(\\)", "");
        LOG.debug(EXIT_WITH, message);
        return message;
    }

    public Column selectColumn(Fixture fixture) throws NoSuchMessageException {
        LOG.debug(ENTRY_WITH, fixture.toString());
        if (isNull()) {
            NullColumn nullColumn = new NullColumn();
            LOG.debug(EXIT_WITH, nullColumn.toString());
            return nullColumn;
        }

        if (isSaved()) {
            SavedColumn savedColumn = new SavedColumn(fixture.check(message()));
            LOG.debug(EXIT_WITH, savedColumn.toString());
            return savedColumn;
        }

        if (isExpected()) {
            ExpectedColumn expectedColumn = new ExpectedColumn(fixture.check(message()));
            LOG.debug(EXIT_WITH, expectedColumn.toString());
            return expectedColumn;
        }
        if (isRecalled()) {
            RecalledColumn recalledColumn = new RecalledColumn(fixture.send(message()));
            LOG.debug(EXIT_WITH, recalledColumn.toString());
            return recalledColumn;
        }

        GivenColumn givenColumn = new GivenColumn(fixture.send(message()));
        LOG.debug(EXIT_WITH, givenColumn.toString());
        return givenColumn;
    }

    public boolean isNull() {
        LOG.debug(ENTRY);
        boolean textIsNull = StringUtils.isBlank(text);
        LOG.debug(EXIT_WITH, textIsNull);
        return textIsNull;
    }
}
