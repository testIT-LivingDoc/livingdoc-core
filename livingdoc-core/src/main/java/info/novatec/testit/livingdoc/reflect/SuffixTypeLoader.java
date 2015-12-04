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

package info.novatec.testit.livingdoc.reflect;

import java.util.ArrayList;
import java.util.List;


public class SuffixTypeLoader<T> implements TypeLoader<T> {
    private final TypeLoader<T> parent;
    private final List<String> suffixes = new ArrayList<String>();

    public SuffixTypeLoader(TypeLoader<T> parent) {
        this.parent = parent;
        addSuffix("");
    }

    @Override
    public void searchPackage(String prefix) {
        parent.searchPackage(prefix);
    }

    @Override
    public void addSuffix(String suffix) {
        suffixes.add(0, suffix);
    }

    public void addSuffixes(String... paramSuffixes) {
        for (String suffix : paramSuffixes) {
            addSuffix(suffix);
        }
    }

    @Override
    public Type<T> loadType(String name) {
        for (String suffix : suffixes) {
            Type<T> type = parent.loadType(name + suffix);
            if (type != null) {
                return type;
            }
        }
        return null;
    }
}
