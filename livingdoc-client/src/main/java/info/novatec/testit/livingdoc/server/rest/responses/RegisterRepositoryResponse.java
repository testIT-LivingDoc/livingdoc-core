package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class RegisterRepositoryResponse {

    public Repository repository;

    public RegisterRepositoryResponse() {
    }

    public RegisterRepositoryResponse(Repository repository) {
        this.repository = repository;
    }

}
