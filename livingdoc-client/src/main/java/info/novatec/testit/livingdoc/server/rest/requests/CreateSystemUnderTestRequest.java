package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class CreateSystemUnderTestRequest {

    public SystemUnderTest systemUnderTest;
    public Repository repository;

    public CreateSystemUnderTestRequest() {
    }

    public CreateSystemUnderTestRequest(SystemUnderTest systemUnderTest, Repository repository) {
        this.systemUnderTest = systemUnderTest;
        this.repository = repository;
    }

}
