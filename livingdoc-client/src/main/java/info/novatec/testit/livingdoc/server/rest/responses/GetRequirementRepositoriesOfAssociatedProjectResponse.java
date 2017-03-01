package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetRequirementRepositoriesOfAssociatedProjectResponse {
    public Set<Repository> requirementRepositoriesOfAssociatedProject;

    public GetRequirementRepositoriesOfAssociatedProjectResponse() {
    }

    public GetRequirementRepositoriesOfAssociatedProjectResponse(
        Set<Repository> requirementRepositoriesOfAssociatedProject) {
        this.requirementRepositoriesOfAssociatedProject = requirementRepositoriesOfAssociatedProject;
    }

    public Set<Repository> getRequirementRepositoriesOfAssociatedProject() {
        return requirementRepositoriesOfAssociatedProject;
    }
}
