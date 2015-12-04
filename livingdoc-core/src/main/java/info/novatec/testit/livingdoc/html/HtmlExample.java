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

package info.novatec.testit.livingdoc.html;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import info.novatec.testit.livingdoc.AbstractExample;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Text;
import info.novatec.testit.livingdoc.annotation.Annotation;
import info.novatec.testit.livingdoc.util.CollectionUtil;


public class HtmlExample extends AbstractExample implements Text {
    private String lead;
    private String startTag;
    private String text;
    private String endTag;
    private String tail;
    private String tag;
    private List<String> childTags;
    private Example sibling;
    private Example child;

    private final Map<String, String> styles = new HashMap<String, String>();

    public HtmlExample(String lead, String startTag, String tag, String content, String endTag, String tail,
        List<String> childTags, Example child, Example sibling) {
        this.tag = tag;
        this.tail = tail;
        this.endTag = endTag;
        this.startTag = startTag;
        this.lead = lead;
        this.text = content;
        this.childTags = childTags;
        this.child = child;
        this.sibling = sibling;
    }

    @Override
    public Example firstChild() {
        return child;
    }

    @Override
    public Example nextSibling() {
        return sibling;
    }

    @Override
    public void print(PrintWriter out) {
        out.write(lead);
        printStartTag(out);
        if (child != null) {
            child.print(out);
        } else {
            out.write(text);
        }
        out.write(endTag);
        if (sibling != null) {
            sibling.print(out);
        } else {
            out.write(tail);
        }
    }

    // Should we return null or empty string when we have children?
    @Override
    public String getContent() {
        String content = normalizeLineBreaks(text);
        content = removeNonLineBreaks(content);
        content = condenseWhitespace(content);
        content = decodeMarkup(content);
        return content.trim();
    }

    private String firstPattern(String tags) {
        Scanner scanner = new Scanner(tags);
        String next = scanner.next();
        scanner.close();
        return next;
    }

    private HtmlExample createSpecification(String paramTag, List<String> moreTags) {
        return new HtmlExample("", start(paramTag), paramTag, "", end(paramTag), "", moreTags, null, null);
    }

    private String start(String paramTag) {
        return String.format("<%s>", paramTag);
    }

    private String end(String paramTag) {
        return String.format("</%s>", paramTag);
    }

    @Override
    public Example addChild() {
        if (hasChild()) {
            return child.addSibling();
        }
        if (childTags.isEmpty()) {
            throw new IllegalStateException("No child tag");
        }
        List<String> moreTags = new ArrayList<String>(childTags);
        String childTag = firstPattern(CollectionUtil.shift(moreTags));
        child = createSpecification(childTag, moreTags);
        return child;
    }

    @Override
    public Example addSibling() {
        if (hasSibling()) {
            return sibling.addSibling();
        }
        sibling = createSpecification(tag, childTags);
        return sibling;
    }

    private String condenseWhitespace(String s) {
        // non breaking space is decimal character 160 (hex A0)
        return s.replace(( char ) 160, ' ').replaceAll("&nbsp;", " ").replaceAll("\\s+", " ");
    }

    private String decodeMarkup(String s) {
        return new HtmlEntitiesDecoder(s).decode();
    }

    @Override
    public void annotate(Annotation annotation) {
        annotation.writeDown(this);
    }

    private String normalizeLineBreaks(String s) {
        return s.replaceAll("<\\s*br(\\s+.*?)*>", "<br/>");
    }

    private void printStartTag(PrintWriter out) {
        out.write(startTag.substring(0, startTag.length() - 1));
        if ( ! styles.isEmpty()) {
            out.write(String.format(" style=\"%s\"", inlineStyle()));
        }
        out.write(">");
    }

    private String inlineStyle() {
        StringBuilder style = new StringBuilder();
        for (String attr : styles.keySet()) {
            style.append(String.format("%s: %s;", attr, styles.get(attr)));
        }
        return style.toString();
    }

    private String removeNonLineBreaks(String s) {
        return s.replaceAll("<" + not("br/>") + ">", "");
    }

    private String not(String regex) {
        return String.format("(?!%s).*?", regex);
    }

    @Override
    public void setStyle(String property, String value) {
        styles.put(property, value);
    }

    @Override
    public String getStyle(String property) {
        return styles.get(property);
    }

    @Override
    public void setContent(String content) {
        text = content;
    }
}
