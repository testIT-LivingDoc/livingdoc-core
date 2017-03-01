package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Runner;

import java.util.Set;


public class GetAllRunnersResponse {

    public Set<Runner> runners;

    public GetAllRunnersResponse(Set<Runner> runners) {
        this.runners = runners;
    }

    public Set<Runner> getRunners() {
        return runners;
    }
}
