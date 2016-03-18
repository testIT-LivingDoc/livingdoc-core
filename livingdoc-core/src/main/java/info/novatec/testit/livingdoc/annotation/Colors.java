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
package info.novatec.testit.livingdoc.annotation;

import java.awt.Color;


public final class Colors {
    public static final String YELLOW = "#FFFFAA"; // 255, 255, 170
    public static final String GREEN = "#AAFFAA"; // 170, 255, 170
    public static final String GRAY = "#CCCCCC"; // 204, 204, 204
    public static final String RED = "#FFAAAA"; // 255, 170, 170
    public static final String ORANGE = "#FFC800"; // 255, 200, 0

    public static String toRGB(Color color) {
        return new StringBuilder("#").append(formatAsHex(color.getRed()))
                .append(formatAsHex(color.getGreen()))
                .append(formatAsHex(color.getBlue()))
                .toString();
    }

    private static String formatAsHex(int value) {
        String hexaString = Integer.toString(value, 16).toUpperCase();
        if (hexaString.length() == 1) {
            return "0" + hexaString;
        }
        return hexaString;
    }

    private Colors() {
    }
}
