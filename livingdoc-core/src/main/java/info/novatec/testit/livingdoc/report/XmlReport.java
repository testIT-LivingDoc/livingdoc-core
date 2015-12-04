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

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.TimeStatistics;
import info.novatec.testit.livingdoc.util.ExceptionImposter;
import info.novatec.testit.livingdoc.util.ExceptionUtils;


public class XmlReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(XmlReport.class);

    private static final String DOCUMENTS = "documents";
    private static final String GLOBAL_EXCEPTION = "global-exception";
    private static final String DOCUMENT = "document";
    private static final String DOCUMENT_NAME = "name";
    private static final String DOCUMENT_EXTERNAL_LINK = "external-link";
    private static final String STATISTICS = "statistics";
    private static final String TIME = "time-statistics";
    private static final String EXECUTION_TIME = "execution";
    private static final String TOTAL_TIME = "total";
    private static final String RESULTS = "results";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String ERROR = "error";
    private static final String IGNORED = "ignored";
    private static final String SECTIONS = "sections";
    private static final String SECTION = "section";

    private static final String ANNOTATION = "annotation";

    private static final DocumentBuilderFactory documentFactoryBuilder = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

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
            LOG.error(LOG_ERROR, e);
            throw ExceptionImposter.imposterize(e);
        }
    }

    private XmlReport(InputSource source) throws SAXException, IOException {
        source.setEncoding("UTF-8");
        dom = newDocumentBuilder().parse(source);
        root = dom.getDocumentElement();
    }

    public static XmlReport parse(String content) throws SAXException, IOException {
        return parse(new StringReader(content));
    }

    public static XmlReport parse(Reader in) throws SAXException, IOException {
        return parse(new InputSource(in));
    }

    public static XmlReport parse(InputStream is) throws SAXException, IOException {
        return parse(new InputSource(is));
    }

    public static XmlReport parse(File file) throws SAXException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInputStream, "UTF-8");

        try {
            return parse(reader);
        } finally {
            IOUtils.closeQuietly(reader);
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
    public void generate(info.novatec.testit.livingdoc.document.Document document) {
        createEmptyDocument();
        Statistics compiler = document.getStatistics();
        Element element = dom.createElement(DOCUMENT);
        root.appendChild(element);

        if ( ! StringUtils.isEmpty(document.getName())) {
            addTextValue(element, DOCUMENT_NAME, document.getName(), true);
        }

        if ( ! StringUtils.isEmpty(document.getExternalLink())) {
            addTextValue(element, DOCUMENT_EXTERNAL_LINK, document.getExternalLink(), true);
        }

        if (document.getSections() != null && document.getSections().length > 0) {
            Element sections = dom.createElement(SECTIONS);
            root.appendChild(sections);

            for (String section : document.getSections()) {
                addTextValue(sections, SECTION, section, true);
            }
        }

        Element time = dom.createElement(TIME);
        element.appendChild(time);

        Element stats = dom.createElement(STATISTICS);
        element.appendChild(stats);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        document.print(writer);
        writer.flush();

        addNumberValue(time, EXECUTION_TIME, document.getTimeStatistics().getExecution());
        addNumberValue(time, TOTAL_TIME, document.getTimeStatistics().getTotal());
        addTextValue(element, RESULTS, buffer.toString(), true);
        addNumberValue(stats, SUCCESS, compiler.rightCount());
        addNumberValue(stats, FAILURE, compiler.wrongCount());
        addNumberValue(stats, ERROR, compiler.exceptionCount());
        addNumberValue(stats, IGNORED, compiler.ignoredCount());
    }

    public String getResults(int index) {
        return getTextValue(RESULTS, index);
    }

    public String getGlobalException() {
        return getTextValue(GLOBAL_EXCEPTION, 0);
    }

    public int getSuccess(int index) {
        return getIntValue(SUCCESS, index);
    }

    public int getFailure(int index) {
        return getIntValue(FAILURE, index);
    }

    public int getError(int index) {
        return getIntValue(ERROR, index);
    }

    public int getIgnored(int index) {
        return getIntValue(IGNORED, index);
    }

    public int getAnnotation(int index) {
        return getIntValue(ANNOTATION, index);
    }

    public String getSections(int index) {
        return getTextValue(SECTION, index);
    }

    public long getExecutionTime(int index) {
        return getLongValue(EXECUTION_TIME, index);
    }

    public long getTotalTime(int index) {
        return getLongValue(TOTAL_TIME, index);
    }

    public String getDocumentName(int index) {
        return getTextValue(DOCUMENT_NAME, index);
    }

    public String getDocumentExternalLink(int index) {
        return getTextValue(DOCUMENT_EXTERNAL_LINK, index);
    }

    @Override
    public void printTo(Writer out) throws IOException {
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(dom), new StreamResult(out));
        } catch (TransformerException ex) {
            LOG.error(LOG_ERROR, ex);
            throw new IOException(ex.getMessage());
        }
    }

    public static XmlReport parse(InputSource source) throws SAXException, IOException {
        return new XmlReport(source);
    }

    public static String toXml(info.novatec.testit.livingdoc.document.Document document) throws IOException {
        StringWriter sw = new StringWriter();

        XmlReport xmlReport = XmlReport.newInstance("");
        xmlReport.generate(document);
        xmlReport.printTo(sw);

        return sw.toString();
    }

    public Statistics toStatistics() {
        return new Statistics(getSuccess(0), getFailure(0), getError(0), getIgnored(0));
    }

    public TimeStatistics toTimeStatistics() {
        return new TimeStatistics(getTotalTime(0), getExecutionTime(0));
    }

    private String getTextValue(String tagName, int index) {
        String textVal = null;
        NodeList nl = root.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0 && index < nl.getLength()) {
            Element el = ( Element ) nl.item(index);
            Node node = el.getFirstChild();
            if (node != null) {
                textVal = node.getNodeValue();
            }
        }

        return textVal;
    }

    private void addTextValue(Element parent, String tagName, String value, boolean cdata) {
        Element element = dom.createElement(tagName);
        Text eleValue = cdata ? dom.createCDATASection(value) : dom.createTextNode(value);
        element.appendChild(eleValue);
        parent.appendChild(element);
    }

    private int getIntValue(String tagName, int index) {
        return Integer.parseInt(getTextValue(tagName, index));
    }

    private long getLongValue(String tagName, int index) {
        String value = getTextValue(tagName, index);
        return value == null ? 0 : Long.parseLong(value);
    }

    private void addNumberValue(Element parent, String tagName, Number value) {
        Element element = dom.createElement(tagName);
        Text eleValue = dom.createTextNode(String.valueOf(value));
        element.appendChild(eleValue);
        parent.appendChild(element);
    }
}
