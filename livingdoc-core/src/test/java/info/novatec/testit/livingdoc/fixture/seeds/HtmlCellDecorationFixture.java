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

package info.novatec.testit.livingdoc.fixture.seeds;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import info.novatec.testit.livingdoc.annotation.Styles;
import info.novatec.testit.livingdoc.html.HtmlExample;
import info.novatec.testit.livingdoc.interpreter.column.ExpectedColumn;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class HtmlCellDecorationFixture {
    private HtmlExample cell = null;
    private String returned;

    public static final String GREEN_COLOR = "#AAFFAA";
    public static final String RED_COLOR = "#FFAAAA";
    public static final String GREY_COLOR = "#CCCCCC";
    public static final String YELLOW_COLOR = "#FFFFAA";

    public void expected(String expected) {
        cell = htmlCell(expected);
    }

    public void returned(String returnedValue) throws IllegalArgumentException, InvocationTargetException,
        IllegalAccessException {
        this.returned = returnedValue;

        Fixture fixture = new PlainOldFixture(this);

        ExpectedColumn column = new ExpectedColumn(fixture.check("doIt"));
        column.doCell(cell);
    }

    public String doIt() throws Exception {
        if ("error".equals(returned)) {
            throw new Exception();
        }

        return ( returned == null ) ? "" : returned.trim();
    }

    private HtmlExample htmlCell(String cellValue) {
        return new HtmlExample("", "<td>", "td", cellValue == null ? "" : cellValue, "</td>", "", new ArrayList<String>(0),
            null, null);
    }

    /* return the color of the cell by parsing a string like <td bgcolor= ...>
     * </td> */
    public String cellColor() {
        String backgroundColor = cell.getStyle(Styles.BACKGROUND_COLOR);

        if (GREEN_COLOR.compareTo(backgroundColor) == 0) {
            return "GREEN";
        }
        if (RED_COLOR.compareTo(backgroundColor) == 0) {
            return "RED";
        }
        if (GREY_COLOR.compareTo(backgroundColor) == 0) {
            return "GREY";
        }
        if (YELLOW_COLOR.compareTo(backgroundColor) == 0) {
            return "YELLOW";
        }

        return backgroundColor;
    }

    public Object cellText() throws Exception {
        if ("error".equals(returned)) {
            throw new Exception();
        }

        return cell.getContent();
    }

}
