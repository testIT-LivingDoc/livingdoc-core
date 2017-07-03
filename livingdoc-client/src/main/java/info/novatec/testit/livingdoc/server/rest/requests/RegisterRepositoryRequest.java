package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class RegisterRepositoryRequest {

    public Repository repository;

    public RegisterRepositoryRequest() {
    }

    public RegisterRepositoryRequest(Repository repository) {
        this.repository = repository;
    }

}
