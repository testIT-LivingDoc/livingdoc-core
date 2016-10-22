package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Requirement;


public class GetRequirementReferencesRequest {

    public Requirement requirement;

    public GetRequirementReferencesRequest() {
    }

    public GetRequirementReferencesRequest(Requirement requirement) {
        this.requirement = requirement;
    }

}
