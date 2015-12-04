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

import java.util.Iterator;

import info.novatec.testit.livingdoc.util.ExampleUtil;


/**
 * Abstract base class for <code>Specification</code> implementations. It
 * provides default implementations for some methods.
 */
public abstract class AbstractExample implements Example {
    @Override
    public Iterator<Example> iterator() {
        return new Iterator<Example>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return at(index) != null;
            }

            @Override
            public Example next() {
                return at(index ++ );
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Specification not mutable");
            }
        };
    }

    @Override
    public boolean hasSibling() {
        return nextSibling() != null;
    }

    @Override
    public boolean hasChild() {
        return firstChild() != null;
    }

    @Override
    public Example at(int i) {
        if (i == 0) {
            return this;
        }
        return hasSibling() ? nextSibling().at(i - 1) : null;
    }

    @Override
    public Example at(int i, int... indexes) {
        Example at = at(i);
        for (int j : indexes) {
            if (at == null || ! at.hasChild()) {
                return null;
            }
            at = at.firstChild().at(j);
        }
        return at;
    }

    @Override
    public int remainings() {
        return hasSibling() ? nextSibling().remainings() + 1 : 1;
    }

    @Override
    public String toString() {
        return ExampleUtil.asString(this);
    }

    @Override
    public Example lastSibling() {
        return at(remainings() - 1);
    }
}
