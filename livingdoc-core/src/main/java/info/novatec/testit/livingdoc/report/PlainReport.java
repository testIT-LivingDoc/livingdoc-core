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

package info.novatec.testit.livingdoc.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import info.novatec.testit.livingdoc.TextExample;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.util.ExceptionUtils;


public class PlainReport implements Report {
    private final String name;
    private Document document;

    public static PlainReport newInstance(String name) {
        return new PlainReport(name);
    }

    public PlainReport(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return document != null ? document.getType() : null;
    }

    @Override
    public void printTo(Writer writer) throws IOException {
        if (document == null) {
            return;
        }
        document.print(new PrintWriter(writer));
    }

    @Override
    public void renderException(Throwable t) {
        document = Document.text(new TextExample(ExceptionUtils.stackTrace(t, "\n")));
    }

    @Override
    public void generate(Document doc) {
        this.document = doc;
    }
}
