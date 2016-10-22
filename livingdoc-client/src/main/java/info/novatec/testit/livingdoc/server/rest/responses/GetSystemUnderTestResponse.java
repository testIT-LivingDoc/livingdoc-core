package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class GetSystemUnderTestResponse {

    public SystemUnderTest systemUnderTest;

    public GetSystemUnderTestResponse() {
    }

    public GetSystemUnderTestResponse(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

}
