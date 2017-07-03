package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetRequirementRepositoriesOfAssociatedProjectRequest {

    public Repository repository;

    public GetRequirementRepositoriesOfAssociatedProjectRequest() {
    }

    public GetRequirementRepositoriesOfAssociatedProjectRequest(Repository repository) {
        this.repository = repository;
    }

}
