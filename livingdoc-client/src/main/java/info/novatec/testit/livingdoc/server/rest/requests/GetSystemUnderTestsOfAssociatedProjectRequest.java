package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetSystemUnderTestsOfAssociatedProjectRequest {

    public Repository repository;

    public GetSystemUnderTestsOfAssociatedProjectRequest() {
    }

    public GetSystemUnderTestsOfAssociatedProjectRequest(Repository repository) {
        this.repository = repository;
    }

}
