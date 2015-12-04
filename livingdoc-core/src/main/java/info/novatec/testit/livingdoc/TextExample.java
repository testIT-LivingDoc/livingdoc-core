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

import java.io.PrintWriter;

import info.novatec.testit.livingdoc.annotation.Annotation;


public class TextExample extends AbstractExample {
    private final String text;

    public TextExample(String text) {
        super();

        this.text = text;
    }

    @Override
    public Example firstChild() {
        return null;
    }

    @Override
    public Example nextSibling() {
        return null;
    }

    @Override
    public void print(PrintWriter out) {
        out.write(text);
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void annotate(Annotation annotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Example addSibling() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Example addChild() {
        throw new UnsupportedOperationException();
    }
}
