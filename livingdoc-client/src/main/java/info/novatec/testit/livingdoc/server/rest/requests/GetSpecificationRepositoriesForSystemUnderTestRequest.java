package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class GetSpecificationRepositoriesForSystemUnderTestRequest {
    public SystemUnderTest systemUnderTest;

    public GetSpecificationRepositoriesForSystemUnderTestRequest() {
    }

    public GetSpecificationRepositoriesForSystemUnderTestRequest(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

}
