package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetRegisteredRepositoryRequest {
    public Repository repository;

    public GetRegisteredRepositoryRequest() {
    }

    public GetRegisteredRepositoryRequest(Repository repository) {
        this.repository = repository;
    }

}
