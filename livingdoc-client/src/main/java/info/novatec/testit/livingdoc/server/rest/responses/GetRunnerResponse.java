package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Runner;


public class GetRunnerResponse {

    public Runner runner;

    public GetRunnerResponse() {
    }

    public GetRunnerResponse(Runner runner) {
        this.runner = runner;
    }

}
