package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetAllRepositoriesForSystemUnderTestResponse {

    public Set<Repository> allRepositoriesForSystemUnderTest;

    public GetAllRepositoriesForSystemUnderTestResponse() {
    }

    public GetAllRepositoriesForSystemUnderTestResponse(Set<Repository> allRepositoriesForSystemUnderTest) {
        this.allRepositoriesForSystemUnderTest = allRepositoriesForSystemUnderTest;
    }

    public Set<Repository> getAllRepositoriesForSystemUnderTest() {
        return allRepositoriesForSystemUnderTest;
    }
}
