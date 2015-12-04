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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.novatec.testit.livingdoc.Example;


public final class ExampleUtil {
    private ExampleUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Example> T[] asArray(T example) {
        List<Example> examples = asList(example);
        T[] array = ( T[] ) Array.newInstance(example.getClass(), examples.size());
        return examples.toArray(array);
    }

    public static List<Example> asList(Example example) {
        if (example == null) {
            return new ArrayList<Example>();
        }

        List<Example> all = new ArrayList<Example>();
        for (Example each : example) {
            all.add(each);
        }
        return all;
    }

    public static String[] content(Iterable<Example> cells) {
        if (cells == null) {
            return new String[0];
        }

        final List<String> values = new ArrayList<String>();
        for (Example cell : cells) {
            values.add(cell.getContent());
        }

        return values.toArray(new String[values.size()]);
    }

    public static List<String> contentAsList(Iterable<Example> cells) {
        return new ArrayList<String>(Arrays.asList(content(cells)));
    }

    public static String asString(Example example) {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        example.print(writer);
        return sw.toString();
    }

    public static String contentOf(Example cell) {
        return cell != null ? cell.getContent() : null;
    }
}
