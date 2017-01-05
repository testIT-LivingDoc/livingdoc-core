package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Runner;


public class UpdateRunnerRequest {

    public String oldRunnerName;
    public Runner runner;

    public UpdateRunnerRequest() {
    }

    public UpdateRunnerRequest(final String oldRunnerName, Runner runner) {

        this.oldRunnerName = oldRunnerName;
        this.runner = runner;
    }
}
