package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.Set;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class GetSpecificationReferencesResponse {

    public Set<Reference> references;

    public GetSpecificationReferencesResponse() {
    }

    public GetSpecificationReferencesResponse(Set<Reference> references) {
        this.references = references;
    }

    public Set<Reference> getReferences() {
        return references;
    }
}
