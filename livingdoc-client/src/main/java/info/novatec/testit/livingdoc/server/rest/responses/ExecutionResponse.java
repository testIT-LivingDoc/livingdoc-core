package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Execution;

public class ExecutionResponse {

    private Execution execution;

    public ExecutionResponse(Execution execution) {
        this.execution = execution;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }
}
