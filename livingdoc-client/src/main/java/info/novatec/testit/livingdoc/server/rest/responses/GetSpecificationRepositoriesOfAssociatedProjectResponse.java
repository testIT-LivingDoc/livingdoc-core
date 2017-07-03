package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetSpecificationRepositoriesOfAssociatedProjectResponse {

    public Set<Repository> specificationRepositoriesOfAssociatedProject;

    public GetSpecificationRepositoriesOfAssociatedProjectResponse() {
    }

    public GetSpecificationRepositoriesOfAssociatedProjectResponse(
        Set<Repository> specificationRepositoriesOfAssociatedProject) {
        this.specificationRepositoriesOfAssociatedProject = specificationRepositoriesOfAssociatedProject;
    }

    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject() {
        return specificationRepositoriesOfAssociatedProject;
    }
}
