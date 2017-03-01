package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;

public class GetAllRepositoriesForSystemUnderTestRequest {

    public SystemUnderTest systemUnderTest;

    public GetAllRepositoriesForSystemUnderTestRequest() {
    }

    public GetAllRepositoriesForSystemUnderTestRequest(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }
}
