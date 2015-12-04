package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.expectation.Expectation;


public final class Result {
    private Expectation expectation;
    private Object actual;
    private Throwable exception;

    public Result() {
    }

    public Result(Expectation expectation) {
        this.expectation = expectation;
    }

    public boolean isIgnored() {
        return ! isException() && expectation == null;
    }

    public boolean isRight() {
        return ! isException() && ! isIgnored() && expectation.meets(actual);
    }

    public boolean isWrong() {
        return ! isException() && ! isIgnored() && ! isRight();
    }

    public boolean isException() {
        return exception != null;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public Expectation getExpected() {
        return expectation;
    }

    public Object getActual() {
        return actual;
    }

    public void exceptionOccured(Throwable t) {
        if ( ! isIgnored() && expectation.meets(t)) {
            actual = t;
        } else {
            exception = t;
        }
    }

    public Throwable getException() {
        return exception;
    }
}
