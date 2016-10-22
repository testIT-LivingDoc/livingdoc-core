package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Execution;


public class RunSpecificationResponse {

    public Execution execution;

    public RunSpecificationResponse() {
    }

    public RunSpecificationResponse(Execution execution) {
        this.execution = execution;
    }

}
