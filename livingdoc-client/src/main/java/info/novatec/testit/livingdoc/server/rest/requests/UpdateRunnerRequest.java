package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Runner;


public class UpdateRunnerRequest {

    public Runner runner;

    public UpdateRunnerRequest() {
    }

    public UpdateRunnerRequest(Runner runner) {
        this.runner = runner;
    }
}
