package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetRegisteredRepositoryResponse {

    public Repository repository;

    public GetRegisteredRepositoryResponse() {
    }

    public GetRegisteredRepositoryResponse(Repository repository) {
        this.repository = repository;
    }

}
