package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_ERRORS_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_EXCEPTION_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_FAILIURES_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_REFERENCES_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_SUCCESS_IDX;

import java.util.Vector;


public class RequirementSummary implements Marshalizable {
    private int referencesSize = 0;
    private int success = 0;
    private int errors = 0;
    private int failures = 0;
    private int exceptions = 0;

    public RequirementSummary() {
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getExceptions() {
        return exceptions;
    }

    public void setExceptions(int exceptions) {
        this.exceptions = exceptions;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getReferencesSize() {
        return referencesSize;
    }

    public void setReferencesSize(int referencesSize) {
        this.referencesSize = referencesSize;
    }

    public void addErrors(int errorCount) {
        this.errors += errorCount;
    }

    public void addSuccess(int successCount) {
        this.success += successCount;
    }

    public void addFailures(int failureCount) {
        this.failures += failureCount;
    }

    public void addException(boolean hasException) {
        if (hasException) {
            exceptions ++ ;
        }
    }

    public boolean getTestsIgnored() {
        return success == 0 && ! getTestsFailed();
    }

    public boolean getTestsFailed() {
        return failures + errors + exceptions > 0;
    }

    public boolean getTestsSucceeded() {
        return success > 0 && ! getTestsFailed();
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(SUMMARY_REFERENCES_IDX, referencesSize);
        parameters.add(SUMMARY_FAILIURES_IDX, failures);
        parameters.add(SUMMARY_ERRORS_IDX, errors);
        parameters.add(SUMMARY_SUCCESS_IDX, success);
        parameters.add(SUMMARY_EXCEPTION_IDX, exceptions);
        return parameters;
    }
}
