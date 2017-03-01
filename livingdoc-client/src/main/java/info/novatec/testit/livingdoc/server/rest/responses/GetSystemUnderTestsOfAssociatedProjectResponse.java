package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class GetSystemUnderTestsOfAssociatedProjectResponse {
    public Set<SystemUnderTest> systemUnderTestsOfAssociatedProject;

    public GetSystemUnderTestsOfAssociatedProjectResponse() {
    }

    public GetSystemUnderTestsOfAssociatedProjectResponse(Set<SystemUnderTest> systemUnderTestsOfAssociatedProject) {
        this.systemUnderTestsOfAssociatedProject = systemUnderTestsOfAssociatedProject;
    }

    public Set<SystemUnderTest> getSystemUnderTestsOfAssociatedProject() {
        return systemUnderTestsOfAssociatedProject;
    }
}
