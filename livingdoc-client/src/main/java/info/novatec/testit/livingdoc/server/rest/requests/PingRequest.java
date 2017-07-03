package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;

public class PingRequest {

    public Repository repository;

    public PingRequest() {
    }

    public PingRequest(Repository repository) {
        this.repository = repository;
    }
}
