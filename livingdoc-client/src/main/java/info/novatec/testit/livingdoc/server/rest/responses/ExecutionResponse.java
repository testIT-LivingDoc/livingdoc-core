package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Execution;

public class ExecutionResponse {

    public Execution execution;

    public ExecutionResponse() {
    }

    public ExecutionResponse(Execution execution) {
        this.execution = execution;
    }
}
