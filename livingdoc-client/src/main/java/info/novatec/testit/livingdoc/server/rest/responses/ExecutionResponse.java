package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Execution;

public class ExecutionResponse {

    private String execution;

    public ExecutionResponse() {
    }

    public ExecutionResponse(String execution) {
        this.execution = execution;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    /* private Execution execution;

    public ExecutionResponse() {
    }

    public ExecutionResponse(Execution execution) {
        this.execution = execution;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }*/
}
