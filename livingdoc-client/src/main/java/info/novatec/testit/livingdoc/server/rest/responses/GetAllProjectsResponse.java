package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Project;


public class GetAllProjectsResponse {

    public Set<Project> allProjects;

    public GetAllProjectsResponse() {
    }

    public GetAllProjectsResponse(Set<Project> allProjects) {
        this.allProjects = allProjects;
    }

    public Set<Project> getAllProjects() {
        return allProjects;
    }
}
