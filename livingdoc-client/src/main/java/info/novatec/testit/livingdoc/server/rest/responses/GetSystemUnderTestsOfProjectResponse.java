package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class GetSystemUnderTestsOfProjectResponse {

    public Set<SystemUnderTest> systemUnderTestsOfProject;

    public GetSystemUnderTestsOfProjectResponse() {
    }

    public GetSystemUnderTestsOfProjectResponse(Set<SystemUnderTest> systemUnderTestsOfProject) {
        this.systemUnderTestsOfProject = systemUnderTestsOfProject;
    }

    public Set<SystemUnderTest> getSystemUnderTestsOfProject() {
        return systemUnderTestsOfProject;
    }
}
