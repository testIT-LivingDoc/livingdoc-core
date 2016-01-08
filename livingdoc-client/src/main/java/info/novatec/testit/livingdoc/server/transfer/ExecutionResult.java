package info.novatec.testit.livingdoc.server.transfer;

import java.io.Serializable;
import java.util.Vector;

import info.novatec.testit.livingdoc.server.domain.Marshalizable;


/**
 * Pojo mapping the execution result data to the {@link Vector} format supported
 * by XML RPC.
 */
@SuppressWarnings("serial")
public class ExecutionResult implements Serializable, Marshalizable {

    public final static String SUCCESS = "<success>";

    public final static int SPACEKEY_IDX = 0;
    public final static int PAGETITLE_IDX = 1;
    public final static int SUT_IDX = 2;
    public final static int XMLREPORT_IDX = 3;

    private String spaceKey;
    private String pageTitle;
    private String sut;
    private String xmlReport;

    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getSut() {
        return sut;
    }

    public void setSut(String sut) {
        this.sut = sut;
    }

    public String getXmlReport() {
        return xmlReport;
    }

    public void setXmlReport(String xmlReport) {
        this.xmlReport = xmlReport;
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(SPACEKEY_IDX, spaceKey);
        parameters.add(PAGETITLE_IDX, pageTitle);
        parameters.add(SUT_IDX, sut);
        parameters.add(XMLREPORT_IDX, xmlReport);
        return parameters;
    }

}
