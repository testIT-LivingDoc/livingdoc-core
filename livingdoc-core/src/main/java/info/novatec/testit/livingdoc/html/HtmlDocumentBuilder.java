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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.TextExample;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.repository.DocumentBuilder;
import info.novatec.testit.livingdoc.util.CollectionUtil;


public class HtmlDocumentBuilder implements DocumentBuilder {
    private final List<String> tags;
    private final List<HtmlContentFilter> filters = new ArrayList<HtmlContentFilter>();

    public static HtmlDocumentBuilder tables() {
        return new HtmlDocumentBuilder("table", "tr", "td th");
    }

    public static HtmlDocumentBuilder tablesAndLists() {
        return new HtmlDocumentBuilder("table ul ol", "table>tr ol>li ul>li", "tr>td tr>th li>span li>b li>i li>u li>em")
            .addFilter(new BulletListFilter());
    }

    public HtmlDocumentBuilder(String... tags) {
        this(Arrays.asList(tags));
    }

    public HtmlDocumentBuilder(List<String> tags) {
        if (tags.isEmpty()) {
            throw new IllegalArgumentException("Specify at least a tag");
        }
        this.tags = new ArrayList<String>(tags);
    }

    @Override
    public Document build(Reader reader) throws IOException {
        String html = IOUtils.toString(reader);
        Example example = parse(html);
        if (example == null) {
            example = new TextExample(html);
        }
        return Document.html(example, name(html), externalLink(html));
    }

    public Example parse(String html) {
        String text = removeComments(html);

        return doParse(text);
    }

    private Example doParse(String text) {

        if (pathologicalCase(text)) {
            return null;
        }

        Matcher match = match(text);
        if ( ! match.find()) {
            return null;
        }

        Example child = hasChild(match) ? childParser(match).doParse(content(match)) : null;
        Example sibling = hasSibling(match) ? doParse(tail(match)) : null;

        return new HtmlExample(lead(match), startTag(match), tag(match), content(match), endTag(match), tail(match),
            childTags(match), child, sibling);
    }

    /**
     * Most regex implementations today do not build a DFA / NFA -- especially
     * those that offer backreferences (which are not "regular" at all).
     * 
     * And because they do NOT build DFAs and NFAs, it's very simple to
     * construct pathological cases - e.g., "((a*)(a*))+b" can take
     * exponentially long to decide that aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaac is
     * not in the language using the matching techniques commonly in use.
     * 
     * Ori Berger
     * 
     * @param text The text to check
     * @return boolean true, if the text matches pathological case, else false
     */
    private boolean pathologicalCase(String text) {
        String tag = toRegex(CollectionUtil.first(tags));
        String regex = String.format("(?is)(<\\s*(%s)\\s*.*?>)", tag);
        Matcher match = compile(regex).matcher(text);

        if (match.find()) {
            regex = String.format("(?is)(.*?)(<\\s*/\\s*(%s)\\s*.*?>)", tag);
            return ! compile(regex).matcher(text).find(match.end());
        }

        return true;
    }

    private Matcher match(String text) {
        return compile(firstTag(tags)).matcher(text);
    }

    private boolean hasSibling(Matcher match) {
        return tail(match).matches(firstTag(tags));
    }

    private HtmlDocumentBuilder childParser(Matcher match) {
        return new HtmlDocumentBuilder(childTags(match)).addAllFilters(filters);
    }

    private boolean hasChild(Matcher match) {
        return ! childTags(match).isEmpty() && content(match).matches(firstTag(childTags(match)));
    }

    private List<String> childTags(Matcher matcher) {
        List<String> moreTags = new ArrayList<String>();
        for (int i = 1; i < tags.size(); i ++ ) {
            moreTags.add(stripSelector(tag(matcher), tags.get(i)));
        }
        return moreTags;
    }

    private String firstTag(List<String> listTags) {
        return elementPattern(toRegex(CollectionUtil.first(listTags)));
    }

    private String tail(Matcher matcher) {
        return matcher.group(6);
    }

    private String endTag(Matcher matcher) {
        return matcher.group(5);
    }

    private String content(Matcher matcher) {
        String content = matcher.group(4);
        for (int i = filters.size() - 1; i >= 0; i -- ) {
            HtmlContentFilter filter = filters.get(i);
            if (filter.handles(tag(matcher))) {
                return filter.process(content);
            }
        }
        return content;
    }

    private String tag(Matcher matcher) {
        return matcher.group(3).toLowerCase();
    }

    private String startTag(Matcher matcher) {
        return matcher.group(2);
    }

    private String lead(Matcher matcher) {
        return matcher.group(1);
    }

    private Pattern compile(String regex) {
        return Pattern.compile(regex);
    }

    private String removeComments(String html) {
        return html.replaceAll("(?is)<!--(.*?)-->", "");
    }

    private String stripSelector(String selector, String tag) {
        return tag.replaceAll(selector + ">", "");
    }

    private String elementPattern(String t) {
        return String.format("(?is)(.*?)(<\\s*(%s)\\s*.*?>)(.*?)(<\\s*/\\s*\\3\\s*>)(.*)", t);
    }

    private String toRegex(String tag) {
        return tag.replaceAll(" ", "|");
    }

    private String name(String content) {
        return meta(content, "title");
    }

    private String externalLink(String content) {
        return meta(content, "external-link");
    }

    private String meta(String content, String name) {
        Matcher matcher = compile(String.format("<meta name=\"%s\" content=\"(.*)\"/>", name)).matcher(content);

        return matcher.find() ? matcher.group(1) : null;
    }

    public HtmlDocumentBuilder useTags(String... paramTags) {
        this.tags.clear();
        this.tags.addAll(Arrays.asList(paramTags));
        return this;
    }

    public HtmlDocumentBuilder addAllFilters(List<HtmlContentFilter> listFilters) {
        this.filters.addAll(listFilters);
        return this;
    }

    public HtmlDocumentBuilder addFilter(HtmlContentFilter... filter) {
        return addAllFilters(Arrays.asList(filter));
    }
}
