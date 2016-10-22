package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Runner;


public class CreateRunnerRequest {

    public Runner runner;

    public CreateRunnerRequest() {
    }

    public CreateRunnerRequest(Runner runner) {
        this.runner = runner;
    }
}
