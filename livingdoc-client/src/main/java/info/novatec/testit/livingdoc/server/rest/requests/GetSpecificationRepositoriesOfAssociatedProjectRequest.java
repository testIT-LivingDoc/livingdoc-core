package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetSpecificationRepositoriesOfAssociatedProjectRequest {
    public Repository repository;

    public GetSpecificationRepositoriesOfAssociatedProjectRequest() {
    }

    public GetSpecificationRepositoriesOfAssociatedProjectRequest(Repository repository) {
        this.repository = repository;
    }

}
