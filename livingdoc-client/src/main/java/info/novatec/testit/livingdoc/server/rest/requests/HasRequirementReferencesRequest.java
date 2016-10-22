package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Requirement;


public class HasRequirementReferencesRequest {

    public Requirement requirement;

    public HasRequirementReferencesRequest() {
    }

    public HasRequirementReferencesRequest(Requirement requirement) {
        this.requirement = requirement;
    }

}
