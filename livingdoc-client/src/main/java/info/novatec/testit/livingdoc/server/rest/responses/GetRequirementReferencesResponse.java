package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class GetRequirementReferencesResponse {

    public Set<Reference> references;

    public GetRequirementReferencesResponse() {
    }

    public GetRequirementReferencesResponse(Set<Reference> references) {
        this.references = references;
    }

    public Set<Reference> getReferences() {
        return references;
    }

}
