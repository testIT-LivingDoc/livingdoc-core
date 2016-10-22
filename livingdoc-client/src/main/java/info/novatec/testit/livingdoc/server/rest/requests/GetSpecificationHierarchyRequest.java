package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class GetSpecificationHierarchyRequest {

    public Repository repository;
    public SystemUnderTest systemUnderTest;

    public GetSpecificationHierarchyRequest() {
    }

    public GetSpecificationHierarchyRequest(Repository repository, SystemUnderTest systemUnderTest) {
        this.repository = repository;
        this.systemUnderTest = systemUnderTest;
    }

}
