package info.novatec.testit.livingdoc.server.domain;

import com.fasterxml.jackson.annotation.*;
import info.novatec.testit.livingdoc.report.*;
import info.novatec.testit.livingdoc.util.*;
import org.apache.commons.lang3.*;

import javax.persistence.*;
import java.sql.*;
import java.util.*;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


@Entity
@Table(name = "EXECUTION", indexes = {@Index(columnList = "EXECUTION_DATE", name = "executionDateIndex")})
@SuppressWarnings("serial")

public class Execution extends AbstractUniqueEntity implements Comparable<Execution> {
    public static final String NOT_RUNNED = "notrunned";
    public static final String IGNORED = "ignored";
    public static final String SUCCESS = "success";
    public static final String FAILED = "error";

    private Specification specification;
    private SystemUnderTest systemUnderTest;
    private String sections;

    private String results;
    private int success;
    private int failures;
    private int errors;
    private int ignored;
    private String executionErrorId;
    private Timestamp executionDate;
    private boolean executedRemotely;

    public static Execution none() {
        return new Execution();
    }

    public static Execution error(Specification specification, SystemUnderTest systemUnderTest, String sections,
                                  String errorId) {
        Execution execution = new Execution();
        execution.setSystemUnderTest(systemUnderTest);
        execution.setExecutionDate(new Timestamp(System.currentTimeMillis()));
        execution.setSpecification(specification);
        execution.setExecutionErrorId(errorId);
        execution.setSections(sections);
        return execution;
    }

    public static Execution newInstance(Specification specification, SystemUnderTest systemUnderTest, XmlReport xmlReport) {
        if (xmlReport.getGlobalException() != null) {
            return error(specification, systemUnderTest, null, xmlReport.getGlobalException());
        }

        Execution execution = new Execution();
        execution.setExecutionDate(new Timestamp(System.currentTimeMillis()));
        execution.setSpecification(specification);
        execution.setSystemUnderTest(systemUnderTest);

        execution.setFailures(xmlReport.getFailure(0));
        execution.setErrors(xmlReport.getError(0));
        execution.setSuccess(xmlReport.getSuccess(0));
        execution.setIgnored(xmlReport.getIgnored(0));
        if (execution.wasRunned()) {
            execution.setResults(xmlReport.getResults(0));
        }

        if (xmlReport.getSections(0) != null) {
            StringBuilder sections = new StringBuilder();
            int index = 0;

            while (xmlReport.getSections(index) != null) {
                if (index > 0) {
                    sections.append(',');
                }
                sections.append(xmlReport.getSections(index));
                index++;
            }

            execution.setSections(sections.toString());
        }

        return execution;
    }

    public static Execution newInstance(Specification specification, SystemUnderTest systemUnderTest, XmlReport xmlReport,
                                        String sections) {
        Execution execution = Execution.newInstance(specification, systemUnderTest, xmlReport);
        execution.setSections(sections);
        return execution;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "SPECIFICATION_ID", nullable = false)
    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "SUT_ID", nullable = false)
    public SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    public void setSystemUnderTest(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    @Basic
    @Column(name = "SECTIONS", nullable = true, length = 50)
    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    @Column(name = "RESULTS", nullable = true, length = 2147483647)
    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    @Column(name = "ERRORID", nullable = true, length = 2147483647)
    public String getExecutionErrorId() {
        return executionErrorId;
    }

    public void setExecutionErrorId(String executionErrorId) {
        this.executionErrorId = executionErrorId;
    }

    @Basic
    @Column(name = "SUCCESS_COUNT")
    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Basic
    @Column(name = "IGNORED_COUNT")
    public int getIgnored() {
        return ignored;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    @Basic
    @Column(name = "FAILURES_COUNT")
    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    @Basic
    @Column(name = "ERRORS_COUNT")
    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    @Basic
    @Column(name = "EXECUTION_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Timestamp getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Timestamp executionDate) {
        this.executionDate = executionDate;
    }

    public boolean hasException() {
        return !StringUtils.isEmpty(executionErrorId);
    }

    public boolean hasFailed() {
        return hasException() || failures + errors > 0;
    }

    public boolean hasSucceeded() {
        return !hasFailed() && success > 0;
    }

    public boolean wasIgnored() {
        return !hasFailed() && !hasSucceeded() && ignored != 0;
    }

    public boolean wasRunned() {
        return hasFailed() || hasSucceeded() || ignored != 0;
    }

    public boolean wasRemotelyExecuted() {
        return this.executedRemotely;
    }

    public void setRemotelyExecuted() {
        this.executedRemotely = true;
    }

    @Transient
    public String getStatus() {
        if (hasFailed()) {
            return FAILED;
        }
        if (hasSucceeded()) {
            return SUCCESS;
        }
        if (ignored != 0) {
            return IGNORED;
        }
        return NOT_RUNNED;
    }

    @Transient
    public String getCleanedResults() {
        return HtmlUtil.cleanUpResults(results);
    }

    @Transient
    public String getFormattedExecutionDate() {
        FormattedDate d = new FormattedDate(executionDate);
        return d.getFormattedTimestamp();
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(EXECUTION_RESULTS_IDX, StringUtils.stripToEmpty(results));
        parameters.add(EXECUTION_ERRORID_IDX, StringUtils.stripToEmpty(executionErrorId));
        parameters.add(EXECUTION_FAILIURES_IDX, failures);
        parameters.add(EXECUTION_ERRORS_IDX, errors);
        parameters.add(EXECUTION_SUCCESS_IDX, success);
        parameters.add(EXECUTION_IGNORED_IDX, ignored);
        parameters.add(EXECUTION_EXECUTION_DATE_IDX, getFormattedExecutionDate());
        return parameters;
    }

    public Execution marshallizeRest() {

        Execution returnValue = Execution.none();
        returnValue.setSystemUnderTest(this.systemUnderTest != null ? this.getSystemUnderTest().marshallizeRest() : null);
        returnValue.setId(this.getId());
        returnValue.setVersion(this.getVersion());
        returnValue.setExecutionDate(this.getExecutionDate());
        returnValue.setFailures(this.getFailures());
        returnValue.setErrors(this.getErrors());
        returnValue.setExecutionErrorId(getExecutionErrorId());
        returnValue.setIgnored(this.getIgnored());
        returnValue.setResults(this.getResults());
        returnValue.setSuccess(this.getSuccess());
        returnValue.setSections(this.getSections());
        returnValue.setUUID(this.getUUID());
        return returnValue;
    }


    @Override
    public int compareTo(Execution o) {
        return executionDate.compareTo(o.executionDate);
    }

    public boolean isSimilar(Execution execution) {
        return execution != null && errors == execution.getErrors() && failures == execution.getFailures()
                && success == execution.getSuccess() && ignored == execution.getIgnored() && StringUtils.equals(executionErrorId,
                execution.getExecutionErrorId()) && StringUtils.equals(results, execution.getResults());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Execution)) {
            return false;
        }

        return super.equals(o);
    }
}
