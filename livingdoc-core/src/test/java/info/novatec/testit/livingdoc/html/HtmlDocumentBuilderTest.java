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

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.annotation.Colors;
import info.novatec.testit.livingdoc.annotation.Styles;
import info.novatec.testit.livingdoc.document.Document;


public class HtmlDocumentBuilderTest {
    private HtmlDocumentBuilder builder;
    private String html;
    private HtmlExample example;

    @Before
    public void setUp() {
        builder = new HtmlDocumentBuilder("td");
    }

    @Test
    public void testTagMustMatch() {
        html = "<div>html fragment</div>";
        assertNull(parse(html));
    }

    @Test
    public void testTagMustMatchWholeWord() {
        html = "< notatd >cell content</ notatd >";
        assertNull(parse(html));
    }

    private HtmlExample parse(String paramHtml) {
        return ( HtmlExample ) builder.parse(paramHtml);
    }

    @Test
    public void testContentShouldBeBodyOfHtmlTag() {
        html = "<td>cell content</td>";
        example = parse(html);
        assertEquals("cell content", example.getContent());
    }

    @Test
    public void testAttributesInTagAreAllowed() {
        html = "< td bgcolor='green'>cell content</ td >";
        example = parse(html);
        assertEquals("cell content", example.getContent());
    }

    @Test
    public void testTagCanBeNestedInOtherElement() {
        html = "<tr><td>a</td><tr>";
        example = parse(html);
        assertEquals("a", example.getContent());
    }

    @Test
    public void testBreadthTraversalIteratesThroughTags() {
        html = "<td>a</td> <td>b</td> <td>c</td>";
        example = parse(html);
        assertEquals("c", example.nextSibling().nextSibling().getContent());
    }

    @Test
    public void testTraversalEndsWithLastTag() {
        html = "<td>a</td>";
        example = parse(html);
        assertNull(example.nextSibling());
    }

    @Test
    public void testTraversalIgnoresTrailingCharacters() {
        html = "<td> </td> more follows ...";
        example = parse(html);
        assertNull(example.nextSibling());
    }

    @Test
    public void testDepthTraversalLooksForNestedTags() {
        html = "<table><tr><td>a</td></tr></table>";
        builder.useTags("table", "tr", "td");
        example = parse(html);
        assertEquals("a", example.firstChild().firstChild().getContent());
    }

    @Test
    public void testSiblingsCanBeAccessedDirectly() {
        html = "<td>a</td> <td>b</td> <td>c</td>";
        example = parse(html);
        assertEquals("c", example.at(2).getContent());
    }

    @Test
    public void testOutOfRangeAccessOfSiblingsGivesNull() {
        html = "<td>a</td>";
        example = parse(html);
        assertNull(example.at(2));
    }

    @Test
    public void testKnowsHowManySiblingsAreLeft() {
        html = "<td>a</td> <td>b</td> <td>c</td>";
        example = parse(html);
        assertEquals(3, example.remainings());
    }

    @Test
    public void testChildrenCanBeAccessedDirectly() {
        html = "<table><tr><td>a</td></tr></table>";
        builder.useTags("table", "tr", "td");
        example = parse(html);
        assertEquals("a", example.at(0, 0, 0).getContent());
    }

    @Test
    public void testOutOfRangeAccessOfChildrenGivesNull() {
        html = "<table><tr><td>a</td></tr></table>";
        builder.useTags("table", "tr", "td");
        example = parse(html);
        assertNull(example.at(1, 2));
        assertNull(example.at(0, 0, 0, 0));
    }

    @Test
    public void testOutputIncludesLeadAndTail() {
        html = "...lead <td> a </td> tail...";
        example = parse(html);
        assertEquals("...lead <td> a </td> tail...", example.toString());
    }

    @Test
    public void testOutputShouldHandleChildrenProperly() {
        html = "<table> ... <tr> ... <td>a</td> ... </tr> ... </table>";
        builder.useTags("table", "tr", "td");

        example = parse(html);
        assertEquals("<table> ... <tr> ... <td>a</td> ... </tr> ... </table>", example.toString());
    }

    @Test
    public void testOutputShouldHandleSiblingsProperly() {
        html = "<tr><td>a</td><td>b</td></tr>";
        builder.useTags("tr", "td");
        example = parse(html);
        assertEquals("<tr><td>a</td><td>b</td></tr>", example.toString());
    }

    @Test
    public void testBackgroundColorCanBeChanged() {
        html = "<td>a</td>";
        example = parse(html);
        example.setStyle("background-color", Colors.GREEN);
        assertEquals("<td style=\"background-color: #AAFFAA;\">a</td>", example.toString());
    }

    @Test
    public void testContentCanBeReplaced() {
        html = "<td>old content</td>";
        example = parse(html);
        example.setContent("new content");
        assertEquals("<td>new content</td>", example.toString());
    }

    @Test
    public void testShouldPreserveExistingAttributes() {
        html = "<td colspan='2'></td>";
        example = parse(html);
        example.setStyle(Styles.BACKGROUND_COLOR, Colors.GRAY);
        example.setContent("a");
        assertEquals("<td colspan='2' style=\"background-color: #CCCCCC;\">a</td>", example.toString());
    }

    @Test
    public void testCondensesWhitespaceInText() {
        html = "<td>-\240\240   &nbsp;&nbsp;-</td>";
        example = parse(html);
        assertEquals("- -", example.getContent());
    }

