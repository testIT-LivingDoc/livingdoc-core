package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Repository;


public class GetAllSpecificationRepositoriesResponse {

    public Set<Repository> allSpecificationRepositories;

    public GetAllSpecificationRepositoriesResponse() {
    }

    public GetAllSpecificationRepositoriesResponse(Set<Repository> allSpecificationRepositories) {
        this.allSpecificationRepositories = allSpecificationRepositories;
    }

    public Set<Repository> getAllSpecificationRepositories() {
        return allSpecificationRepositories;
    }
}
