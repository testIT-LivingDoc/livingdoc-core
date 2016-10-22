package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class UpdateRepositoryRegistrationRequest {
    public Repository repository;

    public UpdateRepositoryRegistrationRequest() {
    }

    public UpdateRepositoryRegistrationRequest(Repository repository) {
        this.repository = repository;
    }

}