    @Test
    public void testRemovesLeadingAndTrailingWhitespace() {
        html = "<td>  --  </td>";
        example = parse(html);
        assertEquals("--", example.getContent());
    }

    @Test
    public void testConvertsSpecificHtmlEntitiesIntoCharacters() {
        html = "<td>&amp;&lt;&nbsp;&gt;&quot;&apos;&#45;&#42;</td>";
        example = parse(html);
        assertEquals("&< >\"'-*", example.getContent());
    }

    @Test
    public void testConvertsLineBreaksAndParagraphsToLineFeeds() {
        html = "<td>..<br>..<br/>..<br />..<  br  /  >..<br class='with-style'>..</td>";
        example = parse(html);
        assertEquals("..\n..\n..\n..\n..\n..", example.getContent());
    }

    @Test
    public void testOtherHtmlMarkupIsRemoved() {
        html = "<td> <span class='result'> resulting <span/> <b> cell </b> content </td>";
        example = parse(html);
        assertEquals("resulting cell content", example.getContent());
    }

    @Test
    public void testHtmlCommentsAreRemoved() {
        html = "<!--<td>this cell should be removed</td>--><td>this cell has been kept</td>";
        example = parse(html);
        assertEquals("this cell has been kept", example.getContent());
    }

    @Test
    public void testMultipleTagsPerLevelShouldBeSupported() {
        html = "<ul><li>item</li></ul>";
        builder.useTags("table ul", "tr li");
        example = parse(html);
        assertEquals("item", example.at(0, 0).getContent());
    }

    @Test
    public void testTagsOfSameLevelCanBeMixedInHtml() {
        html = "<table><tr><td>cell</td></tr></table>" + "<ul><li>item</li></ul>";
        builder.useTags("table ul", "tr li", "td");
        example = parse(html);
        assertEquals("cell", example.at(0, 0, 0).getContent());
        assertEquals("item", example.at(1, 0).getContent());
    }

    @Test
    public void testStartAndEndTagShouldMatch() {
        html = "not <tr>a valid tag</li>";
        example = ( HtmlExample ) builder.useTags("tr li th").parse(html);
        assertNull(example);
    }

    @Test
    public void testValidConstructsCanBeEnforcedUsingDescendantSelectors() {
        html = "<table><tr>valid</tr><li>invalid</li></table>";
        builder.useTags("table ol", "table>tr ol>li", "td");
        example = parse(html);
        assertTrue(example.hasChild());
        assertFalse(example.firstChild().hasSibling());
    }

    @Test
    public void testYoungestSiblingShouldBeLastOfFamily() {
        html = "<td>1</td><td>2</td>";
        example = parse(html);
        example.addSibling();
        assertEquals("<td>1</td><td>2</td><td></td>", example.toString());
    }

    @Test
    public void testYoungestChildShouldBeLastOfChildren() {
        html = "<tr><td>1</td></tr>";
        builder.useTags("tr", "td");
        example = parse(html);
        example.addChild();
        assertEquals("<tr><td>1</td><td></td></tr>", example.toString());
    }

    @Test
    public void testFirstBornChildUsesFirstAvailableChildTag() {
        html = "<table></table>";
        builder.useTags("table", "tr th", "td");
        example = parse(html);
        example.addChild().addChild();
        assertEquals("<table><tr><td></td></tr></table>", example.toString());
    }

    @Test
    public void testFiltersCanBeRegisteredToModifyElementContent() {
        builder.addFilter(new BulletListFilter()).useTags("li", "span");
        example = parse("<li>content</li>");
        assertTrue(example.hasChild());
        assertThat(example.firstChild().getContent(), containsString("content"));
    }

    @Test
    public void testTagsCanHaveAnyCaseEvenWhenUsingDescendantSelectors() {
        html = "<Table><TR><Td>cell content</tD></tr></TABLE>";
        example = ( HtmlExample ) HtmlDocumentBuilder.tablesAndLists().parse(html);
        assertNotNull(example.at(0, 0, 0));
        assertEquals("cell content", example.at(0, 0, 0).getContent());
    }

    @Test(timeout = 500)
    public void testThatParserDoesNotHangOnBigDocumentWithoutTables() throws Exception {
        html = junk();
        example = ( HtmlExample ) HtmlDocumentBuilder.tablesAndLists().parse(html);
    }

    @Test
    public void testThatNameAndExternalLinkAreNullWhenNoMetaTag() throws Exception {
        StringReader reader = new StringReader(" ");
        Document document = HtmlDocumentBuilder.tablesAndLists().build(reader);
        assertNull(document.getName());
        assertNull(document.getExternalLink());
    }

    @Test
    public void testThatWeCanRetrieveTheNameUsingTheMetaTag() throws Exception {
        StringReader reader = new StringReader("<head><meta name=\"title\" content=\"a title\"/>");
        Document document = HtmlDocumentBuilder.tablesAndLists().build(reader);
        assertNotNull(document.getName());
        assertEquals("a title", document.getName());
    }

    @Test
    public void testThatWeCanRetrieveTheExternalLinkUsingTheMetaTag() throws Exception {
        StringReader reader = new StringReader(
            "<head><meta name=\"external-link\" content=\"http://myserver/confluence/space/Spec\"/>");
        Document document = HtmlDocumentBuilder.tablesAndLists().build(reader);
        assertNotNull(document.getExternalLink());
        assertEquals("http://myserver/confluence/space/Spec", document.getExternalLink());
    }

    private String junk() {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 1000; i ++ ) {
            buf.append("Lorem Lipsum");
        }

        return buf.toString();
    }
}
