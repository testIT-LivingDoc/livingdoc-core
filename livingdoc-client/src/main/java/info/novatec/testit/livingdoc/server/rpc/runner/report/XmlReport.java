/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.server.rpc.runner.report;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.util.ExceptionImposter;
import info.novatec.testit.livingdoc.util.ExceptionUtils;


@SuppressWarnings("restriction")
public class XmlReport implements Report {

    private static final String DOCUMENTS = "documents";
    private static final String GLOBAL_EXCEPTION = "global-exception";
    private static final String DOCUMENT = "document";
    private static final String STATISTICS = "statistics";
    private static final String RESULTS = "results";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String ERROR = "error";
    private static final String IGNORED = "ignored";
    private static final String SECTIONS = "sections";
    private static final String SECTION = "section";

    private static final DocumentBuilderFactory documentFactoryBuilder = DocumentBuilderFactory.newInstance();

    private Document dom;
    private Element root;
    private String name;

    public static XmlReport newInstance(String name) {
        return new XmlReport(name);
    }

    public XmlReport(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "xml";
    }

    private DocumentBuilder newDocumentBuilder() {
        try {
            return documentFactoryBuilder.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    @Override
    public void renderException(Throwable throwable) {
        createEmptyDocument();
        String msg = ExceptionUtils.stackTrace(throwable, "\n", 10);
        addTextValue(root, GLOBAL_EXCEPTION, msg, true);
    }

    private void createEmptyDocument() {
        dom = newDocumentBuilder().newDocument();
        root = dom.createElement(DOCUMENTS);
        dom.appendChild(root);
    }

    @Override
    public void generate(Execution execution) {
        createEmptyDocument();
        Element element = dom.createElement(DOCUMENT);
        root.appendChild(element);

        if (execution.getSections() != null) {
            Element sections = dom.createElement(SECTIONS);
            root.appendChild(sections);

            StringTokenizer st = new StringTokenizer(execution.getSections(), ",", false);

            while (st.hasMoreTokens()) {
                addTextValue(sections, SECTION, st.nextToken(), true);
            }
        }

        Element stats = dom.createElement(STATISTICS);
        element.appendChild(stats);

        addTextValue(element, RESULTS, execution.getResults(), true);
        addIntValue(stats, SUCCESS, execution.getSuccess());
        addIntValue(stats, FAILURE, execution.getFailures());
        addIntValue(stats, ERROR, execution.getErrors());
        addIntValue(stats, IGNORED, execution.getIgnored());
    }

    @Override
    public void printTo(Writer out) throws IOException {
        if (dom == null) {
            return;
        }

        StringWriter sw = new StringWriter();

        OutputFormat format = new OutputFormat(dom);
        format.setIndenting(true);

        XMLSerializer serializer = new XMLSerializer(sw, format);
        serializer.serialize(dom);

        out.write(sw.toString());
        out.flush();
    }

    private void addTextValue(Element parent, String tagName, String value, boolean cdata) {
        Element element = dom.createElement(tagName);
        Text eleValue = cdata ? dom.createCDATASection(value) : dom.createTextNode(value);
        element.appendChild(eleValue);
        parent.appendChild(element);
    }

    private void addIntValue(Element parent, String tagName, int value) {
        Element element = dom.createElement(tagName);
        Text eleValue = dom.createTextNode(String.valueOf(value));
        element.appendChild(eleValue);
        parent.appendChild(element);
    }
}
