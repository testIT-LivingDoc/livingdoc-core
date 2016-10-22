package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Requirement;


public class RemoveRequirementRequest {

    public Requirement requirement;

    public RemoveRequirementRequest() {
    }

    public RemoveRequirementRequest(Requirement requirement) {
        this.requirement = requirement;
    }

}
