package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetSpecificationRepositoriesForSystemUnderTestResponse {

    public Set<Repository> specificationRepositoriesOfAssociatedProject;

    public GetSpecificationRepositoriesForSystemUnderTestResponse() {
    }

    public GetSpecificationRepositoriesForSystemUnderTestResponse(
        Set<Repository> specificationRepositoriesOfAssociatedProject) {
        this.specificationRepositoriesOfAssociatedProject = specificationRepositoriesOfAssociatedProject;
    }

    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject() {
        return specificationRepositoriesOfAssociatedProject;
    }
}
