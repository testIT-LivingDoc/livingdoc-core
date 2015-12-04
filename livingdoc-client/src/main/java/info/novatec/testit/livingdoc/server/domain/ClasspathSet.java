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
package info.novatec.testit.livingdoc.server.domain;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("serial")
public class ClasspathSet extends TreeSet<String> {

    public ClasspathSet() {
        super(new ClasspathComparator());
    }

    public ClasspathSet(Collection< ? extends String> c) {
        super(c);
    }

    public static ClasspathSet parse(String classpaths) {

        ClasspathSet newClasspaths = new ClasspathSet();

        if ( ! StringUtils.isEmpty(classpaths)) {
            String[] entries = classpaths.split("[\r\n|\n|\r]");

            for (String entry : entries) {
                if ( ! StringUtils.isEmpty(entry)) {
                    newClasspaths.add(entry.trim());
                }
            }
        }

        return newClasspaths;
    }
}
